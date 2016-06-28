package schedulingAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * A pre-emptive variation of Shortest Job First which
 * suspends the actively running process if another process
 * is found to have a shorter duration.
 * 
 */
public class ShortestRemainingTime {
	
	private ProcessQueue processQueue;
	private int finalTasksDone;
	private float finalTime;
	private float finalTurnaroundTime;
	private float finalWaitTime;
	private float finalResponseTime;
	
	/**
	 * Constructor method.
	 * 
	 * @param processQueue (ProcessQueue) : A specialized Queue used for
	 *     generating and sorting organized simulated processes.
	 */
	public ShortestRemainingTime(ProcessQueue processQueue) 
    {
        this.processQueue = processQueue;
        this.finalTasksDone = 0;
        this.finalTime = 0.0f;
        this.finalTurnaroundTime = 0.0f;
        this.finalWaitTime = 0.0f;
        this.finalResponseTime = 0.0f;
    }
        
	/**
	 * Runs a non-preemptive ShortestRemainingTime algorithm for
	 *     process simulation.
	 */
    public void runPreemptive()
    {
        for(int i = 1; i <= 5; i++) 
        {
            // Variables needed for tracking progress of each run
            int clock = 0;
            int tasksDone = 0;
            int totalTasksDone = 0;
            float completionTime = 0;
            float totalTime = 0.0f;
            float totalTurnaroundTime = 0.0f;
            float totalWaitTime = 0.0f;
            float totalResponseTime = 0.0f;
            ArrayList<Task> scheduledTasks = new ArrayList<>();

            // For each of 5 runs create a new process queue
            Task[] tasks = processQueue.generateProcesses(i);
            
            // Sort task list by arrival time initially
            //processQueue.sortByArrivalTime(tasks);
            
            // Place task list into a queue for processing with SJF
            Queue<Task> taskList = new LinkedList<Task>(Arrays.asList(tasks));
            
            // Queue for ready processes ordered by run time with ties broken by arrival time
            PriorityQueue<Task> readyQueue = new PriorityQueue<>(10, new Comparator<Task>()
            {
                public int compare(Task t1, Task t2)
                {
                	int difference = t1.compareRunTime(t2.getRunTime()); 
                    if (difference == 0)
                    {
                    	return (t1.compareArrivalTime(t2.getArrivalTime()));
                    }
                    return difference;
                }
            });

            while(!taskList.isEmpty() || !readyQueue.isEmpty()) 
            {
                // Schedule the next process
                Task t;
                if (readyQueue.isEmpty()) 
                {
                    t = taskList.poll();
                } 
                else 
                {
                    t = readyQueue.poll();
                }

                //Update start and completion times for this process
                int startTime = Math.max((int) Math.ceil(t.getArrivalTime()), clock);
                if (startTime > 99) break;
                t.setStartTime(startTime);
                completionTime = startTime + t.getRunTime();
                t.setCompletionTime(completionTime);

                // Update completed tasks and clock
                scheduledTasks.add(t);
                tasksDone++;
                clock = (int) Math.ceil(completionTime);

                // Add processes to the ready queue that have arrived by this time
                while (taskList.peek() != null && taskList.peek().getArrivalTime() <= clock) 
                {
                	Task incomingTask = taskList.poll();
                	
                	// If the current process does not have the shortest runtime,
                	// place the current process back into the readyQueue and 
                	// let the incoming process take over. We then need to update all of
                	// our variables to represent the newly scheduled process.
                	if (t.getRunTime() > incomingTask.getRunTime())
                	{
                		readyQueue.add(t);
                		t = incomingTask;
                        
                		//Update start and completion times for this process
                        startTime = Math.max((int) Math.ceil(t.getArrivalTime()), clock);
                        if (startTime > 99) break;
                        t.setStartTime(startTime);
                        completionTime = startTime + t.getRunTime();
                        t.setCompletionTime(completionTime);

                        // Update completed tasks and clock
                        scheduledTasks.add(t);
                        tasksDone++;
                        clock = (int) Math.ceil(completionTime);
                	}
                	else
                	{
                		readyQueue.add(incomingTask);
                	}
                }

                // Variables for statistics for this process only
                float turnaroundTime = (completionTime - t.getArrivalTime());
                float waitTime = (turnaroundTime - t.getRunTime());
                int responseTime = (startTime - t.getArrivalTime());

                // Update totals at end of each run
                totalTurnaroundTime += turnaroundTime;
                totalWaitTime += waitTime;
                totalResponseTime += responseTime;
                totalTasksDone = tasksDone;
                
                if (completionTime >= 99) 
                {
                    totalTime = completionTime; //time until last process is complete
                } 
                else 
                {
                    totalTime = 99;
                }
            }
            
            // Update final numbers needed for averages at each of 5 runs
            finalTurnaroundTime += totalTurnaroundTime;
            finalWaitTime += totalWaitTime;
            finalResponseTime += totalResponseTime;
            finalTime += totalTime;
            finalTasksDone += totalTasksDone;

            // Make a copy of the completed tasks to use in the time chart
            ArrayList<Task> tasksChart = new ArrayList<Task>(scheduledTasks);
            printCompletedTasks(scheduledTasks, i);
            printTimeChart(tasksChart, i);
        }
        
        printFinalBenchmark();
    }
    
	/**
	 * Prints time chart of completed run
	 */
    private void printCompletedTasks(ArrayList<Task> scheduledTasks, int run)
    {
        System.out.println("\n#######################################################################################");
        System.out.println("############ The following processes were completed for SRT run " + run + " #####################");
        System.out.println("#######################################################################################");
        while(!scheduledTasks.isEmpty()) {
            Task t = scheduledTasks.remove(0);
            System.out.println(t);
        }	
    }
    
	/**
	 * Prints out all calculated averages and throughput for
	 *     a completed FirstComeFirstServe simulation.
	 */
    private void printTimeChart(ArrayList<Task> tasksChart, int run)
    {
        System.out.println("\n###########################################################");
        System.out.println("############ SRT Time Chart for run " + run + " #####################");
        System.out.println("###########################################################");
        new GanttChart(tasksChart);
    }
    
	/**
	 * Prints out all calculated averages and throughput for
	 *     a completed ShortestJobFirst simulation.
	 */
    private void printFinalBenchmark()
    {
        System.out.println("\n######################################################################################");
        System.out.println("############ Final calculated averages and calculated throughput for SRT #############");
        System.out.println("######################################################################################");
        System.out.println("Average Turnaround Time = " + finalTurnaroundTime/finalTasksDone);
        System.out.println("Average Wait Time = " + finalWaitTime/finalTasksDone);
        System.out.println("Average Response Time = " + finalResponseTime/finalTasksDone);
        System.out.println("Throughput = " + finalTasksDone/finalTime);
        System.out.println();
    }
}