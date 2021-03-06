import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class IPC_domains {
	public static XESGenerator xes_generator = new XESGenerator();
	
	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		
		// find the folder of the domain
		String domain = args[0];  // blocks-world
		
		String folder_name = "../datasets/table_4/" + domain + "/";
		int[] percent_list = new int[]{10,30,50,70,100};
		String format = "XES";
		
		FileWriter csvWriter = new FileWriter("../outputs/output.csv");
		csvWriter.append("Percent,Real_Goal,Time,Cost,Prob,Results\n");
		
		alignmentTool alignmentTool = null;
		for (int percent : percent_list) {
			// find each problem set
			String dir_name = folder_name + "/problems/" + Integer.toString(percent);
			File dir = new File(dir_name);
			File[] problem_list = dir.listFiles();
			int problem_number = problem_list.length;
			
			for (int i = 0; i < problem_number; i++) {
				String target_folder = dir_name + "/"+ Integer.toString(i) + "/train/";
				
				System.out.println(target_folder);
				
				File models = new File(target_folder);
				File[] model_list = models.listFiles();
				
				int model_count = 0;
				for (File model : model_list) {
					if (model.isFile()) {
						int len = model.getName().length();
						if (model.getName().charAt(len-1) == 's') {
							model_count++;
						}
					}
				}
				
				if (model_count > 0) {
					// index all models once
					alignmentTool = new alignmentTool(model_count, target_folder, format);
				}
				
				// test for once
				// create sequence for testing
				
				String test_dir = folder_name + "/test/" + Integer.toString(percent) + "/p_" + Integer.toString(i) + "/";
				BufferedReader br = new BufferedReader(new FileReader(test_dir + "goal.txt"));
				String goal = br.readLine().toString();
				
				Sequence s = xes_generator.pick_sequence_lower_case(test_dir, Integer.parseInt(goal), 1);  // only have one trace which is 0
				int step = s.sequence.size();
				
				long begin = System.nanoTime();
				ArrayList<Integer> costs = null;
				ArrayList<Double> probabilities = null;
				//System.out.println(alignmentTool);
				costs = alignmentTool.allCosts(s, step, "MoL", "IncreasingCost");
				//probabilities = alignmentTool.probabilities_without_beta(costs);
				probabilities = alignmentTool.probabilities(costs);
				
				ArrayList<Integer> results = alignmentTool.best_match(probabilities);
				long end = System.nanoTime();
				long time = end - begin;
				
				// re-formating to avoid CSV output be split by comma
				String output_cost = "";
				for (int c : costs) {
					output_cost = output_cost + c + "/";
				}
				String output_prob = "";
				for (double p : probabilities) {
					output_prob = output_prob + p + "/";
				}
				String output_results = "";
				for (int r : results) {
					output_results = output_results + r + "/";
				}
				
				System.out.println("Step " + (step) + ": " + costs);
				System.out.println(probabilities);
				System.out.println(results);
				System.out.println("Step " + (step) + ": " + s.sequence);
				
				// csvWriter.append("Percent,Real_Goal,Time,Cost,Prob,Results\n");
				csvWriter.append(percent + "," + s.model + "," + time + "," + output_cost + "," + output_prob + "," + output_results + "\n");
				
			}
			
			
		}
		csvWriter.flush();
		csvWriter.close();
	}
}
