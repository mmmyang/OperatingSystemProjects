package MyProcessor;

import java.io.File;
import MyCommand.Command;

/**
 * Batch file processor used to parse and execute a batch file containing a number of commands
 * Execute the processor as an executable jar file from command line using command such as "java -jar myBatchProcessor.jar work\batch1.dos.xml" 
 *
 */
public class BatchProcessor {
    
    public static void main(String[] args) throws ProcessException {
        try {
            String batchfile = null;
            if (args.length > 0) {
                batchfile = args[0];
            } else {
                batchfile = "work/batch1.dos.xml";
            }
            System.out.println("Opening batch file: " + batchfile);
            File bf = new File(batchfile);
            
            Batch batch = BatchParser.buildBatch(bf);
            executeBatch(batch);
            
            System.out.println("Finished Batch");
        } catch (ProcessException e) {
            System.out.println("Exception during batch processing: " + e.getMessage());
            System.out.println("Process terminated with exception!");
        }
    }
    
    /**
     * execute & describe commands
     * @param batch
     * @throws ProcessException
     */
    public static void executeBatch(Batch batch) throws ProcessException {
        for (Command cmd : batch.getCommands().values()) {
            cmd.execute(batch.getWorkingDir());
        }
    }
}
