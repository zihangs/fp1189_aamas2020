import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class TestAlignmentCost {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// Directory for store testing plans
		File dir = new File("./testing/10_3_100/");
		int goal_num = 1;
		int plan_num = 6;
		int toStep = 10;
		
		File listDir[] = dir.listFiles();
		XESGenerator xes_generator = new XESGenerator();
		Sequence s = xes_generator.pick_sequence(listDir[goal_num].toString(), goal_num, plan_num);
		
		@SuppressWarnings("rawtypes")
		alignmentTool alignmentTool = new alignmentTool(dir.listFiles().length);
		
		// model
		int model_to_test = 2;
		int c = alignmentTool.cost(s, model_to_test, toStep, "MoL", "FlatCost");  //Or: IncreasingCost
		System.out.println("model: " + s.model);
		System.out.println("cost: " + c);
	}
}
