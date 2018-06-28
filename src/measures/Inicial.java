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
public class Inicial implements Measure {

    @Override
    public Object[][] calculateRanking(ArrayList<String> attributes, ArrayList<String> selectedAttributes, AbstractMatrix pointsMatrix) {
        int valuesCount = selectedAttributes.size();
        Object tableData[][] = new Object[valuesCount][2];

        for (int i = 0; i < valuesCount; i++) {
            tableData[i][0] = selectedAttributes.get(i);
            tableData[i][1] = "0.0";

        }
        return tableData;
    }

    @Override
    public Object[][] calculateMatrix(ArrayList<String> attributes, ArrayList<String> selectedAttributes, AbstractMatrix pointsMatrix, Integer numberThread) {
        int valuesCount = selectedAttributes.size() + 1;
        Object tableData[][] = new Object[valuesCount - 1][valuesCount];
        for (int i = 0; i < valuesCount - 1; i++) {
            tableData[i][0] = selectedAttributes.get(i);

        }
        for (int i = 0; i < valuesCount - 1; i++) {
            for (int j = 1; j < valuesCount; j++) {
                tableData[i][j] = 0;
            }
        }
        return tableData;
    }

}
