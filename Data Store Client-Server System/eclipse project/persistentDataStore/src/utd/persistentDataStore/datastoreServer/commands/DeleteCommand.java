package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import org.apache.log4j.Logger;
import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class DeleteCommand extends ServerCommand {
    private static Logger logger = Logger.getLogger(DeleteCommand.class);
    public void run() throws IOException, ServerException {
        try {
            // Read message
            String name = StreamUtil.readLine(inputStream);
            logger.debug("inMessage: " + name);
            
            if (FileUtil.deleteData(name)) {    // Delete data
                // Write response
                this.sendOK();
            } else {
                this.sendError("failed delete file " + name);
                throw new ServerException("Fail to delete the file");
            }    
            logger.debug("Finished writing response");    
        } catch (Exception e) {    ///!!!!!
            this.sendError("failed delete file " + e.getMessage());
            logger.debug("Finished writing response");
            logger.error(e.getMessage());
            //throw new ServerException("failed delete file " + e.getMessage());
        }            
    }
}
