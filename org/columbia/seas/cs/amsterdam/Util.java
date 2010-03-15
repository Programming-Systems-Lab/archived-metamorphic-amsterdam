/*
 * Util.java
 *
 * Created on October 29, 2008, 8:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.columbia.seas.cs.amsterdam;

import com.martiansoftware.jsap.*;
import java.lang.management.*;

/**
 *
 * @author Kuang Shen
 */
public class Util {
    
    /** Creates a new instance of Util */
    public Util() {
    }
    
    public static String processVar(String typeString, String nameString, String value)
    {
        String content = "";
        if (typeString.equals("ARFF_FILENAME"))
        {
            content += "\tArffFile " + nameString + " = new ArffFile(" + value + ");\n";
        }
        if (typeString.equals("MODEL_FILENAME"))
        {
            content += "\tModelFile " + nameString + " = new ModelFile(" + value + ");\n";
        }
        if (typeString.equals("RESULT_SET"))
        {
            content += "\tResultSet " + nameString + " = " + value + ";\n";
        }
        
        content = processKeywords(content);
        
        return content;
    }
    
    public static String paramToArray(String parameters)
    {
        
        String[] raw = CommandLineTokenizer.tokenize(parameters);
        String complete = "{\"\",";

        if (raw.length>=2)
        {
            for (int i=0; i<raw.length-1; i++)
            {
                complete += "\""+raw[i]+"\",";
            }
            complete += "\""+raw[raw.length-1]+"\"}";
        }
        else
        {
            complete += "\""+raw[raw.length-1]+"\"}";
        }
        return complete;
    }
    
    public static String processKeywords(String line)
    {
        line = line.replaceAll("@param", "param");    
        line = line.replaceAll("@branch1", "output");
        line = line.replaceAll("@branch", "output");
        line = line.replaceAll("@input", "input");
        line = line.replaceAll("@output", "output");
        line = line.replaceAll("@op_", "DataProcessor.");
        line = line.replaceAll("@equal", "DataProcessor.equal");
        return line;
    }
    
    public static String processKeywordsBranch(String line, int bn)
    {
        line = line.replaceAll("@param", "param");    
        line = line.replaceAll("@branch1", "output");
        line = line.replaceAll("@branch", "output");
        line = line.replaceAll("@input", "input"+bn);
        line = line.replaceAll("@output", "output"+bn);
        line = line.replaceAll("@op_", "DataProcessor.");
        line = line.replaceAll("@equal", "DataProcessor.equal");
        return line;
    }
    
    public static String escapeChars(String str)
    {
        str = str.replaceAll("\\\\", "\\\\"+"\\\\");
        str = str.replaceAll("\"","\\\\"+"\"");
        return str;
    }
    
        public static long getCpuTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
        bean.getCurrentThreadCpuTime( ) : 0L;
        }
 
    /** Get user time in nanoseconds. */
    public static long getUserTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
        bean.getCurrentThreadUserTime( ) : 0L;
    }

    /** Get system time in nanoseconds. */
    public static long getSystemTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
    
        return bean.isCurrentThreadCpuTimeSupported( ) ?
        ( bean.getCurrentThreadCpuTime() - bean.getCurrentThreadUserTime() ) : 0L;
    }
    
}
