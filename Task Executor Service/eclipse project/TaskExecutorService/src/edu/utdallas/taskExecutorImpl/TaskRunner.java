package edu.utdallas.taskExecutorImpl;

import edu.utdallas.blockingFIFO.BlockingTaskQueue;
import edu.utdallas.taskExecutor.Task;

public class TaskRunner implements Runnable {
    private static BlockingTaskQueue taskQueue = new BlockingTaskQueue();
    
    public void run() {
        while(true) {
            // take() blocks if queue is empty
            try {
                Task newTask = taskQueue.remove();
                newTask.execute();
            }
            catch(Throwable th) {
                // ignore
            }
        }
    }

    public static BlockingTaskQueue getTaskQueue() {
        return taskQueue;
    }
}
