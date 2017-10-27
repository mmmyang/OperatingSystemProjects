package MyCommand;

import java.util.LinkedHashMap;
import java.util.Map;
import org.w3c.dom.Element;
import MyProcessor.ProcessException;

public class FileCommand extends Command {
    private String path;
    private static Map<String, String> files;    // Map used to map each file and its file path
    
    
    public static Map<String, String> getFiles() {
        return files;
    }
    
    public String describe() {
        return "file Command on File: " + path;
    }
    
    public void execute(String workingDir) {
        System.out.println(describe());
        if (files == null) {
            files = new LinkedHashMap<String, String>();
        }
        files.put(id, path);    // add the file to the file map
    }
    
    public void parse(Element element) throws ProcessException {
        id = element.getAttribute("id");
        path = element.getAttribute("path");
        
        validCmdField(id, "Missing id in file command");    // a file command must contain an id
        validCmdField(path, "Missing path in file command");    // a file command must contain a path
    }
}