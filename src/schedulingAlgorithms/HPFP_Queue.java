package schedulingAlgorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by danieltam on 6/27/16.
 */
public class HPFP_Queue extends ArrayList{

    public HPFP_Queue(int priorityQueueCount){
        while (priorityQueueCount > 0) {
            PriorityQueue<Task> priorityQueue = new PriorityQueue<>(10, new Comparator<Task>()
            {
                public int compare(Task t1, Task t2)
                {
                    return t1.compareArrivalTime(t2.getArrivalTime());
                }
            });
            this.add(priorityQueue);
            priorityQueueCount -= 1;
        }
    }

    public boolean isEmpty(ArrayList<PriorityQueue<Task>> readyQueue)
    {
        for (PriorityQueue<Task> priorityQueue : readyQueue)
        {
            if (priorityQueue.isEmpty())
            {
                return true;
            }
        }
        return false;
    }

    public void addTask(ArrayList<PriorityQueue<Task>> priorityQueue, Task t){
        priorityQueue.get(t.getPriority() - 1).add(t);
    }

    public void poll(ArrayList<PriorityQueue<Task>> readyQueue){
        for (PriorityQueue<Task> priorityQueue : readyQueue){
            if(!priorityQueue.isEmpty()){
                priorityQueue.poll();
            }
        }
    }

}
