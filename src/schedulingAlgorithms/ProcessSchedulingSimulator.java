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
    private final static int NUM_OF_TEST_TYPES = 6;
    /**
     * The main method. The entry point for the application.
     * 
     * @param args (String Array) : User-defined arguments.
     */
    public static void main(String[] args) {
        ProcessSchedulingSimulator pss = new ProcessSchedulingSimulator();
        Scanner input = new Scanner(System.in);
        /* added by John Kennedy 28jun16
         * modifying the main() to allow for batch interaction. 
        */
        int option = 1;                // has to be initialized to something...
        boolean is_batch = false;       // assume interactive 
        if(args.length > 0)
        {
            // there must be an argument on the command line.
            try 
            {
                option = Integer.parseInt(args[0]);
                if(option > NUM_OF_TEST_TYPES)
                {
                    System.err.println("Option " + String.valueOf(option) + " not available");
                }
                
                // if we got this far, it must be batch 
                is_batch = true; 
            }
            catch(NumberFormatException e)
            {
                // not a number on the command line. Die. 
                System.err.println("Input error on command line " + args[0] + "not a number");
                return; 
                
            }
           
            
        }
        
        if(!is_batch)
        {
            while(option >= 1 && option <= 6)
            {
                runSim(pss,option);
                option = input.nextInt();
            }
        }else {
            runSim(pss,option);
            
        }
         
    }
    
    private static void runSim(ProcessSchedulingSimulator pss, int option) {
            pss.printMenuOptions();
            ProcessQueue processQueue = new ProcessQueue(QUANTA_MAX, RUNTIME_MAX, PRIORITY_MAX, NUM_OF_TASKS);
            switch(option) 
            {
                case 1:
                	new FirstComeFirstServe(processQueue).runNonPreemptive();
                	break;
                case 2:
                	new ShortestJobFirst(processQueue).runNonPreemptive();
                	break;
                case 3:
                	new ShortestRemainingTime(processQueue).runPreemptive();
                	break;
                case 4:
                    new RoundRobin(processQueue).runPreemptive();
                	break;
                case 5:
                	new HighestPriorityFirst(processQueue).runNonPreemptive();
                	break;
                case 6:
                	new HighestPriorityFirst_preemptive(processQueue).runPreemptive();
                	break;
                default:

                	break;
            }
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
                "(5) Highest Priority First (non-preemptive)\n" +
                "(6) Highest Priority First (Preemptive)" + "\n(7) Exit"
    	);	
    }
}
