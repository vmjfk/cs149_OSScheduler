package schedulingAlgorithms;

import com.apple.concurrent.Dispatch;
//import HPFP_Queue;
import java.util.*;

/**
 * RoundRobin
 * This is not working correctly yet, feel free to modify it however you want
 * in order to get it working correctly
 * @author lord_tyler
 *
 */
public class HighestPriorityFirst_preemptive
{
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
    public HighestPriorityFirst_preemptive(ProcessQueue processQueue)
    {   
        this.processQueue = processQueue;
        this.finalTasksDone = 0;
        this.finalTime = 0.0f;
        this.finalTurnaroundTime = 0.0f;
        this.finalWaitTime = 0.0f;
        this.finalResponseTime = 0.0f;
    }
    
    /**
     * Runs a preemptive RoundRobin algorithm for
     *     process simulation.
     */
    public void runPreemptive()
    {
        for (int i = 1; i <= 5; i++) 
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
            int priorityQueueCount = 4;

            ArrayList<Task> scheduledTasks = new ArrayList<>();
            ArrayList<Task> completedTasks = new ArrayList<>();
            HPFP_Queue readyQueue = new HPFP_Queue(priorityQueueCount);
            Map<String, Float> remainingRunTimes = new HashMap<>();
            
            // For each of 5 runs create a new process queue
            Task[] tasks = processQueue.generateProcesses(i);
            // Sort task list by arrival time initially
            processQueue.sortByArrivalTime(tasks);
            // Place task list into a queue for processing with RR
            Queue<Task> taskList = new LinkedList<Task>(Arrays.asList(tasks));


            while(!taskList.isEmpty() || !readyQueue.isEmpty(readyQueue))
            {
                //Add processes that have arrived to the ready queue
                while(!taskList.isEmpty() && taskList.peek().getArrivalTime() <= clock)
                {
                    Task t = taskList.poll();
                    readyQueue.addTask(readyQueue, t);
                }
                //Variables for statistics for this round only
                // changed sliceStartTime to startTime
                float turnaroundTime = 0.0f;
                float waitTime = 0.0f;
                float responseTime = 0.0f;
                float remainingTime = 0.0f;

                Task t;

                if(!readyQueue.isEmpty(readyQueue))
                {
                    t = readyQueue.poll(readyQueue);
                    // t = readyQueuePoll();
                    if (t.getStartTime() == 0)
                    {
                        t.setStartTime(Math.max((int) Math.ceil(t.getArrivalTime()), clock));
                    }

                    //Update if this is the first time seeing this process
                    if (!remainingRunTimes.containsKey(t.getName()))
                    {
                        if(t.getStartTime() > 99) break;
                        responseTime = t.getStartTime() - t.getArrivalTime();
                        remainingTime = t.getRunTime() - 1;
                        
                        //If process finishes in this time slice
                        if(remainingTime <= 0)
                        {
                            // changed to t.getStartTime()
                            completionTime = t.getStartTime() + t.getRunTime();
                            t.setCompletionTime(completionTime);
                            tasksDone++;
                            completedTasks.add(t);
                            turnaroundTime = completionTime - t.getArrivalTime();
                            //Add wait time for all processes that have started but did not run in this slice
                            waitTime = remainingRunTimes.size() * t.getRunTime();
                        }
                        //Process did not finish yet but ran in this time slice
                        else 
                        {
                            //Add 1 quanta for all processes that have started but did not run in this slice
                            waitTime = remainingRunTimes.size();
                            //Add this process to remainingRunTimes and update remaining time
                            remainingRunTimes.put(t.getName(), remainingTime);
                            //Put back into queue at end of line
                            readyQueue.addTask(readyQueue, t);
                        }

                    }
                    //Update if the process has previously been started
                    else 
                    {
                        remainingTime = remainingRunTimes.get(t.getName()) - 1;
                        //If process finishes in this time slice
                        if(remainingTime <= 0)
                        {
                            completionTime = t.getStartTime() + remainingRunTimes.get(t.getName());
                            t.setCompletionTime(completionTime);
                            tasksDone++;
                            completedTasks.add(t);
                            turnaroundTime = completionTime - t.getArrivalTime();
                            remainingRunTimes.remove(t.getName());
                            //Add wait time for all processes that have started but did not run in this slice
                            waitTime = remainingRunTimes.size() * (completionTime - t.getStartTime());
                        }
                        //Process did not finish yet but ran in this time slice
                        else
                        {
                            //Add 1 quanta for all processes that have started but did not run in this slice
                            waitTime = remainingRunTimes.size() -1; //Subtract 1 for the running process
                            //Update remaining time
                            remainingRunTimes.put(t.getName(), remainingTime);
                            //Put back into queue at end of line
                            readyQueue.addTask(readyQueue, t);
                        }
                    }
                }
                //There were no processes available to run in this slice
                else
                {
                    clock++;
                    if(completionTime >= 99)
                    {
                        totalTime = completionTime; //time until last process is complete
                    }
                    else
                    {
                        totalTime = 99;
                    }
                    continue;
                }
                
                //Update the running schedule
                Task scheduled = (Task) t.clone();
                scheduled.setRunTime(1);
                scheduledTasks.add(scheduled);
                
                //Update totals at end of each run
                totalTurnaroundTime = totalTurnaroundTime + turnaroundTime;
                totalWaitTime = totalWaitTime + waitTime;
                totalResponseTime = totalResponseTime + responseTime;
                totalTasksDone = tasksDone;
                clock = t.getStartTime() + 1;
                if(completionTime >= 99)
                {
                    totalTime = completionTime; //time until last process is complete
                }
                else
                {
                    totalTime = 99;
                }
            }
            
            // Update final numbers needed for averages
            finalTurnaroundTime += totalTurnaroundTime;
            finalWaitTime += totalWaitTime;
            finalResponseTime += totalResponseTime;
            finalTime += totalTime;
            finalTasksDone += totalTasksDone;

            printCompletedTasks(completedTasks, i);
            printTimeChart(scheduledTasks, i);
        }
        printFinalBenchmark();
    }
    /**
     * Prints out the stats for all completed tasks
     */
    public void printCompletedTasks(ArrayList<Task> scheduledTasks, int run)
    {
        System.out.println("\n########################################################################################");
        System.out.println("############ The following processes were completed for RR run " + run + " #####################");
        System.out.println("########################################################################################");
        while(!scheduledTasks.isEmpty()) 
        {
            Task t = scheduledTasks.remove(0);
            System.out.println(t);
        }
    }
    

    public void printTimeChart(ArrayList<Task> tasksChart, int run)
    {
        System.out.println("\n############################################################");
        System.out.println("############ HPF Preemptive Time Chart for run " + run + " #####################");
        System.out.println("############################################################");
        new GanttChart(tasksChart);
    }
    
    /**
     * Prints out all calculated averages and throughput for
     *     a completed FirstComeFirstServe simulation.
     */
    public void printFinalBenchmark()
    {
        System.out.println("\n#######################################################################################");
        System.out.println("############ Final calculated averages and calculated throughput for HPF Preemptive #############");
        System.out.println("#######################################################################################");
        System.out.println("Average Turnaround Time = " + finalTurnaroundTime/finalTasksDone);
        System.out.println("Average Wait Time = " + finalWaitTime/finalTasksDone);
        System.out.println("Average Response Time = " + finalResponseTime/finalTasksDone);
        System.out.println("Throughput = " + finalTasksDone/finalTime);
        System.out.println();
    }
}
