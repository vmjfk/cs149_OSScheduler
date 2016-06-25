package schedulingAlgorithms;

import java.util.ArrayList;

public class GanttChart {
	
	public GanttChart(ArrayList<Task> tasksChart) 
	{
		printTimeChart(tasksChart);
	}
	
    /**
     * printTimeChart prints a timeline representing when processes are scheduled
     * @param tasks the processes that have been scheduled
     */
    private void printTimeChart(ArrayList<Task> tasks) {
        int quanta = 0;
        System.out.print("0"); //The beginning of timeline
        for(Task task : tasks) {
            //Print underscore blanks for idle time
            while(quanta++ < task.getStartTime()) {
                System.out.print("___"); //3 for all characters of a process name
            }
            //Print the process name on the quanta that it runs
            quanta = (int) (task.getStartTime() + Math.ceil(task.getRunTime()));
            for(int i = 0; i < task.getRunTime(); ++i) {
                System.out.print(task.getName());
            }
        }
        System.out.println();//add new line after time chart
    }
}
