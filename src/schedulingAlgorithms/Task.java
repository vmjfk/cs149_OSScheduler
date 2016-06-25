package schedulingAlgorithms;

/**
 * Task is a helper class for creating task objects. These tasks
 * are simulations of running processes in an operating system
 */
class Task implements Cloneable{
    private String name;
    private int arrivalTime;
    private float runTime; //Burst time
    private int priority;
    private int startTime;
    private float completionTime;

    public Task(String name, int arrivalTime, float runTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.runTime = runTime;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public float getRunTime() {
        return runTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getStartTime() {
        return startTime;
    }

    public float getCompletionTime() {
        return completionTime;
    }

    public void setRunTime(float runTime) {
        this.runTime = runTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setCompletionTime(float completionTime) {
        this.completionTime = completionTime;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch(Exception e) {
            return null;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Task){
            Task otherTask = (Task) o;
            return name.equals(otherTask.getName());
        }
        return false;
    }
    @Override
    public String toString() {
        return "Process: " + name + "\n\tArrival Time = " + arrivalTime +
                "\n\tExpected Run Time = " + runTime + "\n\tPriority = " + priority
                + "\n\tStart Time = " + startTime + "\n\tCompletion Time = " + completionTime;
    }
}
