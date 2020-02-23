import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlocksWorld {
	public static XESGenerator xes_generator = new XESGenerator();
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
		int[] goalsArray = new int[]{3,6,9};
		FileWriter csvWriter = new FileWriter("./statistics.csv");
		csvWriter.append("Goals,Model,Step,Time,Cost,Prob,Results\n");
		for(int goals : goalsArray) {
			String folder = "./bw_training/bw_" + goals + "/";
			@SuppressWarnings("rawtypes")
			// create and index all PetriNet model in the folder
			alignmentTool alignmentTool = new alignmentTool(goals, folder);
			for (int i = 0; i < goals; i++) {
				File dir = new File("./bw_testing/bw_"+ goals + "/");
				File listDir[] = dir.listFiles();
				
				File dir_goal = new File("./bw_testing/bw_"+ goals + "/goal" + i + "/");
				File goalDir[] = dir_goal.listFiles();
				int test = goalDir.length;
				
				for (int j = 0; j < test; j++) {
					Sequence s = xes_generator.pick_sequence(listDir[i].toString(), i, j);
					int steps = s.sequence.size();
					List<Integer> shuffle_index = shuffle_steps_index (steps);
					ArrayList<Integer> step_list = percentList(steps);
					
					for (int step : step_list) {
						List<Integer> tmp_index = shuffle_index.subList(0, step);
						java.util.Collections.sort(tmp_index);
						
						Sequence tmp_s = ramdom_sample (s, tmp_index);
						
						long begin = System.nanoTime();
						ArrayList<Integer> costs = null;
						ArrayList<Double> probabilities = null;
						costs = alignmentTool.allCosts(tmp_s, step, "MoL", "IncreasingCost");
						probabilities = alignmentTool.probabilities(costs);
						//probabilities = alignmentTool.probabilities(costs);
						
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
						System.out.println("Step " + (step) + ": " + tmp_s.sequence);
						// if adding the sequence step by step: s.sequence.subList(0, step+1)
						csvWriter.append(goals + "," + s.model + "," + step + "," + time + "," + output_cost + "," + output_prob + "," + output_results + "\n");
					
					}
				}
			}
		}
		csvWriter.flush();
		csvWriter.close();
	}
	public static ArrayList<Integer> percentList (int total_steps){
		ArrayList<Integer> lst = new ArrayList<Integer>();
		
        lst.add((int) (total_steps*0.1+1));
		lst.add((int) (total_steps*0.3+1));
		lst.add((int) (total_steps*0.5+1));
		lst.add((int) (total_steps*0.7+1));
		lst.add(total_steps);
		return lst;
	}
	
	public static List<Integer> shuffle_steps_index (int total_steps){
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < total_steps; i++) {
			list.add(i);
		}
		java.util.Collections.shuffle(list);
		return list;
	}
	
	public static Sequence ramdom_sample (Sequence s, List<Integer> tmp_index){
		ArrayList<String> sequence = new ArrayList<String>();
		for (int i : tmp_index) {
			sequence.add(s.sequence.get(i));
		}
		return new Sequence(sequence, s.model);
	}
}
