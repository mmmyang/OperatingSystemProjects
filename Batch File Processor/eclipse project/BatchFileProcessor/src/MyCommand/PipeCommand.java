package MyCommand;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import MyProcessor.ProcessException;

public class PipeCommand extends Command {
    private List<PipeCmdCommand> pipeCommand; 
    
    public PipeCommand() {
        pipeCommand = new ArrayList<PipeCmdCommand>();
    }
    
    public String describe() {
        return "Pipe Command";
    }
    
    public void execute(String workingDir) throws ProcessException {
        System.out.println(describe());
        
        try {
            // execute pipe command 1
            PipeCmdCommand pipeCmd1 = pipeCommand.get(0);
            pipeCmd1.execute(workingDir);    // map the input and output file        
            System.out.println("Waiting for " + pipeCmd1.getId() + " to exit");
            
            // build command
            List<String> command = new ArrayList<String>();
            command.add(pipeCmd1.getPath());        
            String pipeCmdArgs = pipeCmd1.getArgs();
            if (!(pipeCmdArgs == null || pipeCmdArgs.isEmpty())) {
                StringTokenizer st = new StringTokenizer(pipeCmdArgs);
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
                    command.add(tok);
                }
            }
            
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(new File(workingDir));    // set working directory of this command    
            
            builder.redirectInput(new File(workingDir, pipeCmd1.getIn()));
            final Process process1 = builder.start();
            
            File temp = File.createTempFile("temp", ".txt");        
            InputStream is = process1.getInputStream();
            OutputStream os = new FileOutputStream(temp, false);
            byte[] streamBuffer = new byte[1024];
            int bytes;
            while ((bytes = is.read(streamBuffer)) != -1) {
                os.write(streamBuffer, 0, bytes);
            }
            os.flush();
            is.close();
            os.close();
            
            System.out.println(pipeCmd1.getId() + " has exited");
            
            
            // execute pipe command 2
            PipeCmdCommand pipeCmd2 = pipeCommand.get(1);
            pipeCmd2.execute(workingDir);    // map the input and output file
            System.out.println("Waiting for " + pipeCmd2.getId() + " to exit");
            
            // build command
            command.clear();
            command.add(pipeCmd1.getPath());
            pipeCmdArgs = pipeCmd2.getArgs();
            if (!(pipeCmdArgs == null || pipeCmdArgs.isEmpty())) {
                StringTokenizer st = new StringTokenizer(pipeCmdArgs);
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
                    command.add(tok);
                }
            }
            
            builder = new ProcessBuilder(command);
            builder.directory(new File(workingDir));    // set working directory of this command    
            
            builder.redirectInput(temp);
            builder.redirectOutput(new File(workingDir, pipeCmd2.getOut()));
            builder.start();
            System.out.println(pipeCmd1.getId() + " has exited");
        } catch (Exception e) {
            throw new ProcessException(e.getMessage());
        }
    }
    
    public void parse(Element element) throws ProcessException {
        NodeList pipecmdnodes = element.getChildNodes();    // each pipe command contains two pipe cmd commands
        for (int idx = 0; idx < pipecmdnodes.getLength(); idx++) {
            Node pipecmdnode = pipecmdnodes.item(idx);
            if (pipecmdnode.getNodeType() == Node.ELEMENT_NODE) {    // if this node is an element
                Element elem = (Element) pipecmdnode;
                PipeCmdCommand cmd = buildPipeCommand(elem);
                pipeCommand.add(cmd);    // add the built pipe cmd command to the pipeCommand list
            }
        }
    }
    
    private PipeCmdCommand buildPipeCommand(Element elem) throws ProcessException {
        PipeCmdCommand newPipeCmd = null;
        
        String cmdName = elem.getNodeName();
        if (cmdName == null) {
            throw new ProcessException("Unable to parse command from: " + elem.getTextContent());
        } else if ("cmd".equalsIgnoreCase(cmdName)) {    // parse pipe cmd command
            System.out.println("Parsing pipe cmd");
            newPipeCmd = new PipeCmdCommand();
            newPipeCmd.parse(elem);
        } else {
            throw new ProcessException("Unknown command: " + cmdName + " from: " + elem.getBaseURI());
        }
        return newPipeCmd;
    }
}