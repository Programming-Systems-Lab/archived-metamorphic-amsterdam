import java.util.Scanner;
import java.io.*;

/*************************************
Calculates the Manhattan Distances between the output trace in the first argument
and the output traces in all the other arguments. Provides average, min, and max
normalized equivalences.
************************************/

public class CalcManhattanDist
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

	    // keeps track of the total normalized equivalence values
	    double total = 0;
	    
	    // the min and max
	    double min = 1;
	    double max = 0;

	    // for standard dev
	    double[] values = new double[args.length-1];

	    // keeps track of how many data points we have
	    int count = 0;

	    // loop through the rest of the array of filenames
	    for (int i = 1; i < args.length; i++)
	    {
		double equiv = getStats(file, args[i]);
		if (equiv == -1)
		{
		    System.out.println("ERROR! " + file + " and " + args[i] + " have different events");
		}
		else
		{
		    total += equiv;
		    if (equiv < min) min = equiv;
		    if (equiv > max) max = equiv;
		    values[count++] = equiv;
		}
	    }

	    System.out.println("---------------------------------------");
	    System.out.println("Number of similar files: " + count);
	    double average = total / count;
	    System.out.println("Average normalized equiv: " + average);
	    System.out.println("Min: " + min);
	    System.out.println("Max: " + max);


	    // now the standard deviation
	    double totalDist = 0;
	    for (int i = 0; i < count; i++)
	    {
		totalDist += (values[i] - average)*(values[i] - average);
	    }
	    double stdDev = Math.sqrt(totalDist/count);
	    System.out.println("Std Dev: " + stdDev);

	}

    }

    private double getStats(String file1, String file2)
    {
	try
	{
	    // create the Scanner
	    Scanner scan = new Scanner(new File(file1));

	    // there may be error messages and such
	    // just get the Scanner into a state where it ignores those
	    String line = null;
	    
	    // line counter
	    int count = 0;

	    // number of lines to ignore
	    int ignore = 0;

	    // once we found a line that starts with "0", we know we're into the trace
	    boolean stop = false;
	    while (!stop)
	    {
		line = scan.nextLine();
		count++;
		if (line.startsWith("0")) { stop = true; }
		else ignore++;
	    }

	    // tracks the total distance
	    int totalDist = 0;

	    // now take each non-blank line and find its location in file2
	    while (scan.hasNext())
	    {
		line = scan.nextLine();
		if (line.trim().equals("") == false)
		{
		    count++;
		    // only consider the message part after the time and id
		    line = line.split(";")[1].trim();
		    //System.out.println("Looking for: " + line + ".");
		    // now get the location in file2
		    int dist = findLineNumber(line, file2);
		    // a return value of -1 means it's not found... that's bad
		    if (dist == -1)
		    {
			System.out.println("LINE NOT FOUND: Line " + count + ": " + line);
			return -1;
		    }
		    else
		    {
			//System.out.println("Line number " + count + " in file2 is " + dist);
			totalDist += Math.abs(count-dist);
		    }
		}
	    }

	    System.out.println("Manhattan distance for " + file2 + " is " + totalDist);
	    
	    // calculate normalized equivalence
	    int totalLines = count - ignore; 
	    //System.out.println("Total #lines is " + (count-ignore));
	    double normalizedEquiv = 1.0 - ((double)totalDist*2)/(totalLines*totalLines);
	    System.out.println("Normalized equiv is for " + file2 + " is " + normalizedEquiv);
	    return normalizedEquiv;
	}
	catch (Exception e) { e.printStackTrace(); }
	return -1;

    }


    public int findLineNumber(String line, String file)
    {
	int lineNum = -1;
	Scanner in = null;
	try
	{
	    in = new Scanner(new File(file));
	    int count = 0;

	    while (in.hasNext())
	    {
		count++;
		
		String thisLine = in.nextLine();
		if (thisLine.contains(";"))
		{
		    thisLine = thisLine.split(";")[1].trim();
		    //System.out.println("comparing: " + thisLine + ". to " + line + ".");
		    if (thisLine.equals(line)) return count;
		}
	    }
	}
	catch (Exception e) { e.printStackTrace(); }
	finally { in.close(); }

	// the default return value indicates that the line was not found
	return -1;
    }


    public static void main(String[] args)
    {
	new CalcManhattanDist().run(args);
    }

}