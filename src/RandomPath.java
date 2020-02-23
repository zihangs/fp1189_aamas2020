import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RandomPath {
	int boundary;
	int start_x;
	int start_y;
	
	int end_x;
	int end_y;
	
	int goals;
	
	int threshold;
	int anti_threshold;
	int mini;
	
	public RandomPath() {
		
	}
	
	public RandomPath(int init_x, int init_y, int goal_x, int goal_y, int world, int goals, int threshold) {
		this.boundary = world;
		this.start_x = init_x;
		this.start_y = init_y;
		
		this.end_x = goal_x;
		this.end_y = goal_y;
		this.threshold = threshold;
		this.anti_threshold = 100 - threshold;
		
		this.goals = goals;
		this.mini = mini_cost();
	}
	
	
	class Grid {
		int x;
		int y;
		public Grid(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() {
			return this.x;
		}
		
		public int getY() {
			return this.y;
		}
		
		public void setX(int x) {
			this.x = x;
		}
		
		public void setY(int y) {
			this.y = y;
		}
	}
	
	public int mini_cost() {

		int dis_x = Math.abs(start_x - end_x);
		int dis_y = Math.abs(start_y - end_y);
		if (dis_y > dis_x) {
			int straight = dis_y - dis_x;
			int diagonal = dis_x;
			return straight*10 + diagonal*14;
		} else {
			int straight = dis_x - dis_y;
			int diagonal = dis_y;
			return straight*10 + diagonal*14;
		}
	}
	
	
	public int find_path(int file_num, double lower, double upper, int goal_num) throws IOException {
		Grid curr = new Grid(start_x, start_y);
		
		int choice;
		int cost_count = 0;
		
		String plan = "";
		
		
		while (curr.getY() != end_y || curr.getX() != end_x) {
			
			double current_dis = distance(curr.getX(), curr.getY(), end_x, end_y);

			Grid prev = new Grid(curr.getX(), curr.getY());
			ArrayList<Grid> next_grids = new ArrayList<Grid>();
			
			
			int choice_count = 0;
			
			//  1 2 3
			//  4 x 5
			//  6 7 8
			

			// 1
			if (curr.getX() - 1 >= 0 && curr.getY() + 1 < boundary) {
				Grid next = new Grid(curr.getX() - 1, curr.getY() + 1);
				double dis = distance(next.getX(), next.getY(), end_x, end_y);
				
				if (dis < current_dis) { // good
					for (int i = 0; i < threshold; i++) {
						next_grids.add(next);
					}
					choice_count += threshold;
				} else {
					for (int i = 0; i < anti_threshold; i++) {
						next_grids.add(next);
					}
					choice_count += anti_threshold;
				}
			}
			
			// 2
			if (curr.getY() + 1 < boundary) {
				Grid next = new Grid(curr.getX(), curr.getY() + 1);
				double dis = distance(next.getX(), next.getY(), end_x, end_y);
				
				if (dis < current_dis) { // good
					for (int i = 0; i < threshold; i++) {
						next_grids.add(next);
					}
					choice_count += threshold;
				} else {
					for (int i = 0; i < anti_threshold; i++) {
						next_grids.add(next);
					}
					choice_count += anti_threshold;
				}
			}
			
			// 3
			if (curr.getX() + 1 < boundary && curr.getY() + 1 < boundary) {
				Grid next = new Grid(curr.getX() + 1, curr.getY() + 1);
				double dis = distance(next.getX(), next.getY(), end_x, end_y);
				
				if (dis < current_dis) { // good
					for (int i = 0; i < threshold; i++) {
						next_grids.add(next);
					}
					choice_count += threshold;
				} else {
					for (int i = 0; i < anti_threshold; i++) {
						next_grids.add(next);
					}
					choice_count += anti_threshold;
					
				}
			}
			
			// 4
			if (curr.getX() - 1 >= 0) {
				Grid next = new Grid(curr.getX() - 1, curr.getY());
				double dis = distance(next.getX(), next.getY(), end_x, end_y);
				
				if (dis < current_dis) { // good
					for (int i = 0; i < threshold; i++) {
						next_grids.add(next);
					}
					choice_count += threshold;
				} else {
					for (int i = 0; i < anti_threshold; i++) {
						next_grids.add(next);
					}
					choice_count += anti_threshold;
				}
			}
			
			// 5
			if (curr.getX() + 1 < boundary) {
				Grid next = new Grid(curr.getX() + 1, curr.getY());
				double dis = distance(next.getX(), next.getY(), end_x, end_y);
				
				if (dis < current_dis) { // good
					for (int i = 0; i < threshold; i++) {
						next_grids.add(next);
					}
					choice_count += threshold;
				} else {
					for (int i = 0; i < anti_threshold; i++) {
						next_grids.add(next);
					}
					choice_count += anti_threshold;
				}
			}
			
			// 6
			if (curr.getX() - 1 >= 0 && curr.getY() - 1 >= 0) {
				Grid next = new Grid(curr.getX() - 1, curr.getY() - 1);
				double dis = distance(next.getX(), next.getY(), end_x, end_y);
				
				if (dis < current_dis) { // good
					for (int i = 0; i < threshold; i++) {
						next_grids.add(next);
					}
					choice_count += threshold;
				} else {
					for (int i = 0; i < anti_threshold; i++) {
						next_grids.add(next);
					}
					choice_count += anti_threshold;
					
				}
			}
			
			// 7
			if (curr.getY() - 1 >= 0) {
				Grid next = new Grid(curr.getX(), curr.getY() - 1);
				double dis = distance(next.getX(), next.getY(), end_x, end_y);
				
				if (dis < current_dis) { // good
					for (int i = 0; i < threshold; i++) {
						next_grids.add(next);
					}
					choice_count += threshold;
				} else {
					for (int i = 0; i < anti_threshold; i++) {
						next_grids.add(next);
					}
					choice_count += anti_threshold;
					
				}
			}
			
			// 8
			if (curr.getX() + 1 < boundary && curr.getY() - 1 >= 0) {
				Grid next = new Grid(curr.getX() + 1, curr.getY() - 1);
				double dis = distance(next.getX(), next.getY(), end_x, end_y);
				
				if (dis < current_dis) { // good
					for (int i = 0; i < threshold; i++) {
						next_grids.add(next);
					}
					choice_count += threshold;
				} else {
					for (int i = 0; i < anti_threshold; i++) {
						next_grids.add(next);
					}
					choice_count += anti_threshold;
					
				}
			}
			
			choice = (int)(Math.random()*choice_count);
			
			Grid new_curr = next_grids.get(choice);
			
			int dis_x = prev.getX() - new_curr.getX();
			int dis_y = prev.getY() - new_curr.getY();
			
			int cost = 0;
			if (dis_x != 0 && dis_y != 0) {
				cost = 14;
			}
			
			if (dis_x == 0 && dis_y != 0) {
				cost = 10;
			}
			
			if (dis_x != 0 && dis_y == 0) {
				cost = 10;
			}
			
			if (cost != 0) {
				curr = new_curr;
				String loc_prev = "loc-x" + prev.getX() + "-y" + prev.getY();
				String loc_curr = "loc-x" + curr.getX() + "-y" + curr.getY();
				
				plan += "(move " + loc_prev + " " + loc_curr + ")\n";
				//System.out.println("(move " + loc_prev + " " + loc_curr + ")");
				//System.out.println(next_grids);
				cost_count += cost;
				if (cost_count > mini*(1+upper)){
					break;
				}
			}
		}
		//System.out.println(step_count);
		//System.out.println(cost_count);
		
		if (cost_count >= mini*(1+lower) && cost_count < mini*(1+upper)) {
			
			FileWriter csvWriter = new FileWriter("./train_cases/"+ boundary + "_" + goals + "_" + (int) (upper*100) + "/goal_" + goal_num + "/sas_plan."+file_num);
			csvWriter.append(plan + "; cost = "+ cost_count +" (unit cost)\n");
			csvWriter.flush();
			csvWriter.close();
			
			return file_num + 1;
		} else {
			return file_num;
		}
	}
	
	public double distance(int x1, int y1, int x2, int y2) {
		//double dis = Math.sqrt(  Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)  );
		double dis = Math.abs(x1 - x2) + Math.abs(y1 - y2);
		return dis;
	}
	
}
