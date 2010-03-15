package org.columbia.seas.cs.amsterdam;
/*
 * MetaDescriptorParser.java
 *
 * Created on October 28, 2008, 12:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import org.jdom.*; 
import org.jdom.input.*;  
import java.io.*;
import java.util.*;


/**
 *
 * @author Kuang Shen
 */
public class MetaDescriptorParser {
    
    /** parser method */
    public void MetaDescriptorParsing(File xmlFile) {
        SAXBuilder parser = new SAXBuilder();
        try{
            
                Document doc = parser.build(xmlFile);
            
                Element desc = doc.getRootElement();
                String xmlName = xmlFile.getName();
                StringTokenizer st = new StringTokenizer(xmlName, ".");
                String canonicalName = st.nextToken();
                String testFileName = canonicalName+".java";
               
                    
                String testFileContent = "package test;\n"; 
                
                testFileContent += "import org.columbia.seas.cs.amsterdam.*;\n";  
                
                testFileContent += "//import weka.core.converters.*;\n";
                testFileContent += "\n";
                
                testFileContent += "public class "+canonicalName+"\n";
                testFileContent += "{\n";
                testFileContent += " public static void main(String[] args)\n" + "{\n";
                
                Element metaCondition = desc.getChild("METACONDITION");
                if (metaCondition != null)
                {
                    testFileContent += "\tif (! ("+ metaCondition.getValue() +") ) {\n";
                    testFileContent += "\t\tSystem.out.println(\"Test "+ desc.getValue() +" does not meet the condition \"); \n";
                    testFileContent += "\t\treturn; \n";
                    testFileContent += "\t} \n";
                }
                
                // process element EXECUTION
                Element execution = desc.getChild("EXECUTION");
                String exec = "";
                if (execution != null)
                {
                    exec = execution.getValue();
                    exec = Util.escapeChars(exec);
                }
                
                // process element PARAMETERS
                Element parameters = desc.getChild("PARAMETERS");
                if (parameters !=null)
                {
                    exec = exec.replaceAll("@param", parameters.getValue());
                    
                    exec = exec.replaceAll("\"", "\\\\"+"\"");
                    
                    
                    String param = Util.paramToArray(parameters.getValue());
                    testFileContent += "\tString[] param = "+param+ ";\n";
                }
                
                testFileContent += "\tString exec = \""+exec+ "\";\n\n";
                
                Element inputList = desc.getChild("INPUT");
                testFileContent += "\tInput input = new Input();\n";    
                if (inputList!=null) {                  
                    List inputVars = inputList.getChildren();  
                    for (int j=0; j<inputVars.size(); j++)
                    {
                        int n=j+1;
                        String typeString = ((Element)inputVars.get(j)).getAttributeValue("TYPE");
                        String nameString = "inputVar"+n;
                        
                        testFileContent += Util.processVar(typeString, nameString, ((Element)inputVars.get(j)).getValue() );
                        testFileContent += "\tinput.add("+ nameString +");\n\n";
                    }
                }
                
                
                Element outputList = desc.getChild("OUTPUT");
                testFileContent += "\tOutput output = new Output();\n"; 
                if (outputList!=null) {
                    List outputVars = outputList.getChildren();
                    for (int j=0; j<outputVars.size(); j++)
                    {
                        int n=j+1;
                        String typeString = ((Element)outputVars.get(j)).getAttributeValue("TYPE");
                        String nameString = "ouputVar"+n;
                        
                        testFileContent += Util.processVar(typeString, nameString, ((Element)outputVars.get(j)).getValue() );
                        testFileContent += "\toutput.add("+ nameString +");\n\n";
                    }
                }
                
                // processing PRE block for pre-condition chanchings
                Element pre = desc.getChild("PRE");
                if (pre!=null) {
                    List preProperties = pre.getChildren("PROPERTY");
                    if (!preProperties.isEmpty())
                    {

                        for (int j=0; j<preProperties.size(); j++)
                        {
                            if (((Element)preProperties.get(j)).getChild("OP") != null)
                                testFileContent += "\t"+Util.processKeywords(((Element)preProperties.get(j)).getChild("OP").getValue());
                            testFileContent += "\tif (";
                            String trans = Util.processKeywords(((Element)preProperties.get(0)).getChild("ASSERT").getValue());
                            testFileContent += "! ("+ trans +") ) \n";
                            int number = j+1;
                            testFileContent += "\t\tSystem.out.println(\"Pre-condition:"+number+" checking failed. \"); \n\n";
                        }
                    }
                }
                
                // processing POST block for post-condition chanchings
                Element post = desc.getChild("POST");
                if (post!=null) {
                    List branches = post.getChildren("BRANCH");
                    if (!branches.isEmpty())
                    {
                        testFileContent += "\t"+"long cpuTimePreTrans = Util.getSystemTime(); \n";
                        for (int t=0; t<branches.size(); t++)
                        {
                            Element branch = (Element)branches.get(t);
                            int num = t+1;
                            
                            testFileContent += "\t// code block for branch"+num+"\n";
                            
                            
                            if (branch.getAttributeValue("OPTION").equals("main"))
                            {
                                
                                testFileContent += "\t"+"final String mainExec="+"new String(exec);"+"\n";
                            } 
                            else
                            {
                                String operationLine = Util.processKeywordsBranch(branch.getValue(), t+1);
                                String paramStr = "";
                                if (parameters !=null)
                                {           
                                    paramStr = parameters.getValue();
                                    paramStr = paramStr.replaceAll("\"", "\\\\"+"\"");
                                }
                                testFileContent += "\t" + "final Input input" + num 
                                                    + " = DataProcessor.clone(input,"+num+");" + "\n";
                                testFileContent += "\t" + "final Output output" + num 
                                                    + " = DataProcessor.clone(output,"+ num +");" + "\n";
                                testFileContent += "\t" + "final String param" + num 
                                                    + " = DataProcessor.processParam(input, output,"+num+",\"" +paramStr+ "\");" + "\n";

                                testFileContent += "\t" + operationLine + " \n";
                                testFileContent += "\t"+"final String exec"+num+" = \""+Util.escapeChars(execution.getValue())+"\""+".replaceAll(\"@param\", param"+num+");\n";
                                //testFileContent += "\t\tRuntime.getRuntime().exec(\"cmd /C \"+exec"+num+");\n";
                                /*testFileContent += "\t\tOutput output" + num + " = new Output();\n";
                                
                                if (outputList!=null) {
                                    List outputVars = outputList.getChildren();
                                    for (int j=0; j<outputVars.size(); j++)
                                    {
                                        String typeString = ((Element)outputVars.get(j)).getAttributeValue("TYPE");
                                        String nameString = "var"+j+"_"+num;
                        
                                        testFileContent += "\t"+Util.processVar(typeString, nameString, ((Element)outputVars.get(j)).getValue() );
                                        testFileContent += "\t\toutput" + num + ".add("+ nameString +");\n";
                                    }
                                }*/
                                
                            } 
                            
                        }
                        testFileContent += "\t"+"long cpuTimeCostTrans = Util.getSystemTime()-cpuTimePreTrans; \n";
                        testFileContent += "\t"+"System.out.println(\"CPU time cost for sandboxing: \"+cpuTimeCostTrans+\" nanosec\"); \n";
                        for (int t=0; t<branches.size(); t++)
                        {
                            Element branch = (Element)branches.get(t);
                            int num = t+1;
                            
                            testFileContent += "\t// code block for branch"+num+"\n";
                            
                            
                            if (branch.getAttributeValue("OPTION").equals("main"))
                            {
                                testFileContent += "\t" + "new Thread(new Runnable() {\n";
                                testFileContent += "\t\t" + "public void run() {\n";
                                testFileContent += "\t\t\t" + "try {\n";
                                testFileContent += "\t\t\t\t"+"final long cpuTimePreMain = Util.getSystemTime(); \n";

                                testFileContent += "\t\t\t\t"+"Runtime.getRuntime().exec(mainExec).waitFor();\n";
                                //testFileContent += "\t\tRuntime.getRuntime().exec(\"cmd /C \"+exec);\n";
                                
                                testFileContent += "\t\t\t\t"+"final long cpuTimeCostMain = Util.getSystemTime()-cpuTimePreMain; \n";
                                testFileContent += "\t\t\t\t"+"System.out.println(\"CPU time cost for main branch: \"+cpuTimeCostMain+\" nanosec\"); \n";
                                
                                testFileContent += "\t\t\t" + "} catch(Exception ex) {} }\n";
                                testFileContent += "\t\t" + "}).start();\n";
                            } 
                            if (branch.getAttributeValue("OPTION").equals("sequential"))
                            {
                                
                                
                                testFileContent += "\t"+"System.out.println(\"Execution for branch"+num+": \"+exec"+num+");\n";
                                

                                testFileContent += "\t"+"try {\n";
                                testFileContent += "\t"+"Runtime.getRuntime().exec(exec"+num+").waitFor();\n";
                                testFileContent += "\t} catch(Exception e) {e.printStackTrace();}\n\n";
                            } 
                            if (branch.getAttributeValue("OPTION").equals("para"))
                            {
                                testFileContent += "\t" + "new Thread(new Runnable() {\n";
                                testFileContent += "\t\t" + "public void run() {\n";
                                testFileContent += "\t\t\t" + "try {\n";

                                
                                testFileContent += "\t\t\t\t"+"System.out.println(\"Execution for branch"+num+": \"+exec"+num+");\n";
                                


                                testFileContent += "\t\t\t\t"+"Runtime.getRuntime().exec(exec"+num+").waitFor();\n";
  
                                testFileContent += "\t\t\t" + "} catch(Exception ex) {} }\n";
                                testFileContent += "\t\t" + "}).start();\n";

                            } 
                        }
                    }
                    
                    List postProperties = post.getChildren("PROPERTY");
                    if (!postProperties.isEmpty())
                    {

                        for (int j=0; j<postProperties.size(); j++)
                        {
                            if (((Element)postProperties.get(j)).getChild("OP") != null)
                                testFileContent += "\t"+Util.processKeywords(((Element)postProperties.get(j)).getChild("OP").getValue())+";\n";
                            testFileContent += "\tif (";
                            String trans = Util.processKeywords(((Element)postProperties.get(j)).getChild("ASSERT").getValue());
                            testFileContent += "!("+ trans +") ";
                            testFileContent += ") \n";
                            int number = j+1;
                            testFileContent += "\t\tSystem.out.println(\"Post-condition:"+number+" checking failed. \"); \n";
                            testFileContent += "\telse\n";
                            testFileContent += "\t\tSystem.out.println(\"Post-condition:"+number+" checking passed. \"); \n";
                        }
  
                    }
                }
                
               
                // end of main method
                testFileContent += " }\n";
                
                //end of file
                testFileContent += "}\n";
                 try {
                    //System.out.println(" content\n"+testFileContent);
                    File testFile = new File("./src/test/"+testFileName); 
                    FileOutputStream out = new FileOutputStream(testFile);
                    PrintStream file = new PrintStream(out);
                    //System.out.println("content:"+testFileContent);
                    file.println(testFileContent);
                
                
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
     public static void main(String[] args)
    {
	

	MetaDescriptorParser p = new MetaDescriptorParser();
	p.MetaDescriptorParsing(new File(args[0]));
    }
    
}
