/*
 * SimpleParser.java
 *
 * Created on November 25, 2008, 4:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.columbia.seas.cs.amsterdam;

import org.jdom.*; 
import org.jdom.input.*;  
import java.io.*;
import java.util.*;
import com.martiansoftware.jsap.*;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Kuang Shen
 */
public class SimpleParser {
    
    /** Creates a new instance of SimpleParser */
    public SimpleParser() {
    }
    
    public static void parse(String[] args)
    {
        try {   
                Element root = new Element("TESTDESCRIPTOR");
                
                String line = "";
                
                Element exec = new Element("EXECUTION");
                Element input = new Element("INPUT");
                Element output = new Element("OUTPUT");
                Element param = new Element("PARAMETERS");
                Element post = new Element("POST");
                Element mainBranch = new Element("BRANCH");
                mainBranch.setAttribute("OPTION", "main");
                post.addContent(mainBranch);
                int branchCounter = 1;

                Scanner in = new Scanner(new File(args[0]));
                
                while (in.hasNext())
                { 
                    line = in.nextLine();
                    if (line.startsWith("command=") || line.startsWith("command ="))
                    {
                        line = getValue(line, "command");
                        exec.setText(line);

                    }
                    else if (line.startsWith("param=") || line.startsWith("param ="))
                    {
                        line = getValue(line, "param");
                        String[] raw = CommandLineTokenizer.tokenize(line);
                        
                        for (int i=0; i<raw.length; i++)
                        {
                            int j = i+1;
                            if (raw[i].startsWith("input"))
                            {
                                Element var = new Element("VAR");
                                var.setAttribute("TYPE","ARFF_FILENAME");
                                var.setText("@param["+j+"]");
                                input.addContent(var);
                            }
                            else if (raw[i].startsWith("output"))
                            {
                                Element var = new Element("VAR");
                                var.setAttribute("TYPE","ARFF_FILENAME");
                                var.setText("@param["+j+"]");
                                output.addContent(var);
                            }
                        }
                        
                    }
                    else if (line.startsWith("paramv=") || line.startsWith("paramv ="))
                    {
                        line = getValue(line, "paramv");
                        
                        param.setText(line);
                        
                    }
                    else if (line.startsWith("if"))
                    {
                        line = line.replace("if ", "");
                        String[] statements = line.split(" then ");
                        
                        Element newBranch = new Element("BRANCH");
                        newBranch.setAttribute("OPTION", "sequantial");
                        String branchOp = statements[0];
                        branchCounter++;
                        branchOp = replaceOperators(branchOp,branchCounter);
                        branchOp = branchOp+";";
                        newBranch.setText(branchOp);
                        post.addContent(newBranch);
                        
                        Element newProp = new Element("PROPERTY");
                        Element newAssert = new Element("ASSERT");
                        String Prop = statements[1];
                        Prop = replaceOperators(Prop,branchCounter);
                        newAssert.setText(Prop);
                        newProp.addContent(newAssert);
                        post.addContent(newProp);
                    }  
                   
                }
                 root.addContent(exec);
                 root.addContent(param);
                 root.addContent(input);
                 root.addContent(output);
                 root.addContent(post);
                 Document doc = new Document(root);
                 
                in.close();
                XMLOutputter serializer = new XMLOutputter();
                PrintWriter out = new PrintWriter(new File(args[0]+".xml"));
                serializer.output(doc, out);

                
        } catch (Exception e) {e.printStackTrace();} 
    }
    
    private static String getValue(String line, String phrase)
    {
        line = line.replace(phrase+"=","");
        line = line.replace(phrase+" = ","");
        line = line.replace(phrase+" =","");
        return line;
    }
    
    private static String replaceOperators(String str, int n)
    {
        str = str.replaceAll("permute", "@op_permute");
        str = str.replaceAll("inverse", "@op_inverse");
        str = str.replaceAll("negate", "@op_negate");
        str = str.replaceAll("negate", "@op_negate");
        str = str.replaceAll("equal","@equal");
        str = str.replaceAll("output","@branch1, @branch"+n+"");
        //str = str.replaceAll("input", "@input");
        str = str.replaceAll("input1", "@input.var(1)");
        str = str.replaceAll("input2", "@input.var(2)");
        str = str.replaceAll("input3", "@input.var(3)");
        str = str.replaceAll("input4", "@input.var(4)");
        str = str.replaceAll("input5", "@input.var(5)");
        str = str.replaceAll("input6", "@input.var(6)");
        str = str.replaceAll("input7", "@input.var(7)");
        str = str.replaceAll("input8", "@input.var(8)");
        str = str.replaceAll("input9", "@input.var(9)");
        return str;
    }
    
    public static void main(String[] args)
    {
        if (args[0].endsWith(".xml"))
        {
            MetaDescriptorParser p = new MetaDescriptorParser();
	    p.MetaDescriptorParsing(new File(args[0]));
        }    
        else
        {
            parse(args);
            MetaDescriptorParser p = new MetaDescriptorParser();
	    p.MetaDescriptorParsing(new File(args[0]+".xml"));
        }
    }
    
}
