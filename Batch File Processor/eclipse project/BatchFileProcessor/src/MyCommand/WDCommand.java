package MyCommand;

import org.w3c.dom.Element;
import MyProcessor.Batch;
import MyProcessor.ProcessException;

public class WDCommand extends Command {
    private String path;
    
    /**
     * describe this command, will be called to print the trace message when executing this command
     */
    public String describe() {
        return "wd Command: The working directory will be set to " + path;
    }
    
    
    public void execute(String workingDir) {
        System.out.println(describe());
        Batch.setWorkingDir(path);    // static field
    }
    
    /**
     * parse a wd command
     */
    public void parse(Element element) throws ProcessException {
        id = element.getAttribute("id");
        path = element.getAttribute("path");
        
        validCmdField(id, "Missing id in wd command");    // a wd command must contain an id
        validCmdField(path, "Missing path in wd command");    // a wd command must contain a path
    }
}