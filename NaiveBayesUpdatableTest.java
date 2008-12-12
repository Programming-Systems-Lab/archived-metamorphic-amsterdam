import java.io.*;
import weka.classifiers.bayes.*;

import weka.core.*;
import weka.classifiers.meta.FilteredClassifier;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.core.converters.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kane Shen
 */
public class NaiveBayesUpdatableTest {
    
    public static void main (String[] args)
    {
        try{

         weka.core.converters.ArffLoader trainReader = new weka.core.converters.ArffLoader();
         trainReader.setFile(new File(args[1]));
         weka.core.converters.ArffLoader classifyReader = new weka.core.converters.ArffLoader();
         classifyReader.setFile(new File(args[3]));

         Instances trainInsts = trainReader.getDataSet();
         Instances classifyInsts = classifyReader.getDataSet();
         Instances classifyStruct = classifyReader.getStructure();
        

        trainInsts.setClassIndex(trainInsts.numAttributes() - 1); 
        classifyInsts.setClassIndex(classifyInsts.numAttributes() - 1); 


        NaiveBayesUpdateable model = new NaiveBayesUpdateable();
        model.buildClassifier(trainInsts);
       
      
       
        boolean flag = true;
        for (int i = 0; i < classifyInsts.numInstances(); i++) {
            double cls = model.classifyInstance(classifyInsts.instance(i));

            classifyInsts.instance(i).setClassValue(cls);
       }
        
        weka.core.converters.ArffSaver saver = new weka.core.converters.ArffSaver();

        File outputFile = new File(args[5]);
        outputFile.createNewFile();
        saver.setFile(outputFile);
        saver.setStructure(classifyStruct);
        saver.setInstances(classifyInsts);
        System.out.println("Preparing to write to output file: "+outputFile.getPath());
        saver.writeBatch();
            

    }
    catch(FileNotFoundException e){
      
      System.err.println("File not found:" + e.getMessage());
    }
    catch(IOException i){
            
      System.err.println("IO Exception:"+i.getMessage());
    }
     catch(Exception o){
            
      System.err.println("General Exception: " + o.getMessage());
      o.printStackTrace();
     }

    }
    

}
