import java.util.*;
import java.io.*;

public class Negator3
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
	String myName = filename + ".n";

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


    private void negate()
    {
	ArrayList<String> newLines = new ArrayList<String>();
	StringTokenizer tok;
	String line, newLine;

	for (int i = 0; i < lines.size(); i++)
	{
	    line = lines.get(i);

	    newLine = "";
	    tok = new StringTokenizer(line, ",");

	    int count = 0;

	    while (tok.hasMoreTokens())
	    {
		count ++;

		if (newLine != "") newLine += ",";

		String value = tok.nextToken();

		// we don't want to change the last token (the label)
		// if there are more tokens, this isn't the last one
		if (tok.hasMoreTokens() && count <= 3)
		{
		    try
		    {
			// see if it's a floating point number
			Float.parseFloat(value);
			// if we got here, it is
			if (value.startsWith("-")) value = value.substring(1, value.length());
			else value = "-" + value;
		    }
		    catch (NumberFormatException e) { }
		}
		newLine += value;
	    }

	    //System.out.println("newLine " + newLine);
	    newLines.add(newLine);
	}

	lines = newLines;

    }

    public void go(String filename)
    {
	read(filename);
	negate();
	write(filename);
    }
	

    public static void main(String[] args)
    {
	try
        {
	    // read the filename from the command line
	    String filename = args[0];
	    new Negator3().go(filename);
      	}
	catch (ArrayIndexOutOfBoundsException e)
	{
	    System.out.println("You didn't specify a file");
	}
    }


}