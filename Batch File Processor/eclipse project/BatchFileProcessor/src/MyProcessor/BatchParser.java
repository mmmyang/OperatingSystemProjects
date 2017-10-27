package MyProcessor;

import java.io.File;
import java.io.FileInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import MyCommand.CmdCommand;
import MyCommand.Command;
import MyCommand.FileCommand;
import MyCommand.PipeCommand;
import MyCommand.WDCommand;

public class BatchParser {
    static Batch buildBatch(File batchFile) throws ProcessException {

        if (batchFile == null || !batchFile.exists()) {
            throw new ProcessException("Failed to find batch file: " + batchFile);
        }
        
        Batch batch = new Batch();    // new a Batch to contain the commands and working directory
        Command cmd = null;
        
        try {
            // parse the batch file into Document
            FileInputStream fis = new FileInputStream(batchFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fis);
            
            // for every Xml Element: build Command and add the Command to the command map
            Element pnode = doc.getDocumentElement();
            NodeList nodes = pnode.getChildNodes();
            for (int idx = 0; idx < nodes.getLength(); idx++) {
                Node node = nodes.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {    // if this node is an element
                    Element elem = (Element) node;
                    cmd = buildCommand(elem);
                    batch.addCommand(cmd);
                }
            }
        } catch (Exception e) {
            throw new ProcessException(e.getMessage());
        }
        
        return batch;
    }
    
    private static Command buildCommand(Element elem) throws ProcessException {
        Command newCommand = null;    // new a Command
        
        String cmdName = elem.getNodeName();
        if (cmdName == null) {
            throw new ProcessException("Unable to parse command from: " + elem.getTextContent());
        } else if ("wd".equalsIgnoreCase(cmdName)) {    // parse wd command
            System.out.println("Parsing wd");
            newCommand = new WDCommand();
            newCommand.parse(elem);
        } else if ("file".equalsIgnoreCase(cmdName)) {    // parse file command
            System.out.println("Parsing file");
            newCommand = new FileCommand();
            newCommand.parse(elem);
        } else if ("cmd".equalsIgnoreCase(cmdName)) {    // parse cmd command
            System.out.println("Parsing cmd");
            newCommand = new CmdCommand();
            newCommand.parse(elem);
        } else if ("pipe".equalsIgnoreCase(cmdName)) {    // parse pipe command
            System.out.println("Parsing pipe");
            newCommand = new PipeCommand();
            newCommand.parse(elem);
        } else {
            throw new ProcessException("Unknown command: " + cmdName + " from: " + elem.getBaseURI());
        }
        
        return newCommand;
    }
}
