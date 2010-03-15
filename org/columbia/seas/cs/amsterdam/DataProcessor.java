/*
 * DataProcessor.java
 *
 * Created on October 29, 2008, 11:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.columbia.seas.cs.amsterdam;

import weka.core.converters.*;
import weka.core.*;
import java.io.*;
import com.martiansoftware.jsap.*;
import java.util.*;

/**
 *
 * @author Kuang Shen
 */
public class DataProcessor {
    
    /** Creates a new instance of DataProcessor */
    public DataProcessor() {
    }
    
    public static boolean equal(Output o1, Output o2)
    {
        boolean flag = true;
        
        if (o1.size()!=o2.size())
            return false;
        else
        {
            for (int i=0;i<o1.size();i++)
            {
                Object var1 = o1.get(i);
                Object var2 = o2.get(i);
                flag = ((Variable)var1).equal((Variable)var2);
                
            }
        }
        
        return flag;
    }
    
    public static boolean equal(Input i1, Input i2)
    {
        boolean flag = true;
        
        if (i1.size()!=i2.size())
            flag = false;
        else
        {
            for (int i=0;i<i1.size();i++)
            {
                Object var1 = i1.get(i);
                Object var2 = i2.get(i);
                flag = ((Variable)var1).equal((Variable)var2);
            }
        }
        
        return flag;
    }
    
    public static void permute(ArffFile af)
    {
        System.out.println("Permuting Arff file: "+af.getPath());
        weka.core.converters.ArffLoader loader = new weka.core.converters.ArffLoader();
        weka.core.converters.ArffSaver saver = new weka.core.converters.ArffSaver();
        try{
            FileInputStream fis = new FileInputStream(af);
            loader.setSource(fis);
            Instances structure = loader.getStructure();
            Instances instances = loader.getDataSet();
            instances.randomize(new java.util.Random());
            System.out.println("Finished permuteing instances object for: "+af.getName());
            
            saver.setStructure(structure);
            saver.setInstances(instances);

            loader.reset();
            fis.close();
            
            saver.setFile(af);
            saver.writeBatch();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
     public static void permute(DBResultSet dbrs)
    {
         dbrs.randomize(new java.util.Random());
    }
     
    
    // create copy of the original file then permute the order of the data instances
    public static void permute(Object o)
    {
        ((Variable)o).permute();
    }

    // create copy of the original file then negate the value of the data instances's column or row
    public static void negate(Object o, int colNumber, int rowNumber)
    {
        ((Variable)o).negate(colNumber, rowNumber);
    }
    
    public static void negate(ArffFile af, String operator, int number)
    {
        System.out.println("Negating values for Arff file: "+af.getPath()+" on"+operator+ " "+number);
        weka.core.converters.ArffLoader loader = new weka.core.converters.ArffLoader();
        weka.core.converters.ArffSaver saver = new weka.core.converters.ArffSaver();
        try{
            FileInputStream fis = new FileInputStream(af);
            loader.setSource(fis);
            Instances structure = loader.getStructure();
            Instances instances = loader.getDataSet();
            if (operator.equals("inst"))
            {
                Instance specified = instances.instance(number-1);
                for (int z=0; z<specified.numAttributes();z++)
                {
                    specified.setValue(z, -specified.value(z));
                }
            }
            else if (operator.equals("attr"))
            {
                for (int z=0; z<instances.numInstances();z++)
                {
                    Instance specified = instances.instance(z);
                    specified.setValue(number-1, 0-(specified.value(number-1)));
                }
            }
            System.out.println("Finished negating "+operator+ " "+number+" for: "+af.getName());
            
            saver.setStructure(structure);
            saver.setInstances(instances);

            loader.reset();
            fis.close();
            
            saver.setFile(af);
            saver.writeBatch();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    // create copy of the original file then inverse the order of the data instances
    public static void inverse(Object o)
    {
        ((Variable)o).inverse();
    }
    
    public static void inverse(ArffFile af)
    {
        System.out.println("Inverting Arff file: "+af.getPath());
        weka.core.converters.ArffLoader loader = new weka.core.converters.ArffLoader();
        weka.core.converters.ArffSaver saver = new weka.core.converters.ArffSaver();
        try{
            FileInputStream fis = new FileInputStream(af);
            //System.out.println();
            loader.setSource(fis);
            Instances structure = loader.getStructure();
            Instances instances = loader.getDataSet();
            
            Instances inst2 = new Instances(structure, instances.numInstances());
            for (int i=instances.numInstances()-1; i>=0; i--)
            {
                inst2.add(instances.instance(i));
            }
            
            System.out.println("Finished inverting instances object for: "+af.getName());
            
            saver.setStructure(structure);
            saver.setInstances(inst2);

            loader.reset();
            fis.close();
            
            saver.setFile(af);
            saver.writeBatch();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    
    public static Input clone(Input in, int offset)
    {
        File branchPath = new File("./tmp/branch"+offset);
        branchPath.mkdirs();
        Input newIn = new Input();
        
        for (int i=0; i<in.size(); i++)
        {
            Object var = in.get(i);
            if (var instanceof org.columbia.seas.cs.amsterdam.ArffFile )               
            {
                String path = ((org.columbia.seas.cs.amsterdam.ArffFile)var).getPath();

                try {
                
                    org.columbia.seas.cs.amsterdam.ArffFile newFile 
                        = new org.columbia.seas.cs.amsterdam.ArffFile( "./tmp/branch"+offset+"/"+path);


                    FileReader fileIn = new FileReader((org.columbia.seas.cs.amsterdam.ArffFile)var);
                    FileWriter fileOut = new FileWriter(newFile);
                    int c;

                    while ((c = fileIn.read()) != -1)
                    fileOut.write(c);

                    fileIn.close();
                    fileOut.close();   
                
                    newIn.add(newFile);
                        
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (var instanceof org.columbia.seas.cs.amsterdam.ModelFile )               
            {
                String path = ((org.columbia.seas.cs.amsterdam.ModelFile)var).getPath();

                try {
                
                    org.columbia.seas.cs.amsterdam.ModelFile newFile 
                        = new org.columbia.seas.cs.amsterdam.ModelFile( "./tmp/branch"+offset+"/"+path);


                    FileReader fileIn = new FileReader((org.columbia.seas.cs.amsterdam.ArffFile)var);
                    FileWriter fileOut = new FileWriter(newFile);
                    int c;

                    while ((c = fileIn.read()) != -1)
                    fileOut.write(c);

                    fileIn.close();
                    fileOut.close();   
                
                    newIn.add(newFile);
                        
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else{
                newIn.add(var);
            }
        }
        return newIn;
    }
    
    public static String processParam(ArrayList in, int offset, String paramStr)
    {
        String newStr = new String(paramStr);
        for (int i=0; i<in.size(); i++)
        {
            Object var = in.get(i);
            if (var instanceof File )               
            {
                String path = ((File)var).getPath();
                //System.out.println("path:"+path);
                //System.out.println("old string:"+newStr);
                newStr = newStr.replaceAll(path, "./tmp/branch"+offset+"/"+path);
                //System.out.println("new string:"+newStr);
            }
        }
        //newStr = newStr.replaceAll("\"", "\\\\"+"\"");
        return newStr;

    }
    
    public static String processParam(ArrayList in, ArrayList out, int offset, String paramStr)
    {
        String str = "";
        str = processParam(in, offset, paramStr);
        str = processParam(out, offset, str);
        return str;
    }
    
    
    
    public static Output clone(Output out, int offset)
    {
        File branchPath = new File("./tmp/branch"+offset);
        branchPath.mkdirs();
        Output newOut = new Output();
        for (int i=0; i<out.size(); i++)
        {
            
            Object var = out.get(i);
            //System.out.println(var instanceof org.columbia.seas.cs.amsterdam.ModelFile);
            if (var instanceof org.columbia.seas.cs.amsterdam.ArffFile )               
            {
                String path = ((org.columbia.seas.cs.amsterdam.ArffFile)var).getPath();

                try {
                
                    org.columbia.seas.cs.amsterdam.ArffFile newFile 
                        = new org.columbia.seas.cs.amsterdam.ArffFile( "./tmp/branch"+offset+"/"+path);       
                    newOut.add(newFile);
                        
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (var instanceof org.columbia.seas.cs.amsterdam.ModelFile )               
            {
                String path = ((org.columbia.seas.cs.amsterdam.ModelFile)var).getPath();

                try {
                
                    org.columbia.seas.cs.amsterdam.ModelFile newFile 
                        = new org.columbia.seas.cs.amsterdam.ModelFile( "./tmp/branch"+offset+"/"+path);       
                    newOut.add(newFile);
                        
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else{
                newOut.add(var);
            }
        }
        return newOut;
    }
    
    public static int numAttributes(Object o)
    {
        int num = 0;
        if (o instanceof org.columbia.seas.cs.amsterdam.ArffFile )
        {
            weka.core.converters.ArffLoader loader = new weka.core.converters.ArffLoader();
            try{
                loader.setFile((File)o);
                Instances instances = loader.getDataSet();
                num = instances.numAttributes();
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return num;
    }
    
    public static int numInstances(Object o)
    {
        int num = 0;
        if (o instanceof org.columbia.seas.cs.amsterdam.ArffFile )
        {
            weka.core.converters.ArffLoader loader = new weka.core.converters.ArffLoader();
            try{
                loader.setFile((File)o);
                Instances instances = loader.getDataSet();
                num = instances.numInstances();
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return num;
    }
    
    
}
