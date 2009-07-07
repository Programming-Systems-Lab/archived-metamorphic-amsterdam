public class ManhattanDistEst
{
    private static final int ITERATIONS = 100000;

    public static void main(String[] args)
    {
	int numElements = Integer.parseInt(args[0]);
	
	int targetDist = Integer.parseInt(args[1]);

	compute(numElements, targetDist);
    }

    public static void compute(int numElements, int targetDistance)
    {
	// create an array of elements
	int[] elements = new int[numElements];
	for (int i = 0; i < numElements; i++)
	    elements[i] = i;

	// keep track of the number of lists with a shorter distance
	int numCloser = 0;

	// the minimum distance observed
	int min = numElements * numElements / 2;

	// the maximum distance observed
	int max = 0;

	// create randomizations of the list, calculate distance, and compare
	for (int i = 0; i < ITERATIONS; i++)
	{
	    // create a permutation of the list
	    for (int j = 0; j < numElements; j++)
	    {
		int swap = (int)(Math.random()*numElements);
		int temp = elements[j];
		elements[j] = elements[swap];
		elements[swap] = temp;
	    }

	    // calculate the distance
	    int dist = 0;
	    for (int j = 0; j < numElements; j++)
	    {
		dist += Math.abs(elements[j] - j);
	    }

	    // update the min and max
	    if (dist < min)
		min = dist;
	    if (dist > max)
		max = dist;

	    // now see if this randomization is closer
	    //System.out.println("Distance is " + dist);
	    if (dist < targetDistance)
		numCloser++;
	}

	// report the results
	System.out.println(numCloser + " out of " + ITERATIONS + " had a distance closer than " + targetDistance);
	System.out.println("Confidence is " + (1 - (double)numCloser/ITERATIONS));
	System.out.println("Min: " + min + "; Max: " + max);

	


    }



}