package MyCommand;

import org.w3c.dom.Element;

import MyProcessor.ProcessException;

public abstract class Command {
    protected String id;
    
    public abstract String describe();
    
    public abstract void execute(String workingDir) throws ProcessException;
    
    public abstract void parse(Element element) throws ProcessException;
    
    public String getId()  {
        return id;
    }
    
    protected void validCmdField(String field, String description) throws ProcessException {
        if (field == null || field.isEmpty()) {
            throw new ProcessException(description);
        }
    }
}
