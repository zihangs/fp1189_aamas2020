import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Command {
	
	String instruction;
	String path;
	
	public Command(String instruction, String path) {
		this.instruction = instruction;
		this.path = path;
	}
	
	public void execute() throws IOException {
		List<String> command = new ArrayList<String>();
	    command.add("cmd.exe");
	    command.add("/c");
	    command.add(instruction);
	      
	    // creating the process
	    ProcessBuilder pb = new ProcessBuilder(command);
	      
	    // start the process
	    pb.directory(new File(path));
	    Process process = pb.start();
	    
	    // for reading the output from stream
	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String s = null;
	    while ((s = stdInput.readLine()) != null)
	    { 
	        System.out.println(s);
	    }
	}
}
