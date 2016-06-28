package schedulingAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class HighestPriorityFirst 
{
    private ProcessQueue processQueue;
    private int finalTasksDone;
    private float finalTime;
    private float finalTurnaroundTime;
    private float finalWaitTime;
    private float finalResponseTime;
    
    public HighestPriorityFirst(ProcessQueue processQueue)
    {
        this.processQueue = processQueue;
        this.finalTasksDone = 0;
        this.finalTime = 0.0f;
        this.finalTurnaroundTime = 0.0f;
        this.finalWaitTime = 0.0f;
        this.finalResponseTime = 0.0f;
    }


    /**
     * Highest Priority First (non-preemptive)
     */
    public void runNonPreemptive() {
        for(int i = 1; i <= 5; i++) 
        {
            // Variables needed for tracking progress of each run
            int clock = 0;
            int tasksDone = 0;
            int totalTasksDone = 0;
            float completionTime = 0.0f;
            float totalTime = 0.0f;
            float totalTurnaroundTime = 0.0f;
            float totalWaitTime = 0.0f;
            float totalResponseTime = 0.0f;
            ArrayList<Task> scheduledTasks = new ArrayList<>();

            // For each of 5 runs create a new process queue
            Task[] tasks = processQueue.generateProcesses(i);
            // Sort task list by arrival time initially
            processQueue.sortByArrivalTime(tasks);
            // Place task list into a queue for processing with HPF-NP
            Queue<Task> taskList = new LinkedList<Task>(Arrays.asList(tasks));
            
            // Queue for ready processes ordered by priority with ties broken by arrival time
            PriorityQueue<Task> readyQueue = new PriorityQueue<>(10, new Comparator<Task>()
            {
                public int compare(Task t1, Task t2)
                {
                    int difference = t1.comparePriority(t2.getPriority());
                    if (difference == 0)
                    {
                        return (t1.compareArrivalTime(t2.getArrivalTime()));
                    }
                    return difference;
                }
            });

            while (!taskList.isEmpty() || !readyQueue.isEmpty()) 
            {
                // Get the correct process to be scheduled
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
                int startTime = Math.max((int)Math.ceil(t.getArrivalTime()), clock);
                if (startTime > 99) break;
                t.setStartTime(startTime);
                completionTime = startTime + t.getRunTime();
                t.setCompletionTime(completionTime);

                // Update completed tasks and clock
                scheduledTasks.add(t);
                tasksDone++;
                clock = (int)Math.ceil(completionTime);

                // Add processes to the ready queue that have arrived by this time
                while (taskList.peek() != null && taskList.peek().getArrivalTime() <= clock) 
                {
                    readyQueue.add(taskList.poll());
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
    public void printCompletedTasks(ArrayList<Task> scheduledTasks, int run)
    {
        System.out.println("\n#######################################################################################");
        System.out.println("############ The following processes were completed for HPF-NP run " + run + " #####################");
        System.out.println("#######################################################################################");
        while(!scheduledTasks.isEmpty()) {
            Task t = scheduledTasks.remove(0);
            System.out.println(t);
        }   
    }
    
    /**
     * Prints out all calculated averages and throughput for
     *     a completed HighestPriorityFirst-NP simulation.
     */
    public void printTimeChart(ArrayList<Task> tasksChart, int run)
    {
        System.out.println("\n###########################################################");
        System.out.println("############ HPF-NP Time Chart for run " + run + " #####################");
        System.out.println("###########################################################");
        new GanttChart(tasksChart);
    }
    
    /**
     * Prints out all calculated averages and throughput for
     *     a completed HighestPriorityFirst-NP simulation.
     */
    public void printFinalBenchmark()
    {
        System.out.println("\n######################################################################################");
        System.out.println("############ Final calculated averages and calculated throughput for HPF-NP #############");
        System.out.println("######################################################################################");
        System.out.println("Average Turnaround Time = " + finalTurnaroundTime/finalTasksDone);
        System.out.println("Average Wait Time = " + finalWaitTime/finalTasksDone);
        System.out.println("Average Response Time = " + finalResponseTime/finalTasksDone);
        System.out.println("Throughput = " + finalTasksDone/finalTime);
        System.out.println();
    }

    /**
     * Higest Priority First (preemptive)
     */
    public void runPreemptive() {
        //TODO
    }
}
