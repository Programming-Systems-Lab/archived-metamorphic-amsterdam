import java.util.*;
import java.io.*;

public class Permutor
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
	String myName = filename + ".p";

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


    private void randomize()
    {
	for (int i = 0; i < lines.size() * 3; i++)
	{
	    int index = (int)(Math.random() * lines.size());
	    String line = lines.get(index);
	    lines.remove(index);
	    lines.add(line);
	}
    }

    public void go(String filename)
    {
	read(filename);
	randomize();
	write(filename);
    }
	

    public static void main(String[] args)
    {
	try
        {
	    // read the filename from the command line
	    String filename = args[0];
	    new Permutor().go(filename);
      	}
	catch (ArrayIndexOutOfBoundsException e)
	{
	    System.out.println("You didn't specify a file");
	}
    }


}