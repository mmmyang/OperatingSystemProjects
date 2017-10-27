package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import org.apache.log4j.Logger;
import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class WriteCommand extends ServerCommand {
    private static Logger logger = Logger.getLogger(WriteCommand.class);
    public void run() throws IOException, ServerException {
        try {
            // Read message
            String name = StreamUtil.readLine(inputStream);
            logger.debug("inMessage: " + name);
            String size = StreamUtil.readLine(inputStream);
            logger.debug("inMessage: " + size);
            byte[] data = StreamUtil.readData(Integer.valueOf(size), inputStream);
            logger.debug("inMessage: " + data);
            
            // Write data
            FileUtil.writeData(name, data);

            // Write response
            this.sendOK();
            logger.debug("Finished writing response");
        } catch (Exception e) {
            this.sendError("Error occured with write command: " + e.getMessage());
            logger.debug("Error occured with write command: " + e.getMessage());
            //throw new ServerException("Error occured with write command: " + e.getMessage());
        }
    }
}
