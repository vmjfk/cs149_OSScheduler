package schedulingAlgorithms;

/**
 * Task is a helper class for creating task objects. These tasks
 * are simulations of running processes in an operating system
 */
class Task implements Cloneable, Comparable {
    private String name;
    private int arrivalTime;
    private float runTime; // Burst time
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
        return this.name;
    }

    public int getArrivalTime() {
        return this.arrivalTime;
    }

    public float getRunTime() {
        return this.runTime;
    }

    public int getPriority() {
        return this.priority;
    }

    public int getStartTime() {
        return this.startTime;
    }

    public float getCompletionTime() {
        return this.completionTime;
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
        try 
        {
            return super.clone();
        } 
        catch (Exception e) 
        {
            return null;
        }
    }
    
    /**
     * 
     * @param otherInt
     * @return
     */
    public int compareArrivalTime(int otherArrivalTime)
    {
    	int difference = (this.arrivalTime - otherArrivalTime);
    	if (difference > 0) {return 1;}
    	else if (difference == 0) {return 0;}
    	else {return -1;}
    }
    
    /**
     * 
     * @param otherFloat
     * @return
     */
    public int compareRunTime(Float otherRunTime)
    {
    	float difference = (this.runTime - otherRunTime);
    	if (difference > 0) {return 1;}
    	else if (difference == 0) {return 0;}
    	else {return -1;}
    }
    
    /**
     * 
     * @param otherInt
     * @return
     */
    public int comparePriority(int otherPriority)
    {
        int difference = (this.priority - otherPriority);
        // Highest priority 1 > 4
        if (difference < 0) {return 1;}
        else if (difference == 0) {return 0;}
        else {return -1;}
    }
    
    @Override
    public boolean equals(Object o) 
    {
        if (o instanceof Task){
            Task otherTask = (Task) o;
            return name.equals(otherTask.getName());
        }
        return false;
    }
    @Override
    public String toString() 
    {
        return "Process: " + name + "\n\tArrival Time = " + arrivalTime +
                "\n\tExpected Run Time = " + runTime + "\n\tPriority = " + priority
                + "\n\tStart Time = " + startTime + "\n\tCompletion Time = " + completionTime;
    }

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
