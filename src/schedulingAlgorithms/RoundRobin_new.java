package schedulingAlgorithms;

import java.util.*;

/**
 * Created by Natera on 6/27/16.
 */
public class RoundRobin_new {
    private ProcessQueue processQueue;
    private int finalTasksDone;
    private float finalTime;
    private float finalTurnaroundTime;
    private float finalWaitTime;
    private float finalResponseTime;
    private float quantaLength;

    /**
     * Constructor method.
     *
     * @param processQueue (ProcessQueue) : A specialized Queue used for
     *     generating and sorting organized simulated processes.
     */
    public RoundRobin_new(ProcessQueue processQueue)
    {
        this.processQueue = processQueue;
        this.finalTasksDone = 0;
        this.finalTime = 0.0f;
        this.finalTurnaroundTime = 0.0f;
        this.finalWaitTime = 0.0f;
        this.finalResponseTime = 0.0f;
        this.quantaLength = 0.0f;
    }

    public void runRoundRobin_new() {
        for (int i = 1; i <= 5; i++) {
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
            ArrayList<Task> completedTasks = new ArrayList<>();
            Queue<Task> readyQueue = new LinkedList<>();
            Map<String, Float> remainingRunTimes = new HashMap<>();

            // For each of 5 runs create a new process queue
            Task[] tasks = processQueue.generateProcesses(i);
            // Sort task list by arrival time initially
            processQueue.sortByArrivalTime(tasks);
            // Place task list into a queue for processing with RR
            Queue<Task> taskList = new LinkedList<Task>(Arrays.asList(tasks));

        }

    }

}
