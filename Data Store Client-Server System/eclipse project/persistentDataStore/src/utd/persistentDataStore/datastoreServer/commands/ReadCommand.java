package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import org.apache.log4j.Logger;
import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class ReadCommand extends ServerCommand {
    private static Logger logger = Logger.getLogger(ReadCommand.class);
    public void run() throws IOException, ServerException {
        try {
            // Read message
            String name = StreamUtil.readLine(inputStream);
            logger.debug("inMessage: " + name);
            
            // Read data
            byte[] data = FileUtil.readData(name);
            String size = String.valueOf(data.length);

            // Write response
            this.sendOK();
            StreamUtil.writeLine(size, outputStream);
            StreamUtil.writeData(data, outputStream);
            logger.debug("Finished writing response");    
        } catch (Exception e) {
            this.sendError("Error ocurred with read command: " + e.getMessage());
            logger.debug("Error ocurred with read command: " + e.getMessage());
            //throw new ServerException("Error ocurred with read command: " + e.getMessage());
            
        }
            
    }
}
