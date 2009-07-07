import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

/*************************************
Calculates the Manhattan Distances between the output trace in the first argument
and the output traces in all the other arguments, BUT ONLY CONSIDERS THE PARTS
THAT ARE SIMILAR, ie the events that occur in both. Useful for comparing the
traces that include Choice steps.

Provides average, min, and max normalized equivalences.
************************************/

public class CalcSimilarManhattanDist
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
	    
	    // once we found a line that starts with "0", we know we're into the trace
	    boolean stop = false;
	    while (!stop)
	    {
		line = scan.nextLine();
		if (line.startsWith("0")) { stop = true; }
	    }

	    // tracks the lines in file1
	    ArrayList<String> file1Lines = new ArrayList<String>();

	    // now take each non-blank line and find its location in file2
	    while (scan.hasNext())
	    {
		line = scan.nextLine();
		if (line.trim().equals("") == false)
		{
		    // only consider the message part after the time and id
		    line = line.split(";")[1].trim();
		    //System.out.println("Looking for: " + line + ".");
		    int dist = findLineNumber(line, file2);
		    if (dist != -1)
		    {
			//System.out.println(line + " appears in both files");
			file1Lines.add(line);
		    }
		}
	    }

	    // finds the lines in file2 that appear in file1
	    ArrayList<String> file2Lines = getLines(file1Lines, file2);

	    if (file1Lines.size() != file2Lines.size()) System.out.println("SIZES ARE DIFFERENT");

	    // tracks the total distance
	    int totalDist = 0;

	    // calculate the distance
	    for (int i = 0; i < file1Lines.size(); i++)
	    {
		String line1 = file1Lines.get(i);
		if (file2Lines.contains(line1) == false)
		{
		    System.out.println("ERROR " + line1 + " not found in file2Lines");
		}
		else
		{
		    totalDist += Math.abs(file2Lines.indexOf(line1) - i);
		}
	    }

	    System.out.println("Manhattan distance for " + file2 + " is " + totalDist);
	    
	    // calculate normalized equivalence
	    int totalLines = file1Lines.size();
	    double normalizedEquiv = 1.0 - ((double)totalDist*2)/(totalLines*totalLines);
	    System.out.println("Normalized equiv is for " + file2 + " is " + normalizedEquiv);
	    return normalizedEquiv;
	}
	catch (Exception e) { e.printStackTrace(); }
	return -1;

    }

    private ArrayList<String> getLines(ArrayList<String> lines, String file)
    {
	ArrayList<String> myLines = new ArrayList<String>();

	Scanner in = null;

	try
	{
	    in = new Scanner(new File(file));

	    // ignore anything before the line that starts with 0
	    boolean stop = false;
	    while (!stop)
	    {
		if (in.nextLine().startsWith("0")) stop = true; 
	    }

	    while (in.hasNext())
	    {
		String line = in.nextLine();
		// make sure the line's not empty
		if (line.trim().equals("") == false)
		{
		    // only care about the event part (after the semicolon)
		    line = line.split(";")[1].trim();
		    // if the line is in the arraylist, add it
		    if (lines.contains(line))
		    {
			myLines.add(line);
		    }
		}
	    }
	}
	catch (Exception e) { e.printStackTrace(); }
	finally { in.close(); }

	return myLines;
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
	new CalcSimilarManhattanDist().run(args);
    }

}