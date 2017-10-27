package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class DirectoryCommand extends ServerCommand {
    private static Logger logger = Logger.getLogger(DirectoryCommand.class);
    public void run() throws IOException, ServerException {        
        // Read file names
        List<String> fileNames = FileUtil.directory();
        String size = String.valueOf(fileNames.size());

        // Write response
        this.sendOK();
        StreamUtil.writeLine(size, outputStream);
        for (String fileName: fileNames) {
            StreamUtil.writeLine(fileName, outputStream);
        }
        logger.debug("Finished writing response");                
    }
}
