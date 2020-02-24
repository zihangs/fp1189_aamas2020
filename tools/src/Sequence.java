import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.jbpt.petri.*;
import org.jbpt.petri.io.PNMLSerializer;

public class Sequence {
	
	public int model;
	public ArrayList<String> sequence;
	
	public Sequence() {
		this.model = selectModel();
		this.sequence = getSequence(model);
	}
	
	public Sequence(ArrayList<String> seq, int model) {
		this.model = model;
		this.sequence = seq;
	}
	
	private int selectModel() {
		Random rand = new Random();
		// if select pnml file from a folder known the number of pnml files
		int modelIndex = rand.nextInt(6) + 1;
		return modelIndex;
	}
	
	private ArrayList<String> getSequence(int n){
		String file = COMP90055.directory + "/pnml/pql/" + n + ".pnml";
		
		PNMLSerializer pnmls = new PNMLSerializer();
		NetSystem ns = pnmls.parse(file);
		
		// execute the net system
		
		Set<Place> source = ns.getSourcePlaces();
		Set<Place> sink = ns.getSinkPlaces();
		
		Iterator<Place> SrcIterator = source.iterator();
		
		ArrayList<String> sequence = new ArrayList<String>();
		Random rand = new Random();
		
		while(SrcIterator.hasNext()){
			Place place = SrcIterator.next();
			ns.putTokens(place, 1);
			while (!sink.contains(place)) {
				
				Set<Transition> NextTransition = ns.getEnabledTransitions();
				int index = rand.nextInt(NextTransition.size());
				Iterator<Transition> iter = NextTransition.iterator();
				for (int i = 0; i < index; i++) {
				    iter.next();
				}
				
				Transition t = iter.next();
				
				if (t.isObservable()) {
					sequence.add(t.getLabel());
				}
				
				Set<Place> NextPlaces = ns.getPostset(t);
				Set<Place> PreviousPlaces = ns.getPreset(t);
				
				//update all the tokens in the net system
				for (Place p : NextPlaces) {
				    ns.putTokens(p, ns.getTokens(p)+1);
				}
				for (Place p : PreviousPlaces) {
				    ns.putTokens(p, ns.getTokens(p)-1);
				}
				
				//ns.putTokens(p, tokens)
				index = rand.nextInt(NextPlaces.size());
				Iterator<Place> place_iter = NextPlaces.iterator();
				for (int i = 0; i < index; i++) {
					place_iter.next();
				}
				
				place = place_iter.next();
			}
		}
		return sequence;
	}
	
	
	// steps by steps matched queries
	// fully matched
	public ArrayList<String> full_matched_with_star() {
		int size = this.sequence.size();
		ArrayList<String> queries = new ArrayList<String>();
		for (int j = 1; j <= size; j++) {
			String query = "SELECT * FROM * WHERE Executes(<*,";
			for (int i = 0; i < j; i++) {
				query = query + "\"" + this.sequence.get(i) + "\",*,";
			}
			int endIndex = query.length()-1;
			int beginIndex = 0;
			query = query.substring(beginIndex, endIndex);
			query = query + ",*>);";
			queries.add(query);
		}
		return queries;
	}
	
	public ArrayList<String> exactly_matched_without_star() {
		int size = this.sequence.size();
		ArrayList<String> queries = new ArrayList<String>();
		for (int j = 1; j <= size; j++) {
			String query = "SELECT * FROM * WHERE Executes(<";
			for (int i = 0; i < j; i++) {
				query = query + "\"" + this.sequence.get(i) + "\",";
			}
			int endIndex = query.length()-1;
			int beginIndex = 0;
			query = query.substring(beginIndex, endIndex);
			query = query + ",*>);";
			queries.add(query);
		}
		return queries;
	}
	
	// partially matched
	public ArrayList<String> partial_matched_replaced_with_star(float partial_rate) {
		int size = this.sequence.size();
		ArrayList<String> queries = new ArrayList<String>();
		ArrayList<String> partial_sequence = new ArrayList<String>();
		
		Random r = new Random();
		for (int k = 0; k < size; k++) {
			int random = r.nextInt((int) (partial_rate*10));
			if (random != 0) {
				partial_sequence.add(this.sequence.get(k));
			}
			else {
				partial_sequence.add(null);
			}
		}
		
		for (int j = 1; j <= size; j++) {
			String query = "SELECT * FROM * WHERE Executes(<";
			for (int i = 0; i < j; i++) {
				String action = partial_sequence.get(i);
				if (action != null) {
					query = query + "\"" + action + "\",";
				} else {
					query = query + "*,";
				}
			}
			int endIndex = query.length()-1;
			int beginIndex = 0;
			query = query.substring(beginIndex, endIndex);
			query = query + ",*>);";
			queries.add(query);
		}
		return queries;
	}
	

}
