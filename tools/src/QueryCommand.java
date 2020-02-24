import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QueryCommand extends Command{
	
	public ArrayList<String> result = null;
	long time = 0;

	public QueryCommand(String instruction, String path) {
		super(instruction, path);
		// TODO Auto-generated constructor stub
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
	    	// this should only work for query
	        // System.out.println(s);
	        if (s.length() > 6 && s.substring(0,6).equals("Result")) {
	        	this.result = processString(s);
	        }
	        
	        if (s.length() > 4 && s.substring(0,4).equals("time")) {
	        	this.time = Long.parseLong(s.split(":")[1]);
	        }
	    }
	}
	
	// process string
	public ArrayList<String> processString(String s) {
		ArrayList<String> models = new ArrayList<String>();
    	s = " " + s.substring(10);
    	int boundary = s.length()-5;
    	for (int i = 0; i < boundary; i++) {
    		if (s.substring(i, i+5).equals(".pnml")) {
    			int pre = i - 1;
    			String model = "";
    			while(pre >= 0 && !s.substring(pre, pre+1).equals(" ")) {
    				model = s.substring(pre, pre+1) + model;
    				pre = pre - 1;
    			}
    			models.add(model);
    		}
    	}
        return models;
	}
	
	public ArrayList<String> getResult(){
		return this.result;
	}
	
	public long getTime(){
		return this.time;
	}

}
