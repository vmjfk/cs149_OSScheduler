package schedulingAlgorithms;
import java.util.*;
/**
 *COPYRIGHT (C) 2016 CS149_1 Group forkQueue. All Rights Reserved.
 * The process scheduling simulator uses various algorithms to
 * simulate process scheduling
 * Solves CS149 Homework#2
 * @author Tyler Jones, Scot Matson, ______....
 */
public class ProcessSchedulingSimulator {
    //Declare all final variables
    private final static int QUANTA_MAX = 100;
    private final static int RUNTIME_MAX = 10;
    private final static int PRIORITY_MAX = 4;
    private final static int NUM_OF_TASKS = 100;

    /**
     * The main method. The entry point for the application.
     * 
     * @param args (String Array) : User-defined arguments.
     */
    public static void main(String[] args) {
        ProcessSchedulingSimulator pss = new ProcessSchedulingSimulator();
        Scanner input = new Scanner(System.in);
        ProcessQueue processQueue = new ProcessQueue(QUANTA_MAX, RUNTIME_MAX, PRIORITY_MAX, NUM_OF_TASKS);
        int option = 1;
        while(option >= 1 && option <= 6) {
            pss.printMenuOptions();
            option = input.nextInt();
            switch(option) {
                case 1: 
                	FirstComeFirstServe fcfs = new FirstComeFirstServe(processQueue);
                	fcfs.runNonPreemptive();
                	break;
                case 2: 
                	ShortestJobFirst sjf = new ShortestJobFirst(processQueue);
                	sjf.runNonPreemptive();
                	break;
                case 3: 
                	new ShortestRemainingTime();
                	break;
                case 4: 
                	pss.rr(processQueue);
                	break;
                case 5: 
                	new HighestPriorityFirst();
                	break;
                case 6: 
                	new HighestPriorityFirst();
                	break;
                default: 
                	option = 7;
                	break;
            }
        }
        input.close();
    }
    
    /**
     * A helper method which prints out a console-based
     * user-interface.
     */
    private void printMenuOptions()
    {
    	System.out.println(
    			"Please choose the number of the " +
                "process scheduling algorithm you would like to run:\n" +
                "(1) First Come First Served\n" + 
                "(2) Shortest Job First\n" +
                "(3) Shortest Remaining Time\n" +
                "(4) Round Robin\n" +
                "(5) Highest Priority First (Preemptive)\n" +
                "(6) Highest Priority First (non-preemptive)" + "\n(7) Exit"
    	);	
    }

    /**
     * Round Robin (preemptive) NOT FINISHED
     */
    private void rr(ProcessQueue processQueue) {
        //Declare all variables needed for final output 
        int finalTasksDone = 0;
        float finalTime = 0;
        float finalTurnaroundTime = 0;
        float finalWaitTime = 0;
        float finalResponseTime = 0;

        for(int i = 1; i <= 5; i++) {
            //Variables needed for tracking progress of each run
            int clock = 0;
            int tasksDone = 0;
            int totalTasksDone = 0;
            int startTime = 0;
            float completionTime = 0;
            float totalTime = 0;
            float totalTurnaroundTime = 0;
            float totalWaitTime = 0;
            float totalResponseTime = 0;
            ArrayList<Task> completedTasks = new ArrayList<>();
            ArrayList<Task> scheduledTasks = new ArrayList<>();
            Queue<Task> readyQueue = new LinkedList<>();
            Queue<Task> waitingQueue = new LinkedList<>();
            Map<String, Float> remainingRunTimes = new HashMap<>();

            //For each of 5 runs create a new process queue
            Task[] tasks = processQueue.generateProcesses(i);
            Queue<Task> taskList = new LinkedList<Task>(Arrays.asList(tasks));

            while(!taskList.isEmpty() || !readyQueue.isEmpty() || !waitingQueue.isEmpty()) {
                //Add processes that have arrived to the ready queue
                while(!taskList.isEmpty() && taskList.peek().getArrivalTime() <= clock) {
                    readyQueue.add(taskList.poll());
                }
                Task t;
                if(!readyQueue.isEmpty()) {
                    t = readyQueue.poll();
                } else if (!waitingQueue.isEmpty()) {
                    t = readyQueue.poll();
                } else {
                    continue;
                }
                
                startTime = Math.max((int)Math.ceil(t.getArrivalTime()), clock);
                completionTime = startTime + 1;
                t.setCompletionTime(completionTime);
                
                //Variables for statistics for this round only
                float turnaroundTime = 0;
                float waitTime = 0;
                int responseTime = 0;
                
                //Update if this is the first time seeing this process
                if (!remainingRunTimes.containsKey(t.getName())) {
                    if(startTime > 99) break;
                    t.setStartTime(startTime);
                    waitTime = startTime - t.getArrivalTime();
                    responseTime = startTime - t.getArrivalTime();
                    remainingRunTimes.put(t.getName(), t.getRunTime() - 1);
                } else {
                    waitTime = 1;
                }



                //TODO complete round robin (Lost the rest of this when setting up git)



                
                //Update totals at end of each run
                totalTurnaroundTime = totalTurnaroundTime + turnaroundTime;
                totalWaitTime = totalWaitTime + waitTime;
                totalResponseTime = totalResponseTime + responseTime;
                totalTasksDone = tasksDone;
                if(completionTime >= 99) {
                    totalTime = completionTime; //time until last process is complete
                } else {
                    totalTime = 99;
                }
            }
            //Update final numbers needed for averages at each of 5 runs
            finalTurnaroundTime = finalTurnaroundTime + totalTurnaroundTime;
            finalWaitTime = finalWaitTime + totalWaitTime;
            finalResponseTime = finalResponseTime + totalResponseTime;
            finalTime = finalTime + totalTime;
            finalTasksDone = finalTasksDone + totalTasksDone;

            //Print out the stats for all completed tasks for each run
            System.out.println("\n#######################################################################################");
            System.out.println("############ The following processes were completed for SJF run " + i + " #####################");
            System.out.println("#######################################################################################");
            while(!completedTasks.isEmpty()) {
                Task t = completedTasks.remove(0);
                System.out.println(t);
            }
            //print time chart of completed tasks for each run
            System.out.println("\n###########################################################");
            System.out.println("############ SJF Time Chart for run " + i + " #####################");
            System.out.println("###########################################################");
            new GanttChart(scheduledTasks);
        }
        //Print out all calculated averages and Throughput for all 5 runs
        System.out.println("\n######################################################################################");
        System.out.println("############ Final calculated averages and calculated throughput for SJF #############");
        System.out.println("######################################################################################");
        System.out.println("Average Turnaround Time = " + finalTurnaroundTime/finalTasksDone);
        System.out.println("Average Wait Time = " + finalWaitTime/finalTasksDone);
        System.out.println("Average Response Time = " + finalResponseTime/finalTasksDone);
        System.out.println("Throughput = " + finalTasksDone/finalTime);
        System.out.println();
    }
}
