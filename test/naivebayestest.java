package test;
import org.columbia.seas.cs.amsterdam.*;
//import weka.core.converters.*;

public class naivebayestest
{
 public static void main(String[] args)
{
	String[] param = {"","-t","trainingData.arff","-l","testingData.arff","-d","outputData.arff"};
	String exec = "java -cp .\\lib\\JSAP-2.1.jar;.\\lib\\jdom.jar;.\\lib\\weka.jar;. NaiveBayesUpdatableTest -t \"trainingData.arff\" -l \"testingData.arff\" -d \"outputData.arff\"";

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

	// code block for branch1
	try {
		Runtime.getRuntime().exec(exec).waitFor();
	} catch(Exception e) {e.printStackTrace();}

	// code block for branch2
	Input input2 = DataProcessor.clone(input,2);
	String param2 = DataProcessor.processParam(input,2,"-t \"trainingData.arff\" -l \"testingData.arff\" -d \"outputData.arff\"");
	Output output2 = DataProcessor.clone(output,2);
	param2 = DataProcessor.processParam(output,2,param2);
	 DataProcessor.permute(input2.var(1));  
	String exec2 = "java -cp .\\lib\\JSAP-2.1.jar;.\\lib\\jdom.jar;.\\lib\\weka.jar;. NaiveBayesUpdatableTest @param";
	exec2 = exec2.replaceAll("@param", param2);
	System.out.println("Execution for branch2: "+exec2);
	try {
		Runtime.getRuntime().exec(exec2).waitFor();
	} catch(Exception e) {e.printStackTrace();}

	// code block for branch3
	Input input3 = DataProcessor.clone(input,3);
	String param3 = DataProcessor.processParam(input,3,"-t \"trainingData.arff\" -l \"testingData.arff\" -d \"outputData.arff\"");
	Output output3 = DataProcessor.clone(output,3);
	param3 = DataProcessor.processParam(output,3,param3);
	 DataProcessor.inverse(input3.var(2));  
	String exec3 = "java -cp .\\lib\\JSAP-2.1.jar;.\\lib\\jdom.jar;.\\lib\\weka.jar;. NaiveBayesUpdatableTest @param";
	exec3 = exec3.replaceAll("@param", param3);
	System.out.println("Execution for branch3: "+exec3);
	try {
		Runtime.getRuntime().exec(exec3).waitFor();
	} catch(Exception e) {e.printStackTrace();}

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

