/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import java.util.Comparator;

/**
 *
 * @author aurea
 */
public class FloatComparator implements Comparator<Object> {
  

    

    @Override
    public int compare(Object o1, Object o2) {
        return new Float(o1.toString()).compareTo(new Float(o2.toString()));
    }
}
