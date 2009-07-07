import java.util.*;
import java.io.*;

/**
 * This is a class for adding a value to a data file.
 * It works for sparse data formats in the attr:value format.
 */

public class Adder2
{
    private ArrayList<String> lines = new ArrayList<String>();

    public void read(String name)
    {
	try
	{
	    // first, create the File object
	    File file = new File(name);

	    // now create the Scanner using the File
	    Scanner in = new Scanner(file);

	    // keep reading while there is more to read
	    while (in.hasNext())
	    {
		// read an entire line
		String line = in.nextLine();

		// print it to the screen
		//System.out.println(line);

		// add it to the arraylist
		lines.add(line);
	    }

	}
	catch (FileNotFoundException e)
	{
	    System.out.println(name + " doesn't exist");
	}
    }

    public void write(String filename)
    {
	// create a null PrintWriter outside the try block
	PrintWriter out = null;

	// update the name
	String myName = filename + ".a";

	try
	{
	    // create the File
	    File file = new File(myName);
	    
	    // initialize the PrintWriter
	    out = new PrintWriter(file);
	    
	    for (String line : lines)
	    {
		out.println(line);
	    }
		    
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    return;
	}
	finally
	{
	    try { out.flush(); } catch (Exception e) { }
	    try { out.close(); } catch (Exception e) { }
	}
	
    }


    private void add(int factor)
    {
	ArrayList<String> newLines = new ArrayList<String>();
	StringTokenizer tok;
	String line, newLine;

	for (int i = 0; i < lines.size(); i++)
	{
	    line = lines.get(i);

	    newLine = "";
	    tok = new StringTokenizer(line, " ");

	    // ignore the first token, which is the label
	    newLine += tok.nextToken();

	    while (tok.hasMoreTokens())
	    {
		String value = tok.nextToken();

		// separate the attribute number
		String attr = value.split(":")[0];

		value = value.split(":")[1];

		// see if it's a floating point number
		double x = Double.parseDouble(value);
		// if we got here, it is
		x += factor;
		value = "" + x;
		
		newLine += " " + attr + ":" + value;
	    }

	    //System.out.println("newLine " + newLine);
	    newLines.add(newLine);
	}

	lines = newLines;

    }

    public void go(String filename, int factor)
    {
	read(filename);
	add(factor);
	write(filename);
    }
	

    public static void main(String[] args)
    {
	try
        {
	    // read the filename from the command line
	    String filename = args[0];
	    int factor = Integer.parseInt(args[1]);
	    new Adder2().go(filename, factor);
      	}
	catch (ArrayIndexOutOfBoundsException e)
	{
	    //System.out.println("You didn't specify a file");
	    e.printStackTrace();
	}
    }


}