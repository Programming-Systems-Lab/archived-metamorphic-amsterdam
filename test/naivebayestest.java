package test;
import org.columbia.seas.cs.amsterdam.*;
import java.util.*;
//import weka.core.converters.*;

public class naivebayestest
{
 public static void main(String[] args)
{
	String[] param = {"","-t","iris.arff","-l","irisTest.arff","-d","outputData.arff"};
	String exec = "java -cp .\\lib\\JSAP-2.1.jar;.\\lib\\jdom.jar;.\\lib\\weka.jar;. NaiveBayesUpdatableTest -t \"iris.arff\" -l \"irisTest.arff\" -d \"outputData.arff\"";

	Input input = new Input();
	ArffFile inputVar1 = new ArffFile(param[2]);
	input.add(inputVar1);

	ArffFile inputVar2 = new ArffFile(param[4]);
	input.add(inputVar2);

	Output output = new Output();
	ArffFile ouputVar1 = new ArffFile(param[6]);
	output.add(ouputVar1);

	if (! (DataProcessor.numAttributes(input.var(1)) == DataProcessor.numAttributes(output.var(1))) ) 
		System.out.println("Pre-condition:1 checking failed. "); 

	if (! (DataProcessor.numAttributes(input.var(1)) == DataProcessor.numAttributes(output.var(1))) ) 
		System.out.println("Pre-condition:2 checking failed. "); 

	long cpuTimePreTrans = new Date().getTime(); 
	// code block for branch1
	final String mainExec=new String(exec);
	// code block for branch2
	final Input input2 = DataProcessor.clone(input,2);
	final Output output2 = DataProcessor.clone(output,2);
	final String param2 = DataProcessor.processParam(input, output,2,"-t \"iris.arff\" -l \"irisTest.arff\" -d \"outputData.arff\"");
	 DataProcessor.permute(input2.var(1));  
	final String exec2 = "java -cp .\\lib\\JSAP-2.1.jar;.\\lib\\jdom.jar;.\\lib\\weka.jar;. NaiveBayesUpdatableTest @param".replaceAll("@param", param2);
	// code block for branch3
	final Input input3 = DataProcessor.clone(input,3);
	final Output output3 = DataProcessor.clone(output,3);
	final String param3 = DataProcessor.processParam(input, output,3,"-t \"iris.arff\" -l \"irisTest.arff\" -d \"outputData.arff\"");
	 DataProcessor.inverse(input3.var(2));  
	final String exec3 = "java -cp .\\lib\\JSAP-2.1.jar;.\\lib\\jdom.jar;.\\lib\\weka.jar;. NaiveBayesUpdatableTest @param".replaceAll("@param", param3);
	final long cpuTimeCostTrans = new Date().getTime()-cpuTimePreTrans; 
	
	// code block for branch1
	new Thread(new Runnable() {
		public void run() {
			try {
				final long cpuTimePreMain = new Date().getTime(); 
				Runtime.getRuntime().exec(mainExec).waitFor();
				final long cpuTimeCostMain = new Date().getTime()-cpuTimePreMain; 
                                System.out.println("CPU time cost for sandboxing: "+cpuTimeCostTrans+" nanosec"); 
				System.out.println("CPU time cost for main branch: "+cpuTimeCostMain+" nanosec"); 
			} catch(Exception ex) {} }
		}).start();
	// code block for branch2
	/*new Thread(new Runnable() {
		public void run() {
			try {
				System.out.println("Execution for branch2: "+exec2);
				Runtime.getRuntime().exec(exec2).waitFor();
			} catch(Exception ex) {} }
		}).start();
	// code block for branch3
	new Thread(new Runnable() {
		public void run() {
			try {
				System.out.println("Execution for branch3: "+exec3);
				Runtime.getRuntime().exec(exec3).waitFor();
			} catch(Exception ex) {} }
		}).start();*/
	if (!(DataProcessor.equal( output, output2)) ) 
		System.out.println("Post-condition:1 checking failed. "); 
	else
		System.out.println("Post-condition:1 checking passed. "); 
	DataProcessor.inverse( output3.var(1));
	if (!(DataProcessor.equal( output, output3 )) ) 
		System.out.println("Post-condition:2 checking failed. "); 
	else
		System.out.println("Post-condition:2 checking passed. "); 
 }
}

