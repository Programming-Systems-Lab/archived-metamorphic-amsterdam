import java.util.*;
import java.io.*;

/*
 * Mutates math operators
 */

public class Mutator2
{
    private int mutations = 0; // counts the number of mutations to make
    private ArrayList<String> lines = new ArrayList<String>();

    public void read(String name)
    {
	try
	{
	    // first, create the File object
	    File file = new File(name);

	    // now create the Scanner using the File
	    Scanner in = new Scanner(file);

	    boolean inComment = false;

	    // keep reading while there is more to read
	    while (in.hasNext())
	    {
		// read an entire line
		String line = in.nextLine();

		// print it to the screen
		//System.out.println(line);

		// add it to the arraylist
		lines.add(line);

		if (line.contains("/*")) inComment = true;

		// see if it contains an operator to mutate
		if (!inComment && line.contains("//") == false && canMutate(line))
		    mutations++;

		if (line.contains("*/")) inComment = false;
	    }

	    System.out.println("Mutation count is " + mutations);
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

	// figure out the name of the file (without its extension)
	String name = filename.split("[.]")[0];

	// check to make sure this is working right
	int check = 0;
	
	for (int i = 0; i < mutations; i++)
	{
	    int count = -1; // keeps track of the number of ops seen so far
	    try
	    {
		// update the name
		String myName = name + i;

		// create the File
		File file = new File(filename.replace(name, myName));

		// tracks whether or not we're in a comment
		boolean inComment = false;
		
		// initialize the PrintWriter
		out = new PrintWriter(file);
		
		for (String line : lines)
		{
		    /*
		    // if the line contains the filename, replace it
		    if (line.contains(" " + name + " ")) line = line.replace(" " + name + " ", " " + myName + " ");
		    if (line.contains(name + ".this")) line = line.replace(name, myName);
		    if (line.contains(name + "(")) line = line.replace(name, myName);

		    // just for SMO
		    if (line.contains("BinarySMO ") || line.contains("BinarySMO[")) line = line.replace(name, myName);
		    */

		    // start of the comment
		    if (line.contains("/*")) inComment = true;

		    // see if this line can be mutated
		    if (!inComment && line.contains("//") == false && canMutate(line))
		    {
			// if so, increase the counter
			count++;
			
			// if we're at the right version number, mutate the line
			if (count == i)
			{
			    out.println(mutate(line));
			    check++;
			}
			else
			    out.println(line);
		    }
		    else
		    {
			out.println(line);
		    }

		    // end of the comment
		    if (line.contains("*/")) inComment = false;
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
	
	System.out.println("Final count is " + check);
    }


    private boolean canMutate(String line)
    {
	return line.contains("\"") == false && (line.contains("+") || (line.contains("-") && line.contains("->") == false) || line.contains(" * ") || line.contains("/"));
    }

    private String mutate(String line)
    {
	if (line.contains("++")) return line.replace("++", "--");
	else if (line.contains("--")) return line.replace("--", "++");
	else if (line.contains("+")) return line.replace("+", "-");
	else if (line.contains("-") && line.contains("->") == false) return line.replace("-", "+");
	else if (line.contains(" * ")) return line.replace(" * ", "/");
	else if (line.contains("/")) return line.replace("/", "*");
	else return line; // rut roh
    }

    public void go(String filename)
    {
	read(filename);
	write(filename);
    }
	

    public static void main(String[] args)
    {
	try
        {
	    // read the filename from the command line
	    String filename = args[0];
	    new Mutator2().go(filename);
      	}
	catch (ArrayIndexOutOfBoundsException e)
	{
	    System.out.println("You didn't specify a file");
	}
    }


}