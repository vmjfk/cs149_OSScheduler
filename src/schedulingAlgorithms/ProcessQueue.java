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
     * Generates an array of processes
     * 
     * @return tasks (Task Array) : Randomly generated processes.
     */
    public Task[] generateProcesses(int seed) {
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
        return sortByArrivalTime(tasks);
    }
    
    /**
     * Sorts an Array of tasks by their arrival time.
     * 
     * @param tasks (Task Array) : An Array of tasks.
     * @return tasks (Task Array) : A sorted Array of tasks.
     */
    public Task[] sortByArrivalTime(Task[] tasks) 
    {
        //Comparator for sorting the array by arrival time
        Comparator<Task> c = new Comparator<Task>() {
            public int compare(Task t1, Task t2) {
                return t1.compareArrivalTime(t2.getArrivalTime());
            }
        };
        Arrays.sort(tasks, c);
        return tasks;
    }
    
   /**
    * Sorts an Array of tasks by their burst time.
    * 
    * @param tasks (Task Array) : An Array of tasks.
    * @return tasks (Task Array) : A sorted Array of tasks.
    */
   public Task[] sortByBurstTime(Task[] tasks) 
   {
       //Comparator for sorting the array by burst time
       Comparator<Task> c = new Comparator<Task>() {
           public int compare(Task t1, Task t2) {
               return t1.compareRunTime(t2.getRunTime());
           }
       };
       Arrays.sort(tasks, c);
       return tasks;
   }
    
    
}
