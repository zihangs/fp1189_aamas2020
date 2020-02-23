import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class path3goals {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//double[] irrationality = {0.1,0.2,0.3};
		double[] irrationality = {0.3};
		
		//int[][] threshold_list = {{90,90,90},{97,95,92},{98,97,94},{98,95,98},{100,100,100}};
		int[][] threshold_list = {{90,90,90},{97,95,92},{98,97,94},{98,95,98},{100,100,100}};
		int number_of_plans = 60;
		
		// for a certain world and goal:
		
		String instruction;
		Command com;
		
		BufferedReader csvReader = new BufferedReader(new FileReader("./goal_list.csv"));
		String row;
		
		instruction = "mkdir train_cases";
		com = new Command(instruction, "./");
		com.execute();
		
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
		    
		    int world = Integer.parseInt(data[0]);
		    int goals = Integer.parseInt(data[1]);
		    
		    String[] init_coordinate = data[2].split("\\|");
			int init_x = Integer.parseInt(init_coordinate[0]);
			int init_y = Integer.parseInt(init_coordinate[1]);
			
			
			double lower = 0.0;
			for (double upper : irrationality) {
				
				instruction = "mkdir " + world + "_" + goals + "_" + (int) (upper*100);
				com = new Command(instruction, "./train_cases/");
				com.execute();
				
				String newdir = "./train_cases/" + world + "_" + goals + "_" + (int) (upper*100) + "/";
				int threshold = threshold_list[(int) (world/10) - 1][(int) (upper*10) - 1];
				
				
				int count_traces = 0;
				int left_traces;
				int tmp_rand;
				for (int i = 0; i < goals; i++) {
					left_traces = number_of_plans - count_traces;
					if (i == goals-1) {
						tmp_rand = number_of_plans - count_traces;
					} else {
						tmp_rand = number_of_plans/goals;
						//tmp_rand = (int)(Math.random()*left_traces) + 1;
						count_traces += tmp_rand;
					}

					
					String[] goal_coordinate = data[3+i].split("\\|");
					int goal_x = Integer.parseInt(goal_coordinate[0]);
					int goal_y = Integer.parseInt(goal_coordinate[1]);
					RandomPath rp = new RandomPath(init_x, init_y, goal_x, goal_y, world, goals, threshold);
					
					int file_num = 1;
					
					instruction = "mkdir goal_" + i;
					com = new Command(instruction, newdir);
					com.execute();
					
					System.out.print(" world: " + world);
					System.out.print(" goal: " + goals);
					System.out.print(" threshold: " + upper);
					System.out.print(" goal_num : " + i + "\n");
					while (file_num <= tmp_rand) {
						file_num = rp.find_path(file_num, lower, upper, i);
					}
					
				}
				
				lower = upper;
			}
			
		}
		
		
		csvReader.close();
		
	}

}
