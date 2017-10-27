package edu.utdallas.blockingFIFO;

import java.util.concurrent.Semaphore;
import edu.utdallas.taskExecutor.Task;

/**
 * blocking FIFO using semaphores
 * The container is an array of Task
 * FIFO implementation size (array length) no more than 100
 *
 */
public class BlockingTaskQueue {
    final private int queueSize = 100;
    private Task[] taskQueue = new Task[queueSize];    // implement by array to keep queue size fixed when the queue is created.    
    private int nextIn = 0, nextOut = 0;    // queue pointer
    private Semaphore notFull = new Semaphore(queueSize);
    private Semaphore notEmpty = new Semaphore(0);
    private Semaphore sema = new Semaphore(1);
    
    /**
     * Place the passed task into the queue.
     * Thread blocked if the queue is full,
     * until a Task is removed from the queue through the remove() method.
     * @param item
     */
    public void add(Task item) {
        try {
            notFull.acquire();    // block if the queue if full
            sema.acquire();    // critical section begins
            taskQueue[nextIn] = item;
            nextIn = (nextIn +1) % queueSize;
            notEmpty.release();    // resume one remove task thread if any
            sema.release();    // critical section ends        
        } catch (InterruptedException e) {    
            // ignore
        }    
    }
    
    /**
     * Remove and return a task from the queue. 
     * Thread blocked if the queue is empty,
     * until a Task has been placed in the queue though the add() method. 
     * @return
     */
    public Task remove() {
        Task nextTask = null;
        try {
            notEmpty.acquire();    // block if the queue is empty
            sema.acquire();    // critical section begins
            nextTask = taskQueue[nextOut];
            nextOut = (nextOut+1) % queueSize;
            notFull.release();    // resume a add() task if any
            sema.release();    // critical section ends    
        } catch (InterruptedException e) {    
            // ignore
        }    
        return nextTask;
    }
}