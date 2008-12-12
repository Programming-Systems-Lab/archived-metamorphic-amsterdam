package test;
import org.columbia.seas.cs.amsterdam.*;
//import weka.core.converters.*;

public class simpleTest
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

	if (!(DataProcessor.equal(output, output2)) ) 
		System.out.println("Post-condition:1 checking failed. "); 
	else
		System.out.println("Post-condition:1 checking passed. "); 
 }
}

