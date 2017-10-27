package MyProcessor;

import java.util.LinkedHashMap;
import java.util.Map;
import MyCommand.Command;

public class Batch {
    
    private static String workingDir;
    private Map<String, Command> commands;
    
    public Batch() {
        commands = new LinkedHashMap<String, Command>();
        workingDir = null;
    }
    
    public void addCommand(Command command) {
        commands.put(command.getId(), command);    // add command to the Map
    }
    
    public String getWorkingDir() {
        return workingDir;
    }
    
    public static void setWorkingDir(String wDir) {
        workingDir = wDir;
    }
    
    public Map<String, Command> getCommands() {
        return commands;
    }
}
