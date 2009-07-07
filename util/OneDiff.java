import java.util.*;
import java.io.*;

public class OneDiff
{

    public void read(String name1, String name2)
    {
	Scanner in1 = null, in2 = null;

	try
	{
	    File file1 = new File(name1);
	    in1 = new Scanner(file1);
	}
	catch (Exception e)
	{
	    System.out.println("ERROR: Cannot read file " + name1);
	    return;
	}

	try
	{
	    File file2 = new File(name2);
	    in2 = new Scanner(file2);
	}
	catch (Exception e)
	{
	    System.out.println("ERROR: Cannot read file " + name2);
	    return;
	}


	int line = 0;
	
	// keep reading while there is more to read
	while (in1.hasNext() && in2.hasNext())
	{
	    line++;
	    
	    // read an entire line
	    String line1 = in1.nextLine();
	    String line2 = in2.nextLine();
	    
	    if (line1.equals(line2) == false)
	    {
		System.out.println("DIFF FOUND: line " + line);
		System.out.println("File 1: " + line1);
		System.out.println("File 2: " + line2);
		return;
	    }
	}
	
	if (in1.hasNext() || in2.hasNext())
	{
	    System.out.println("DIFFERENT FILE LENGTHS");
	}
	else
	{
	    System.out.println("ok");
	}


    }


    public static void main(String[] args)
    {
	try
        {
	    // read the filename from the command line
	    String filename1 = args[0];
	    String filename2 = args[1];
	    new OneDiff().read(filename1, filename2);
      	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }


}