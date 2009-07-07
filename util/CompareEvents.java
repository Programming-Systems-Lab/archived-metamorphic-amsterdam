import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;


/*********************************

Compares the output trace in the first argument to those in the other arguments.
Reports how many events in the first also happen in the second by looking at
which steps are actually "started".

Useful for "choice" simulations when we want to see how close two traces are.

Assumes the time is before the colon and the event is after the semicolon.

 ********************************/

public class CompareEvents
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

	    ArrayList<String> events = getEvents(file);
	    System.out.println(file + " has " + events.size() + " events");

	    // loop through the rest of the array of filenames
	    for (int i = 1; i < args.length; i++)
	    {
		int eventsInCommon = compareEvents(events, getEvents(args[i]));
		if (events.size() == eventsInCommon) System.out.println(file + " and " + args[i] + " have the same events");
		else System.out.println(file + " and " + args[i] + " are different by " + (events.size()-eventsInCommon));
	    }
	}

    }


    // finds all the lines in the file that end with "started"
    public ArrayList<String> getEvents(String file)
    {
	ArrayList<String> events = new ArrayList<String>();
	Scanner scan = null;
	try
	{
	    scan = new Scanner(new File(file));

	    String line;

	    while(scan.hasNext())
	    {
		line = scan.nextLine().trim();
		if (line.endsWith("started"))
		{
		    events.add(line.split(";")[1].trim());
		}
	    }

	}
	catch (Exception e) { }

	return events;
    }


    public int compareEvents(ArrayList<String> list1, ArrayList<String> list2)
    {
	int count = 0;

	for (String event : list1)
	{
	    // System.out.println("Looking for " + event);
	    if (list2.contains(event))
	    {
		// System.out.println("Found it");
		count++;
	    }
	}
	
	return count;
    }


    public static void main(String[] args)
    {
	new CompareEvents().run(args);
    }

}