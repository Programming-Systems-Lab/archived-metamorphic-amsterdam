/*
 * Output.java
 *
 * Created on October 29, 2008, 11:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.columbia.seas.cs.amsterdam;

/**
 *
 * @author Kuang Shen
 */
public class Output extends java.util.ArrayList {
    
    /** Creates a new instance of Output */
    public Output() {
        super();
    }
    
    public Object var(int i) {
        return get(i-1);
    }
}
