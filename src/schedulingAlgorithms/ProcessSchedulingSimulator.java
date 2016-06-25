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
                	new FirstComeFirstServe(processQueue);
                	break;
                case 2: 
                	pss.sjf(processQueue);
                	break;
                case 3: 
                	pss.srt();
                	break;
                case 4: 
                	pss.rr(processQueue);
                	break;
                case 5: 
                	pss.hpfP();
                	break;
                case 6: 
                	pss.hpfNP();
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
     * Shortest job first (non-preemptive)
     */
    private void sjf(ProcessQueue processQueue) {
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
            float completionTime = 0;
            float totalTime = 0;
            float totalTurnaroundTime = 0;
            float totalWaitTime = 0;
            float totalResponseTime = 0;
            ArrayList<Task> scheduledTasks = new ArrayList<>();

            //For each of 5 runs create a new process queue
            Task[] tasks = processQueue.generate(i);
            Queue<Task> taskList = new LinkedList<Task>(Arrays.asList(tasks));
            //Queue for ready processes ordered by run time with ties broken by arrival time
            PriorityQueue<Task> readyQueue = new PriorityQueue<>(10, new Comparator<Task>() {
                public int compare(Task t1, Task t2) {
                    if(t1.getRunTime() == t2.getRunTime()) {
                        return t1.getArrivalTime() < t2.getArrivalTime() ? -1 : 1;
                    } else {
                        return t1.getRunTime() < t2.getRunTime() ? -1 : 1;
                    }
                }
            });

            while(!taskList.isEmpty() || !readyQueue.isEmpty()) {
                //Get the correct process to be scheduled
                Task t;
                if(readyQueue.isEmpty()) {
                    t = taskList.poll();
                } else {
                    t = readyQueue.poll();
                }

                //Update start and completion times for this process
                int startTime = Math.max((int)Math.ceil(t.getArrivalTime()), clock);
                if(startTime > 99) break;
                t.setStartTime(startTime);
                completionTime = startTime + t.getRunTime();
                t.setCompletionTime(completionTime);

                //Update completed tasks and clock
                scheduledTasks.add(t);
                tasksDone++;
                clock = (int)Math.ceil(completionTime);

                //Add processes to the ready queue that have arrived by this time
                while(taskList.peek() != null && taskList.peek().getArrivalTime() <= clock) {
                    readyQueue.add(taskList.poll());
                }

                //Variables for statistics for this process only
                float turnaroundTime = completionTime - t.getArrivalTime();
                float waitTime = turnaroundTime - t.getRunTime();
                int responseTime = startTime - t.getArrivalTime();

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

            //Make a copy of the completed tasks to use in the time chart
            ArrayList<Task> tasksChart = new ArrayList<Task>(scheduledTasks);

            //Print out the stats for all completed tasks for each run
            System.out.println("\n#######################################################################################");
            System.out.println("############ The following processes were completed for SJF run " + i + " #####################");
            System.out.println("#######################################################################################");
            while(!scheduledTasks.isEmpty()) {
                Task t = scheduledTasks.remove(0);
                System.out.println(t);
            }
            //print time chart of completed tasks for each run
            System.out.println("\n###########################################################");
            System.out.println("############ SJF Time Chart for run " + i + " #####################");
            System.out.println("###########################################################");
            new GanttChart(tasksChart);
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

    //Shortest remaining time (preemptive)
    private void srt() {
        //TODO
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
            Task[] tasks = processQueue.generate(i);
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

    /**
     * Higest Priority First (non-preemptive)
     */
    private void hpfNP() {
        //TODO
    }

    /**
     * Higest Priority First (preemptive)
     */
    private void hpfP() {
        //TODO
    }
}
