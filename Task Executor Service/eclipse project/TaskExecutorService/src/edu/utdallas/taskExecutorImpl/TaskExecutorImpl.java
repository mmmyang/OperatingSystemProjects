package edu.utdallas.taskExecutorImpl;

import java.util.ArrayList;
import java.util.List;
import edu.utdallas.taskExecutor.Task;
import edu.utdallas.taskExecutor.TaskExecutor;

public class TaskExecutorImpl implements TaskExecutor {
    private List<TaskRunner> runnerPool = new ArrayList<>();
    
    /**
     * for every poolSize runners(), create TaskRunner, and run the thread.
     * @param poolSize
     */
    public TaskExecutorImpl(int poolSize) {
        for(int i = 0; i < poolSize; i++) {
            TaskRunner newRunner = new TaskRunner();

            runnerPool.add(newRunner);
            Thread newThread = new Thread(newRunner);
            newThread.start();
            Thread.yield();
        }
    }

    /**
     * add tasks to Blocking FIFO queue.
     */
    public void addTask(Task task) {
        TaskRunner.getTaskQueue().add(task);
    }

}
