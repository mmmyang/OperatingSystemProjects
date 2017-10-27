package MyCommand;

import java.util.Map;
import org.w3c.dom.Element;
import MyProcessor.ProcessException;

public class PipeCmdCommand extends Command {
    private String path;
    private String args;
    private String in;
    private String out;
    
    public String getPath() {
        return path;
    }
    
    public String getArgs() {
        return args;
    }
    
    public String getIn() {
        return in;
    }
    
    public String getOut() {
        return out;
    }
    
    public String describe() {
        return "Command : " + id;    ///
    }
    
    public void execute(String workingDir) throws ProcessException {
        System.out.println(describe());
        System.out.println(id + " Deferring Execution");    ///
        
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
    }
    
    public void parse(Element element) throws ProcessException {
        id = element.getAttribute("id");
        path = element.getAttribute("path");
        args = element.getAttribute("args");
        in = element.getAttribute("in");
        out = element.getAttribute("out");
        
        validCmdField(id, "Missing id in pipe cmd command");    // a pipe cmd command must contain an id
        validCmdField(path, "Missing path in pipe cmd command");    // a pipe cmd command must contain a path
    }
}