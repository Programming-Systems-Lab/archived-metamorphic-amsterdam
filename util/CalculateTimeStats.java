import java.util.Scanner;
import java.io.*;

/**

Takes a set of trace files and finds the average, min, and max of how long each
takes, as well as the standard deviation.

Assumes that the last line contains the time followed by a colon.

 */


public class CalculateTimeStats
{
    String[] files;
    int[] values;

    public CalculateTimeStats(String[] a)
    {
	files = a;
	values = new int[files.length];
    }

    public void run()
    {
	// track the running sum
	int totalTime = 0;
	int min = 10000000, max = 0;

	// get the time from each file, and track the min and max
	for (int i = 0; i < files.length; i++)
	{
	    int time = getTime(files[i]);
	    values[i] = time;
	    totalTime += time;

	    if (time < min) min = time;
	    if (time > max) max = time;
	}

	System.out.println("-----------------------------");
	
	// calculate the mean
	double average = ((double)totalTime)/files.length;
	System.out.println("Average: " + average);

	// now the standard deviation
	double totalDist = 0;
	for (int i = 0; i < values.length; i++)
	{
	    totalDist += (values[i] - average)*(values[i] - average);
	}
	double stdDev = Math.sqrt(totalDist/values.length);
	System.out.println("Std Dev: " + stdDev);

	// min and max
	System.out.println("Min: " + min);
	System.out.println("Max: " + max);

    }


    private int getTime(String file)
    {
	int time = 0;
	String lastTime = null;
	try
	{
	    Scanner in = new Scanner(new File(file));
	    while (in.hasNext())
	    {
		String line = in.nextLine();
		//System.out.println(line);
		if (line.contains(":"))
		{
		    lastTime = line;
		}
	    }
	    time = Integer.parseInt(lastTime.split(":")[0].trim());
	    System.out.println(file + ": " + time);
	}
	catch (Exception e) { e.printStackTrace(); }

	return time;

    }


    public static void main(String[] args)
    {
	new CalculateTimeStats(args).run();
    }

}