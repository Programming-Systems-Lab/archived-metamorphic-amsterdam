import java.util.Scanner;
import java.io.*;

/****************

Compares two output trace files to check that the sequence of events is exactly the same.
But does not consider the timing of events.

This is useful for when the trace is deterministic but the timing is not.

Assumes that the event is after the semicolon.

 **************/

public class CompareJSimSequence extends CompareJSimOutput
{
    public boolean compare(String line1, String line2)
    {
	// ONLY COMPARE THE EVENTS, NOT THE TIME

	if (line1.trim().equals("") == false && line2.trim().equals("") == false)
	{
	    // just take whatever is after the semicolon
	    String event1 = line1.split(";")[1].trim();
	    String event2 = line2.split(";")[1].trim();

	    return event1.equals(event2);
	}
	else
	    return true;
    }

    public static void main(String[] args)
    {
	new CompareJSimSequence().run(args);
    }

}

