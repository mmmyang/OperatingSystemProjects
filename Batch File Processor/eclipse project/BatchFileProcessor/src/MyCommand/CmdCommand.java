package MyCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.w3c.dom.Element;
import MyProcessor.ProcessException;

public class CmdCommand extends Command {
    private String path;
    private String args;
    private String in;
    private String out;
    
    public String describe() {
        return "Command Executing: " + id;
    }
    
    public void execute(String workingDir) throws ProcessException {
        System.out.println(describe());
        
        List<String> command = new ArrayList<String>();
        command.add(path);
        
        if (!(args == null || args.isEmpty())) {
            StringTokenizer st = new StringTokenizer(args);
            while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                command.add(tok);
            }
        }
        
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(new File(workingDir));    // set working directory of this command

        // map input and output files
        Map<String, String> filemap = FileCommand.getFiles();
        boolean inMaped = false, outMaped = false;
        for (String filename : filemap.keySet()) {
            if (filename.equalsIgnoreCase(in)) {
                in = filemap.get(filename);
                inMaped = true;
            }
            if (filename.equalsIgnoreCase(out)) {
                out = filemap.get(filename);
                outMaped = true;
            }
        }
        
        // check if the file mapping is successful
        if (!in.isEmpty() && !inMaped) {
            throw new ProcessException("Error Processing Batch. Unable to locate IN FileCommand with id: " + in);
        }
        if (!out.isEmpty() && !outMaped) {
            throw new ProcessException("Error Processing Batch. Unable to locate OUT FileCommand with id: " + out);
        }
        
        // redirect the in, output and error output file
        if (!(in == null || in.isEmpty())) {
            builder.redirectInput(new File(workingDir, in));    // redirect input
        }
        builder.redirectError(new File(workingDir, "error.txt"));    // redirect error message output
        builder.redirectOutput(new File(workingDir, out));    // redirect output
        
        try {
            Process process = builder.start();
            process.waitFor();
            System.out.println("Command Executed: " + id);
        } catch (Exception e) {    // IOException | InterruptedException
            throw new ProcessException("Error executing cmd command: " + id + " " + e.getMessage());
        }
    }
    
    public void parse(Element element) throws ProcessException {
        id = element.getAttribute("id");
        path = element.getAttribute("path");
        args = element.getAttribute("args");
        in = element.getAttribute("in");
        out = element.getAttribute("out");
        
        validCmdField(id, "Missing id in cmd command");    // a cmd command must contain an id
        validCmdField(path, "Missing path in cmd command");    // a cmd command must contain a path
    }
}
