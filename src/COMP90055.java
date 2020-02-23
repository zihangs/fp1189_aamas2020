import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class COMP90055 {
	
	public static String directory = "./PQL-1.3";
	public static PnmlTools tools = new PnmlTools(directory);

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, InterruptedException, 
	ClassNotFoundException, SQLException, JSONException{
		
		System.out.println("This is PQL simulation tools\n" +
	                       "Please select a task:\n" +
				           "1. generate XES file\n" +
				           "2. simulation");
		
		Scanner scan = new Scanner(System.in);
		String task = null;
		if (scan.hasNext()) {
            task = scan.next();
        }
		
		System.out.println("================ START ======================");
		
		// generate XES file from a fold of plans
		if (task.contentEquals("1")) {
			//C:\\Users\\suzih\\OneDrive\\desktop\\miquel\\size_10_mixed
			File dir = new File("C:\\Users\\suzih\\OneDrive\\desktop\\ll\\40_9_30");
			File listDir[] = dir.listFiles();
			
			// folder for output and store the XES event logs
			File new_folder = new File("C:\\Users\\suzih\\OneDrive\\desktop\\ll\\40_9_30");
			new_folder.mkdir();
			
			XESGenerator xes_generator = new XESGenerator();
			for (int i = 0; i < listDir.length; i++) {
			    if (listDir[i].isDirectory()) {
			    	xes_generator.generate(listDir[i].toString(), new_folder.toString() + "/created" + i + ".xes");

			    	System.out.println(listDir[i]);
			    }
			}
			System.out.println("successfully done");
		}
		
		
		if (task.contentEquals("2")) {
			
			// simulation options
			int simulation_times = 0;
			float partial_rate = (float) 0.5;
			
			int world = 10;
			int goals = 3;
			int plans = (int) (100 * 0.1);
			
			/*
			System.out.println("Please enter the times of simulation: ");
			if (scan.hasNext()) {
				simulation_times = Integer.parseInt(scan.next());
	        }
	        */
			
			System.out.println("Please enter a partial rate: ");
			if (scan.hasNext()) {
				partial_rate = Float.parseFloat(scan.next());
	        }
			
			File dir = new File("./testing/10_6_100/");
			File listDir[] = dir.listFiles();
			
			XESGenerator xes_generator = new XESGenerator();
			
			JSONObject full = new JSONObject();
			JSONObject partial = new JSONObject();
			
			simulation_times = goals * plans;
			
			full.put("num_of_iterations", simulation_times);
			partial.put("num_of_iterations", simulation_times);
			
			int iter_count = 0;
			for (int i = 0; i < goals; i++) {
				for (int j = 0; j < plans; j++) {
					Sequence s = xes_generator.pick_sequence(listDir[i].toString(), i, j);
					JSONObject a_record_full = new JSONObject();
					JSONObject a_record_partial = new JSONObject();
					
					System.out.println("======================");
					System.out.println("Iteration: " + iter_count);
					a_record_full.put("model", s.model);
					a_record_partial.put("model", s.model);
					System.out.println(s.model);
					
					
					//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
					
					/*
					ArrayList<String> full_queries = null;
					ArrayList<String> partial_queries = null;
					full_queries = s.exactly_matched_without_star();
					partial_queries = s.partial_matched_replaced_with_star(partial_rate);
					
					//executeSingleQuery(s);
					executeMultipleQuery(full_queries, a_record_full, iter_count);
					System.out.println("----------------------");
					executeMultipleQuery(partial_queries, a_record_partial, iter_count);
					System.out.println("======================");
					
					full.put("iteration_" + iter_count, a_record_full);
					partial.put("iteration_" + iter_count, a_record_partial);
					*/
					//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
					
					iter_count++;
				}
			}
			
			try {
				FileWriter file = new FileWriter("statistics_full.json");
	            file.write(full.toString());
	            file.flush();
	            file.close();
	            
	            file = new FileWriter("statistics_partial.json");
	            file.write(partial.toString());
	            file.flush();
	            file.close();
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
	
	
	
	// ========================  used python to show statistics  ========================================
	public static Object readJson(String filename) throws Exception {  
	    FileReader reader = new FileReader(filename);
	    JSONParser jsonParser = new JSONParser();
	    return jsonParser.parse(reader);
	}
	
	public static void getStatistics(JSONObject statistic_records) throws JSONException {
		double average_best_score = 0.0;
		double average_shortest_step_found_best = 0.0;
		int num_of_iterations = Integer.parseInt(statistic_records.get("num_of_iterations").toString());
		
		for (int i = 0; i < num_of_iterations; i++) {
			JSONObject a_record = (JSONObject) statistic_records.get("iteration_" + i);
			int steps = Integer.parseInt(a_record.get("num_queries").toString());
			String model = a_record.get("model").toString();
			
			double shortest_step_found_best = 0.0;
			double best_score = 0.0;
			
			JSONObject results = (JSONObject) a_record.get("results");
			for (int j = 0; j < steps; j++) {
				double found = 0.0;
				System.out.println(results.getClass());
				System.out.println(results);
				@SuppressWarnings("unchecked")
				//results.get(key)
				ArrayList<String> a_result = (ArrayList<String>) results.get("step_"+j);
				JSONObject result = (JSONObject) results.get("step_"+j+1);
				System.out.println(result.toString());
				if (a_result.contains(model)) {
					found = 1.0;
				}
				double score = found/a_result.size();
				if (score > best_score) {
					best_score = score;
					shortest_step_found_best = j + 1;
				}
			}
			average_shortest_step_found_best += shortest_step_found_best;
			average_best_score += best_score;
		}
		average_shortest_step_found_best = average_shortest_step_found_best/num_of_iterations;
		average_best_score = average_best_score/num_of_iterations;
		
		System.out.println("average_shortest_step_found_best: " + average_shortest_step_found_best);
		System.out.println("average_best_score: " + average_best_score);
	}
	
	@SuppressWarnings("unchecked")
	public static void executeMultipleQuery(ArrayList<String> queries, JSONObject a_record, int iter) throws IOException, JSONException {
		
		int total_step = 0;
		
		JSONObject json_query = new JSONObject();
		JSONObject json_results = new JSONObject();
		JSONObject json_time = new JSONObject();
		
		for (String query : queries) {
			System.out.println(query);
			PrintWriter out = new PrintWriter(directory + "/search.pql");
			out.println(query);
			out.close();
			
			QueryCommand q = tools.Query("search.pql");
			ArrayList<String> result = q.getResult();
			Long time = q.getTime();
			System.out.println("time: " + time);
			
			//record average querying time
			System.out.println(result);
			
			total_step++;
			json_query.put("step_" + total_step, query);
			json_results.put("step_" + total_step, result);
			json_time.put("step_" + total_step, time);
		}
		
		a_record.put("queries", json_query);
		a_record.put("results", json_results);
		a_record.put("time", json_time);
		a_record.put("num_queries", total_step);
	}
	
}		
		
		// add goal to the net
		
		/*
		int n = 1;
		NSModifier mdf = new NSModifier();
		String file = Run.directory + "/pnml/pql/" + n + ".pnml";
		PNMLSerializer pnmls = new PNMLSerializer();
		NetSystem ns = pnmls.parse(file);
		ArrayList<String> placeName = new ArrayList<String>();
		placeName.add("p8");
		placeName.add("p9");
		NetSystem output_ns = mdf.AddGoal(ns, placeName);
		System.out.println(output_ns);*/
		
		
		
		// translate PEP to PNML
		
		/*try {
			PEPreader.read();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/



