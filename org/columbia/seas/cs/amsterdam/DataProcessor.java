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
                if ( (var1 instanceof org.columbia.seas.cs.amsterdam.ArffFile) && (var2 instanceof org.columbia.seas.cs.amsterdam.ArffFile))
                {
                    System.out.println("checking outputs consistency");
                    weka.core.converters.ArffLoader loader1 = new weka.core.converters.ArffLoader();
                    weka.core.converters.ArffLoader loader2 = new weka.core.converters.ArffLoader();
                    try{
                        System.out.println("loading file:"+((File)var1).getAbsolutePath());
                        loader1.setFile((File)var1);
                        System.out.println("loading file:"+((File)var2).getAbsolutePath());
                        loader2.setFile((File)var2);
                        Instances structure1 = loader1.getStructure();
                        Instances instances1 = loader1.getDataSet();
                        instances1.setClassIndex(instances1.numAttributes() - 1);
                        Instances structure2 = loader2.getStructure();
                        Instances instances2 = loader2.getDataSet();
                        instances2.setClassIndex(instances2.numAttributes() - 1);
                        
                        if (instances1.numInstances()!=instances1.numInstances())
                            flag = false;
                        for (int z=0; z<instances1.numInstances(); z++)
                        {
                            if (instances1.instance(z).classValue()!=instances2.instance(z).classValue())
                                return false;
                        }
                        
                        System.out.println("checking result: "+flag);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
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
                if ( (var1 instanceof org.columbia.seas.cs.amsterdam.ArffFile) && (var2 instanceof org.columbia.seas.cs.amsterdam.ArffFile))
                {
                    System.out.println("checking outputs consistency");
                    weka.core.converters.ArffLoader loader1 = new weka.core.converters.ArffLoader();
                    weka.core.converters.ArffLoader loader2 = new weka.core.converters.ArffLoader();
                    try{
                        loader1.setFile((File)var1);
                        loader2.setFile((File)var2);
                        Instances structure1 = loader1.getStructure();
                        Instances instances1 = loader1.getDataSet();
                        Instances structure2 = loader2.getStructure();
                        Instances instances2 = loader2.getDataSet();
                        
                        if (instances1.numInstances()!=instances1.numInstances())
                            flag = false;
                        System.out.println("checking result: "+flag);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
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
        if (o instanceof org.columbia.seas.cs.amsterdam.ArffFile)
        {
            permute((ArffFile)o);
        }
        if (o instanceof org.columbia.seas.cs.amsterdam.DBResultSet)
        {
            permute((DBResultSet)o);
        }
    }

    // create copy of the original file then negate the value of the data instances's column or row
    public static void negate(Object o, String operator, int number)
    {
        System.out.println("Negating values  ");
        if (o instanceof org.columbia.seas.cs.amsterdam.ArffFile)
        {
            negate((ArffFile)o, operator, number);
        }
        if (o instanceof org.columbia.seas.cs.amsterdam.DBResultSet)
        {
            //permute((DBResultSet)o);
        }
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
        if (o instanceof org.columbia.seas.cs.amsterdam.ArffFile)
        {
            inverse((ArffFile)o);
        }
        if (o instanceof org.columbia.seas.cs.amsterdam.DBResultSet)
        {
            //inverse((DBResultSet)o);
        }
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
            else{
                newIn.add(var);
            }
        }
        return newIn;
    }
    
    public static String processParam(ArrayList in, int offset, String paramStr)
    {
        String newStr = paramStr;
        for (int i=0; i<in.size(); i++)
        {
            Object var = in.get(i);
            if (var instanceof org.columbia.seas.cs.amsterdam.ArffFile )               
            {
                String path = ((org.columbia.seas.cs.amsterdam.ArffFile)var).getPath();
                //System.out.println("old string:"+newStr);
                newStr = newStr.replaceAll(path, "./tmp/branch"+offset+"/"+path);
                //System.out.println("new string:"+newStr);
            }
        }
        //newStr = newStr.replaceAll("\"", "\\\\"+"\"");
        return newStr;

    }
    
    
    
    public static Output clone(Output out, int offset)
    {
        File branchPath = new File("./tmp/branch"+offset);
        branchPath.mkdirs();
        Output newOut = new Output();
        for (int i=0; i<out.size(); i++)
        {
            Object var = out.get(i);
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
