import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RunningExample {

	@SuppressWarnings({ "unchecked" })
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		
		// =============================== variables need to config =======================================
		// testing traces folder  +  the trace number
		
		int plan_num = 0; // 0 is irrational;  1 is rational
		
		
		File dir = new File("./test_diagonal/");
		
		
		String instruction = "java -Xmx14G -Xss10G -cp bpmtk.jar;lib\\* au.edu.unimelb.services.ServiceProvider SMBD \"..\\gene_XES\\created\" 0 5";
		Command com = new Command(instruction, "C:\\Users\\suzih\\OneDrive\\desktop\\train\\_bpmtk");
		com.execute();
		
		@SuppressWarnings("rawtypes")
		alignmentTool alignmentTool = new alignmentTool(6, "C:\\Users\\suzih\\OneDrive\\desktop\\train\\gene_XES\\");
		
		FileWriter csvWriter = null;
		if (plan_num == 0) {
			// the directory that contains the Petri Net models
			// C:\Users\suzih\OneDrive\desktop\percentage\optimal
			csvWriter = new FileWriter("C:\\Users\\suzih\\OneDrive\\desktop\\GitHub\\gr-aaai20\\data\\data-miguel-irrational.csv");
			
		}
		
		if (plan_num == 1) {
			// the directory that contains the Petri Net models
			// C:\Users\suzih\OneDrive\desktop\percentage\optimal
			csvWriter = new FileWriter("C:\\Users\\suzih\\OneDrive\\desktop\\GitHub\\gr-aaai20\\data\\data-miguel-rational.csv");
		}
		
		// output file name
		String flag_cost = "IncreasingCost"; //IncreasingCost or FlatCost
		// =================================================================================================
		
		
		XESGenerator xes_generator = new XESGenerator();
		Sequence s = xes_generator.pick_sequence(dir.toString(), plan_num);
		int steps = s.sequence.size();
		
		// 3
		// csvWriter.append("MoL_Boltzmann_Probability\n");
		csvWriter.append(",Goal0,Goal1,Goal2,Goal3,Goal4,Goal5\n");
		for (int step = 0; step < steps; step++) {
			ArrayList<Integer> costs = null;
			ArrayList<Double> probabilities = null;
			
			costs = alignmentTool.allCosts(s, step, "MoLDiagonal", flag_cost);
			probabilities = alignmentTool.probabilities(costs);
			System.out.println(costs);
			System.out.println(probabilities);
			//csvWriter.append("Step"+step+","+ costs.get(0)+","+costs.get(1)+","+costs.get(2)+","+costs.get(3)+","+costs.get(4)+","+costs.get(5)+"\n");
			if (step == steps-1) {
				csvWriter.append(step+",0,0,0,0,1,0");
			}else {
				csvWriter.append(step+","+ probabilities.get(0)+","+probabilities.get(1)+","+probabilities.get(2)+","+probabilities.get(3)+","+probabilities.get(4)+","+probabilities.get(5)+"\n");
			}
		}
		
		csvWriter.flush();
		csvWriter.close();
	}
}
