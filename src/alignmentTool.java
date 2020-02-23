import java.sql.SQLException;
import java.util.ArrayList;

import org.jbpt.petri.IFlow;
import org.jbpt.petri.IMarking;

import org.jbpt.petri.INode;
import org.jbpt.petri.IPlace;
import org.jbpt.petri.ITransition;
import org.jbpt.petri.NetSystem;
import org.jbpt.petri.io.PNMLSerializer;
import org.jbpt.throwable.SerializationException;
import org.pql.alignment.AlignmentAPI;
import org.pql.alignment.PQLAlignment;
import org.pql.alignment.Replayer;
import org.pql.core.PQLTask;
import org.pql.core.PQLTrace;
import org.jbpt.petri.INetSystem;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;


public class alignmentTool <F extends IFlow<N>, N extends INode, P extends IPlace, 
T extends ITransition, M extends IMarking<F,N,P,T>> {
	String folder;
	
	public int num_of_goals;
	public ArrayList<PetrinetGraph> netLib;
	public ArrayList<AlignmentAPI<F,N,P,T,M>> apiLib;
	public ArrayList<String> output_strings;
	
	public alignmentTool(int num_of_goals){
		this.folder = "./gene_XES/";
		this.num_of_goals = num_of_goals;
		this.netLib = new ArrayList<PetrinetGraph>();
		this.apiLib = new ArrayList<AlignmentAPI<F,N,P,T,M>>();
		this.output_strings = new ArrayList<String>();
		prepare(num_of_goals);
	}
	
	
	public alignmentTool(int num_of_goals, String dir){
		this.folder = dir;
		this.num_of_goals = num_of_goals;
		this.netLib = new ArrayList<PetrinetGraph>();
		this.apiLib = new ArrayList<AlignmentAPI<F,N,P,T,M>>();
		this.output_strings = new ArrayList<String>();
		prepare(num_of_goals);
	}
	
	public void prepare(int num_of_goals) {
		for (int i=0; i<num_of_goals; i++) {
			try {
				storeNetsAPI(i);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				System.out.println("prepare error");
			}
		}
	}
	
	public void storeNetsAPI(int model) throws ClassNotFoundException, SQLException {
		//String file = folder + "created" + model + ".tsml.pnml";
		String file = folder + "created" + model + ".xes.pnml";
		PNMLSerializer pnmls = new PNMLSerializer();
		Petrinet net = PetrinetFactory.newPetrinet("PNML");
		@SuppressWarnings("unchecked")
		INetSystem<F,N,P,T,M> ns = (INetSystem<F,N,P,T,M>) pnmls.parse(file);
		System.out.println(file);
		ns.loadNaturalMarking();
		
		// set t* to silence T
		for (N n : ns.getNodes()) {
			if (!n.getLabel().isEmpty() && n.getLabel().charAt(0) == 't') {
				n.setLabel("");
			}
		}
		
		AlignmentAPI<F,N,P,T,M> api = new AlignmentAPI<F,N,P,T,M>(ns);
		PetrinetGraph net_graph = api.constructPetrinetGraph(net);
		this.netLib.add(net_graph);
		this.apiLib.add(api);
		
		// Output net strings
		try {
			String s = PNMLSerializer.serializePetriNet((NetSystem) ns);
			this.output_strings.add(s);
		} catch (SerializationException e) {
			e.printStackTrace();
			System.out.println("String error");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int cost(Sequence s, int model, int steps, String flag, String cost_flag) throws ClassNotFoundException, SQLException {
		
		PQLTrace trace = new PQLTrace();
		int count_step = 0;
		for (String label : s.sequence) {
			if(count_step > steps) {
				break;
			}
			PQLTask task = new PQLTask(label, 0.0);
			trace.addTask(task);
			count_step++;
		}
		
		trace.createTraceLog();
		Replayer rp = new Replayer(this.apiLib.get(model));
		PQLAlignment alignment = rp.getAlignment(this.netLib.get(model), trace.getTraceLog());
		
		if (flag.equals("MoL")) {
			return alignment.getAlignmentCostForMoL("4_parameters");
		} 
		if (flag.equals("MoLDiagonal")) {
			// 4 parameters
			return alignment.getAlignmentCostForMoLDiagonal(cost_flag);
			// 2 parameters
			// return alignment.getAlignmentCostForMoLDiagonal();
		}
		if (flag.equals("Total")) {
			return alignment.getAlignmentCost();
		}
		if (flag.equals("TotalDiagonal")) {
			return alignment.getAlignmentCostDiagonal();
		}
		return 9999999;
	}
	
	// return the best matched model
	public ArrayList<Integer> best_match(ArrayList<Double> probs){
		double best_prob = probs.get(0);
		ArrayList<Integer> results = new ArrayList<Integer>();
		results.add(0);
		
		for (int model = 1; model < num_of_goals; model++) {
			double tmp_prob = probs.get(model);
			
			if (tmp_prob == best_prob) {
				results.add(model);
			} else {
				if (tmp_prob > best_prob) {
					results = new ArrayList<Integer>();
					results.add(model);
					best_prob = tmp_prob;
				}
			}
		}
		return results;
	}
		
	public ArrayList<Integer> allCosts(Sequence s, int steps, String MoL, String cost_flag) throws ClassNotFoundException, SQLException {
		ArrayList<Integer> costs = new ArrayList<Integer>();
		for (int model = 0; model < num_of_goals; model++) {
			int tmp_cost = cost(s, model, steps, MoL, cost_flag);
			costs.add(tmp_cost);
		}
		add_init_cost(costs);
		return costs;
	}
	
	public ArrayList<Integer> add_init_cost(ArrayList<Integer> costs){
		int length = costs.size();
		for (int i=0; i < length; i++) {
			int tmp = costs.get(i)+50;   // with 50 initial cost
			costs.set(i, tmp);
		}
		return costs;
	}
	
	// Boltzmann distribution with parameter BETA
	public ArrayList<Double> probabilities(ArrayList<Integer> costs){
		ArrayList<Double> probability = new ArrayList<Double>();
		double sum = 0.0;
		int min_cost = costs.get(0);
		for (int c : costs) {
			if (c < min_cost) {
				min_cost = c;
			}
		}
		
		double beta = 1/(1.0 + min_cost);
		for (int c : costs) {
			sum += Math.exp(-beta*c);
		}
		
		for (int c : costs) {
			double p = Math.exp(-beta*c)/sum;
			probability.add(p);
		}
		return probability;
	}
	
	public ArrayList<Double> probabilities_without_beta(ArrayList<Integer> costs){
		ArrayList<Double> probability = new ArrayList<Double>();
		double sum = 0.0;
		for (int c : costs) {
			sum += Math.exp(-c);
		}
		
		for (int c : costs) {
			double p = Math.exp(-c)/sum;
			probability.add(p);
		}
		return probability;
	}	
}
