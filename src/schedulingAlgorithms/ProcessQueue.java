package schedulingAlgorithms;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ProcessQueue {
	
	private int quantaMax;
	private int runtimeMax;
	private int priorityMax;
	private int numberOfTasks;
	
	public ProcessQueue(int quantaMax, int runtimeMax, int priorityMax, int numberOfTasks)
	{
		this.quantaMax = quantaMax;
		this.runtimeMax = runtimeMax;
		this.priorityMax = priorityMax;
		this.numberOfTasks = numberOfTasks;
		
	}
	
    /**
     * generateProcessQueue generates an array of processes and sorts
     * them by arrival time.
     * @return taskList, an array of randomly generated processes
     */
    public Task[] generate(int seed) {
        int arrivalTime;
        float runTime;
        float floatVal;
        int priority;
        Task[] tasks = new Task[numberOfTasks];
        Set<Task> taskSet = new HashSet<Task>();
        Random random = new Random(seed);
        for(int i = 0; i < numberOfTasks; i++) {
            int length = String.valueOf(i).length();
            String name;
            if(length > 1) {
                name = "p" + i;
            } else {
                name = "p0" + i;
            }
            arrivalTime = random.nextInt(quantaMax);
            floatVal = random.nextFloat();
            runTime = random.nextInt(runtimeMax) + floatVal + 1;
            priority = random.nextInt(priorityMax) + 1;
            Task task = new Task(name, arrivalTime, runTime, priority);
            taskSet.add(task);
        }
        taskSet.toArray(tasks);
        //Comparator for sorting the array by arrival time
        Comparator<Task> c = new Comparator<Task>() {
            public int compare(Task t1, Task t2) {
                return t1.getArrivalTime() - t2.getArrivalTime();
            }
        };
        Arrays.sort(tasks, c); // actually sort the array
        return tasks;
    }
}
