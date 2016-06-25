package schedulingAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;

public class FirstComeFirstServe {
	
	public FirstComeFirstServe(ProcessQueue processQueue) {
		
		//Declare all variables needed for final output after 5 runs
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
            Task[] tasks = processQueue.generateProcesses(i);
            //Tasks are already sorted by arrivalTime so put in list for FCFS
            ArrayList<Task> taskList = new ArrayList<Task>(Arrays.asList(tasks));

            //Schedule tasks until either no more tasks or start time > 99
            while(!taskList.isEmpty()) {
                //Get the correct process to be scheduled
                Task t = taskList.remove(0);

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
            // EXITING THE 
            
            //Update final numbers needed for averages
            finalTurnaroundTime = finalTurnaroundTime + totalTurnaroundTime;
            finalWaitTime = finalWaitTime + totalWaitTime;
            finalResponseTime = finalResponseTime + totalResponseTime;
            finalTime = finalTime + totalTime;
            finalTasksDone = finalTasksDone + totalTasksDone;

            //Make a copy of the completed tasks to use in the time chart
            ArrayList<Task> tasksChart = new ArrayList<Task>(scheduledTasks);

            //Print out the stats for all completed tasks for each run
            System.out.println("\n########################################################################################");
            System.out.println("############ The following processes were completed for FCFS run " + i + " #####################");
            System.out.println("########################################################################################");
            while(!scheduledTasks.isEmpty()) {
                Task t = scheduledTasks.remove(0);
                System.out.println(t);
            }
            //print time chart of completed tasks for each run
            System.out.println("\n############################################################");
            System.out.println("############ FCFS Time Chart for run " + i + " #####################");
            System.out.println("############################################################");
            new GanttChart(tasksChart);
        }
        //Print out all calculated averages and Throughput for all 5 runs
        System.out.println("\n#######################################################################################");
        System.out.println("############ Final calculated averages and calculated throughput for FCFS #############");
        System.out.println("#######################################################################################");
        System.out.println("Average Turnaround Time = " + finalTurnaroundTime/finalTasksDone);
        System.out.println("Average Wait Time = " + finalWaitTime/finalTasksDone);
        System.out.println("Average Response Time = " + finalResponseTime/finalTasksDone);
        System.out.println("Throughput = " + finalTasksDone/finalTime);
        System.out.println();
    }
}
