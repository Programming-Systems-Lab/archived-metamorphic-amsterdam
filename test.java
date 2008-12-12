import org.columbia.seas.cs.amsterdam.*;
//import weka.core.converters.*;

public class test
{
 public static void main(String[] args)
{
	String[] param = {"","-t","trainingData.arff","-l","testingData","-d","outputData.arff"};
	String exec = "java NaiveBayes -t \"trainingData.arff\" -l \"testingData\" -d \"outputData.arff\"";

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

	try {
		Runtime.getRuntime().exec(exec);
	} catch(Exception e) {e.printStackTrace();}

	Input input2 = DataProcessor.clone(input,2);
	String param2 = DataProcessor.processParam(input,2,"-t \"trainingData.arff\" -l \"testingData\" -d \"outputData.arff\"");
	 DataProcessor.permute(input2.var(1));  
	Output output2 = DataProcessor.clone(output,2);
	param2 = DataProcessor.processParam(output,2,param2);
	try {
		Runtime.getRuntime().exec(exec);
	} catch(Exception e) {e.printStackTrace();}

	Input input3 = DataProcessor.clone(input,3);
	String param3 = DataProcessor.processParam(input,3,"-t \"trainingData.arff\" -l \"testingData\" -d \"outputData.arff\"");
	 DataProcessor.negate( input3.var(1), "attr_4" );  
	Output output3 = DataProcessor.clone(output,3);
	param3 = DataProcessor.processParam(output,3,param3);
	try {
		Runtime.getRuntime().exec(exec);
	} catch(Exception e) {e.printStackTrace();}

	if (!(DataProcessor.equal( output, output2)) ) 
		System.out.println("Post-condition:1 checking failed. "); 
	DataProcessor.negate( output2.var(1), "attr_4" );
	if (!(DataProcessor.equal( output, output2 )) ) 
		System.out.println("Post-condition:2 checking failed. "); 
 }
}

