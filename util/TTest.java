/*
Does the calculations for a Student's t-test.
*/

public class TTest
{
    public static double ttest(double x1, double s1, double x2, double s2, double n)
    {
	double s = Math.sqrt((s1*s1+s2*s2)/n);

	double t = (x1 - x2)/(s); // * Math.sqrt(2/n));

	return t;
    }

    public static void main(String[] args)
    {
	try
	{
	    double x1 = Double.parseDouble(args[0]);
	    double s1 = Double.parseDouble(args[1]);
	    double x2 = Double.parseDouble(args[2]);
	    double s2 = Double.parseDouble(args[3]);
	    double n = Double.parseDouble(args[4]);

	    System.out.println("t-value = " + TTest.ttest(x1, s1, x2, s2, n));
	}
	catch (Exception e)
	{
	    System.out.println("Usage: java TTest x1 s1 x2 s2 n");
	}
		

    }

}