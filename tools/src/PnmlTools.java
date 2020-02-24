import java.io.IOException;

public class PnmlTools {
	String dir;
	
	public PnmlTools(String dir) {
		this.dir = dir;
	}
	
	public void OpenBot(String name) throws IOException {
		String instruction = "java -jar PQL.BOT-1.3.jar --name=" + name + " --sleep=5";
		Command command = new Command(instruction, dir);
		command.execute();
	}
	
	public void Store() throws IOException {
		String instruction = "java -jar PQL.TOOL-1.3.jar --store -pnml=./pnml/pql/";
		Command command = new Command(instruction, dir);
		command.execute();
	}
	
	public void Reset() throws IOException {
		String instruction = "java -jar PQL.TOOL-1.3.jar --reset";
		Command command = new Command(instruction, dir);
		System.out.println("yes");
		command.execute();
	}
	
	public void Index(int id) throws IOException {
		String instruction = "java -jar PQL.TOOL-1.3.jar --index -id=" + id + ".pnml";
		Command command = new Command(instruction, dir);
		command.execute();
	}
	
	public QueryCommand Query(String script) throws IOException {
		String instruction = "java -jar PQL.TOOL-1.3.jar --query -pql=" + script;
		QueryCommand command = new QueryCommand(instruction, dir);
		command.execute();
		return command;
	}
}
