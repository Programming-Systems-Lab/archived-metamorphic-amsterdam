/*
 * ArffFile.java
 *
 * Created on October 29, 2008, 11:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.columbia.seas.cs.amsterdam;

import java.io.*;
import weka.core.*;

/**
 *
 * @author Kuang Shen
 */
public class ArffFile extends File implements Variable{
    
    /** Creates a new instance of ArffFile */
    public ArffFile(String fileName) {
        super(fileName);
    }
    
    public Variable add(int colNumber, int rowNumber, double val)
    {
        weka.core.converters.ArffLoader loader = new weka.core.converters.ArffLoader();
        weka.core.converters.ArffSaver saver = new weka.core.converters.ArffSaver();
        try{
            FileInputStream fis = new FileInputStream(this);
            loader.setSource(fis);
            Instances structure = loader.getStructure();
            Instances instances = loader.getDataSet();
            
            // perform addition to the values of all attributes at the speicified row
            if (colNumber == 0)
            {
                int nAttr = instances.numAttributes();
                Instance instance = instances.instance(rowNumber-1);
                for (int i = 0; i < nAttr; i++)
                {
                    instance.setValue(i, instance.value(i)+val );
                }
            }
            
            // peform addition to the values of all rows on the specified attribute
            else if (rowNumber == 0)
            {
                int nInst = instances.numInstances();
                for (int i = 0; i < nInst; i++)
                {
                    Instance instance = instances.instance(i);
                    instance.setValue(colNumber-1, instance.value(colNumber-1)+val );
                }
            }
            
            // perform addition to a single cell with specified row and column number
            else
            {
                Instance instance = instances.instance(rowNumber-1);
                instance.setValue(colNumber-1, instance.value(colNumber-1)+val );
            }
            
            saver.setStructure(structure);
            saver.setInstances(instances);

            loader.reset();
            fis.close();
            
            saver.setFile(this);
            saver.writeBatch();
            
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }
    
    public Variable multiply(int colNumber, int rowNumber, double val)
    {
        weka.core.converters.ArffLoader loader = new weka.core.converters.ArffLoader();
        weka.core.converters.ArffSaver saver = new weka.core.converters.ArffSaver();
        try{
            FileInputStream fis = new FileInputStream(this);
            loader.setSource(fis);
            Instances structure = loader.getStructure();
            Instances instances = loader.getDataSet();
            
            // perform multiplication to the values of all attributes at the speicified row
            if (colNumber == 0)
            {
                int nAttr = instances.numAttributes();
                Instance instance = instances.instance(rowNumber-1);
                for (int i = 0; i < nAttr; i++)
                {
                    instance.setValue(i, instance.value(i)*val );
                }
            }
            
            // peform multiplication to the values of all rows on the specified attribute
            else if (rowNumber == 0)
            {
                int nInst = instances.numInstances();
                for (int i = 0; i < nInst; i++)
                {
                    Instance instance = instances.instance(i);
                    instance.setValue(colNumber-1, (instance.value(colNumber-1))*val );
                }
            }
            
            // perform multiplication to a single cell with specified row and column number
            else
            {
                Instance instance = instances.instance(rowNumber-1);
                instance.setValue(colNumber-1, (instance.value(colNumber-1))*val );
            }
            
            saver.setStructure(structure);
            saver.setInstances(instances);

            loader.reset();
            fis.close();
            
            saver.setFile(this);
            saver.writeBatch();
            
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }
    
    public Variable permute()
    {
        System.out.println("Permuting Arff file: "+this.getPath());
        weka.core.converters.ArffLoader loader = new weka.core.converters.ArffLoader();
        weka.core.converters.ArffSaver saver = new weka.core.converters.ArffSaver();
        try{
            FileInputStream fis = new FileInputStream(this);
            loader.setSource(fis);
            Instances structure = loader.getStructure();
            Instances instances = loader.getDataSet();
            instances.randomize(new java.util.Random());
            
            saver.setStructure(structure);
            saver.setInstances(instances);

            loader.reset();
            fis.close();
            
            saver.setFile(this);
            saver.writeBatch();
            System.out.println("Finished permuteing instances object for: "+this.getName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }
    
    public Variable inverse()
    {
        System.out.println("Inverting Arff file: "+this.getPath());
        weka.core.converters.ArffLoader loader = new weka.core.converters.ArffLoader();
        weka.core.converters.ArffSaver saver = new weka.core.converters.ArffSaver();
        try{
            FileInputStream fis = new FileInputStream(this);
            loader.setSource(fis);
            Instances structure = loader.getStructure();
            Instances instances = loader.getDataSet();
            
            Instances inst2 = new Instances(structure, instances.numInstances());
            for (int i=instances.numInstances()-1; i>=0; i--)
            {
                inst2.add(instances.instance(i));
            }
            
            saver.setStructure(structure);
            saver.setInstances(inst2);

            loader.reset();
            fis.close();
            
            saver.setFile(this);
            saver.writeBatch();
            System.out.println("Finished inverting instances object for: "+this.getName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }
    
    public Variable negate(int colNumber, int rowNumber)
    {
        System.out.println("Negating values for Arff file: "+this.getPath()+" on column "+colNumber+" and row "+rowNumber);
        weka.core.converters.ArffLoader loader = new weka.core.converters.ArffLoader();
        weka.core.converters.ArffSaver saver = new weka.core.converters.ArffSaver();
        try{
            FileInputStream fis = new FileInputStream(this);
            loader.setSource(fis);
            Instances structure = loader.getStructure();
            Instances instances = loader.getDataSet();
            
            multiply(colNumber, rowNumber, -1);
            
            saver.setStructure(structure);
            saver.setInstances(instances);

            loader.reset();
            fis.close();
            
            saver.setFile(this);
            saver.writeBatch();
            System.out.println("Finished negating "+this.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }
    
    public Variable sort(int attributeToSortBy)
    {
        System.out.println("sorting values for Arff file: "+this.getPath()+" on attribute "+attributeToSortBy);
        weka.core.converters.ArffLoader loader = new weka.core.converters.ArffLoader();
        weka.core.converters.ArffSaver saver = new weka.core.converters.ArffSaver();
        try{
            FileInputStream fis = new FileInputStream(this);
            loader.setSource(fis);
            Instances structure = loader.getStructure();
            Instances instances = loader.getDataSet();
            
            instances.sort(attributeToSortBy-1);
            
            saver.setStructure(structure);
            saver.setInstances(instances);

            loader.reset();
            fis.close();
            
            saver.setFile(this);
            saver.writeBatch();
            System.out.println("Finished sorting "+this.getName()+" on attribute "+attributeToSortBy);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }
    
    public Variable exclude(int rowNumber)
    {
        System.out.println("Excluding row " + rowNumber + " from Arff file: "+this.getPath());
        weka.core.converters.ArffLoader loader = new weka.core.converters.ArffLoader();
        weka.core.converters.ArffSaver saver = new weka.core.converters.ArffSaver();
        try{
            FileInputStream fis = new FileInputStream(this);
            loader.setSource(fis);
            Instances structure = loader.getStructure();
            Instances instances = loader.getDataSet();
            
            instances.delete(rowNumber-1);
            
            saver.setStructure(structure);
            saver.setInstances(instances);

            loader.reset();
            fis.close();
            
            saver.setFile(this);
            saver.writeBatch();
            System.out.println("Finished excluding row "+ rowNumber +" "+ this.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }
    
    public Variable randomSubset(int size)
    {
        System.out.println("Taking random subset with the size of " + size + " from Arff file: "+this.getPath());
        weka.core.converters.ArffLoader loader = new weka.core.converters.ArffLoader();
        weka.core.converters.ArffSaver saver = new weka.core.converters.ArffSaver();
        try{
            FileInputStream fis = new FileInputStream(this);
            loader.setSource(fis);
            Instances structure = loader.getStructure();
            Instances instances = loader.getDataSet();
            
            if (size < instances.numInstances())
            {
                instances.randomize(new java.util.Random());
            
                saver.setStructure(structure);
                saver.setInstances(instances);

                loader.reset();
                fis.close();
            
                saver.setFile(this);
                saver.writeBatch();
            }
            
            System.out.println("Finished taking random subset from Arff file"+ this.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }
    
    public boolean equal(Variable var)
    {
        boolean flag = true;
        System.out.println("checking outputs consistency");
        weka.core.converters.ArffLoader loader1 = new weka.core.converters.ArffLoader();
        weka.core.converters.ArffLoader loader2 = new weka.core.converters.ArffLoader();
        try{
                System.out.println("loading file:"+this.getAbsolutePath());
                loader1.setFile(this);
                System.out.println("loading file:"+((ArffFile)var).getAbsolutePath());
                loader2.setFile((ArffFile)var);
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
        return flag;
    }
    
    public boolean similar(Variable var, double offset)
    {
        boolean flag = true;
        System.out.println("checking outputs consistency");
        weka.core.converters.ArffLoader loader1 = new weka.core.converters.ArffLoader();
        weka.core.converters.ArffLoader loader2 = new weka.core.converters.ArffLoader();
        try{
                System.out.println("loading file:"+this.getAbsolutePath());
                loader1.setFile(this);
                System.out.println("loading file:"+((ArffFile)var).getAbsolutePath());
                int numberOfInconsistency = 0;
                loader2.setFile((ArffFile)var);
                Instances structure1 = loader1.getStructure();
                Instances instances1 = loader1.getDataSet();
                instances1.setClassIndex(instances1.numAttributes() - 1);
                Instances structure2 = loader2.getStructure();
                Instances instances2 = loader2.getDataSet();
                instances2.setClassIndex(instances2.numAttributes() - 1);
                        
                if (instances1.numInstances()!=instances1.numInstances())
                    flag = false;
                else
                {
                    for (int z=0; z<instances1.numInstances(); z++)
                    {
                        if (instances1.instance(z).classValue()!=instances2.instance(z).classValue())
                            numberOfInconsistency++;
                    }
                    if (numberOfInconsistency / instances1.numInstances() > offset)
                        flag = false;
                }        
                    System.out.println("checking result: "+flag);
           } catch (Exception ex) {
                ex.printStackTrace();
           }
        return flag;
    }
    
}
