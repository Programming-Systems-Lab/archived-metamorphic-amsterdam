import java.util.Scanner;
import java.io.*;

/*********************************

Compares the output trace in the first argument to those in the other arguments.
Checks that any event that happens at time T in the first trace also happens at
time T in the second trace (but doesn't care about the overall sequence in the trace).

Useful for "parallel" simulations when the timing is deterministic but the ordering
of events in the trace is not.

Assumes the time is before the colon and the event is after the semicolon.

 ********************************/

public class CheckSameTimes
{
    public void run(String[] args)
    {
	if (args.length < 2) 
	{
	    System.out.println("Need to specify at least two files!");
	    return;
	}
	else
	{
	    // the file to which everything will be compared
	    String file = args[0];

	    // loop through the rest of the array of filenames
	    for (int i = 1; i < args.length; i++)
	    {
		boolean success = checkSameTimes(file, args[i]);
		if (success) System.out.println(file + " and " + args[i] + " have the same event timings");
		else System.out.println(file + " and " + args[i] + " are different");
	    }
	}

    }

    public boolean checkSameTimes(String file1, String file2)
    {
	Scanner scan = null;
	try
	{
	    // create the Scanner
	    scan = new Scanner(new File(file1));

	    // there may be error messages and such
	    // just get the Scanner into a state where it ignores those
	    String line = null;
	    
	    // once we found a line that starts with "0", we know we're into the trace
	    boolean stop = false;
	    while (!stop)
	    {
		line = scan.nextLine();
		if (line.startsWith("0")) stop = true; 
	    }

	    // now take each non-blank line and find its location in file2
	    while (scan.hasNext())
	    {
		line = scan.nextLine();
		if (line.trim().equals("") == false)
		{
		    String time = line.split(":")[0];
		    String event = line.split(";")[1].trim();
		    //System.out.println("Looking for: " + event + ".");
		    // now get the location in file2
		    String othertime = findTime(event, file2);
		    if (othertime == null)
		    {
			System.out.println(event + " not found in " + file2);
			return false;
		    }
		    else if (time.equals(othertime) == false)
		    {
			System.out.println("Timing is different: " + time + " in " + file1 + "; " + othertime + " in " + file2);
			return false;
		    }
		}
	    }

	}
	catch (Exception e) { e.printStackTrace(); }
	finally { scan.close(); }
	return true;

    }


    public String findTime(String target, String file)
    {
	Scanner in = null;
	try
	{
	    in = new Scanner(new File(file));

	    while (in.hasNext())
	    {		
		String line = in.nextLine();
		if (line.contains(":") && line.contains(";"))
		{
		    String time = line.split(":")[0];
		    String event = line.split(";")[1].trim();
		    //System.out.println("comparing: " + event + ". to " + target + ".");
		    if (event.equals(target)) return time;
		}
	    }
	}
	catch (Exception e) { e.printStackTrace(); }
	finally { in.close(); }

	// the default return value indicates that the line was not found
	return null;
    }


    public static void main(String[] args)
    {
	new CheckSameTimes().run(args);
    }

}