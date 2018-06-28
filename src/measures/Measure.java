/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package measures;

import java.util.ArrayList;
import matrix.pointsmatrix.AbstractMatrix;

/**
 *
 * @author aurea
 */
public interface Measure {
    
    public Object[][] calculateRanking(ArrayList<String> attributes,ArrayList<String> selectedAttributes, AbstractMatrix pointsMatrix);
    public Object[][] calculateMatrix(ArrayList<String> attributes,ArrayList<String> selectedAttributes, AbstractMatrix pointsMatrix,Integer numberThreads);

    
}
