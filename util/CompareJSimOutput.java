import java.util.Scanner;
import java.io.*;

/*************

Compares the first specified file to multiple output trace files 
to make sure they're exactly the same.

Makes no assumptions about the format of the output.

 ************/


public class CompareJSimOutput
{
    public boolean compare(String line1, String line2)
    {
	return line1.equals(line2);
    }


    public void run(String[] args)
    {
	String file1 = args[0];

	for (int i = 1; i < args.length; i++)
	{
	    System.out.println("Comparing " + file1 + " to " + args[i]);
	    if (compareFiles(file1, args[i]))
	    {
		System.out.println("Files are equal");
	    }
	    else
	    {
		System.out.println("Files are different");
	    }
	}
    }

    public boolean compareFiles(String file1, String file2)
    {
	try
	{
	    // create the Scanners
	    Scanner scan1 = new Scanner(new File(file1));
	    Scanner scan2 = new Scanner(new File(file2));

	    // there may be error messages and such
	    // just get each Scanner into a state where it ignores those
	    String line1 = null;
	    String line2 = null;

	    int count1 = 0;
	    int count2 = 0;

	    boolean stop = false;
	    while (!stop)
	    {
		line1 = scan1.nextLine();
		count1++;
		if (line1.startsWith("0")) stop = true;
	    }

	    stop = false;
	    while (!stop)
	    {
		line2 = scan2.nextLine();
		count2++;
		if (line2.startsWith("0")) stop = true;
	    }

	    if (compare(line1, line2) == false)
	    {
		    System.out.println("DIFF FOUND!");
		    System.out.println("File 1 (line " + count1 + "): " + line1); 
		    System.out.println("File 2 (line " + count2 + "): " + line2); 
		    return false;
	    }

	    // now keep reading until one or both are out of lines
	    while (scan1.hasNext() && scan2.hasNext())
	    {
		line1 = scan1.nextLine();
		count1++;

		line2 = scan2.nextLine();
		count2++;

		// compare the two lines
		if (compare(line1, line2) == false)
		{
		    System.out.println("DIFF FOUND!");
		    System.out.println("File 1 (line " + count1 + "): " + line1); 
		    System.out.println("File 2 (line " + count2 + "): " + line2); 
		    return false;
		}
	    }

	    // make sure they both had the same number of lines
	    if (scan1.hasNext() || scan2.hasNext())
	    {
		System.out.println("Different number of lines!");
		return false;
	    }

	    //System.out.println("Done. No differences found");

	}
	catch (Exception e) { e.printStackTrace(); }

	return true;
    }


    public static void main(String[] args)
    {
	new CompareJSimOutput().run(args);
    }

}