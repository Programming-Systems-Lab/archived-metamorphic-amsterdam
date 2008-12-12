/*
 * Input.java
 *
 * Created on October 29, 2008, 11:17 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.columbia.seas.cs.amsterdam;

/**
 *
 * @author Kuang Shen
 */
public class Input extends java.util.ArrayList{
    
    /** Creates a new instance of Input */
    public Input() {
        super();
    }
    
    public Object var(int i)
    {
        return get(i-1);
    }
    
}
