import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BPIC_Simulation {

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		XESGenerator xes_generator = new XESGenerator();
		
		String instruction;
		Command com;
		int goals = 8;
		
		FileWriter csvWriter = new FileWriter("./cose_64.csv");
		csvWriter.append("Model,Step,Time,Cost,Prob,Results\n");
		
		alignmentTool alignmentTool = new alignmentTool(goals, "./cose_64/");
		
		int count = 0;
		for (int j = 0; j < goals; j++) {
			
			File dir_goal = new File("./cose_64/goal"+ j);
			File goalDir[] = dir_goal.listFiles();
			int test = goalDir.length;
			
			
			for (int i = 0; i < test; i++) {
				Sequence s = xes_generator.pick_sequence("./cose_64/goal"+ j +"/", j, i);
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
					
					count += 1;
					System.out.println("Iteration: " + count);
					System.out.println("Step " + (step) + ": " + costs);
					System.out.println(probabilities);
					System.out.println(results);
					System.out.println("Step " + (step) + ": " + tmp_s.sequence);
					// if adding the sequence step by step: s.sequence.subList(0, step+1)
					csvWriter.append(s.model + "," + step + "," + time + "," + output_cost + "," + output_prob + "," + output_results + "\n");
					csvWriter.flush();
				}
			}
		}
		
		csvWriter.close();
		
	}
		
	public static Sequence ramdom_sample (Sequence s, List<Integer> tmp_index){
		ArrayList<String> sequence = new ArrayList<String>();
		for (int i : tmp_index) {
			sequence.add(s.sequence.get(i));
		}
		return new Sequence(sequence, s.model);
	}
	
	public static List<Integer> shuffle_steps_index (int total_steps){
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < total_steps; i++) {
			list.add(i);
		}
		java.util.Collections.shuffle(list);
		return list;
	}
	
	public static ArrayList<Integer> percentList (int total_steps){
		ArrayList<Integer> lst = new ArrayList<Integer>();
		lst.add((int) (total_steps*0.1 + 1));
		lst.add((int) (total_steps*0.3 + 1));
		lst.add((int) (total_steps*0.5 + 1));
		lst.add((int) (total_steps*0.7 + 1));
		lst.add(total_steps);
		return lst;
	}

}
