/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package measures;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import matrix.pointsmatrix.AbstractMatrix;
import util.Utils;

/**
 *
 * @author aurea
 */
public class AverageAbsoluteDeviation implements Measure {

    @Override
    public Object[][] calculateRanking(ArrayList<String> attributes, ArrayList<String> selectedAttributes, AbstractMatrix pointsMatrix) {
        int valuesCount = selectedAttributes.size();
        Object tableData[][] = new Object[valuesCount][2];

        for (int i = 0; i < valuesCount; i++) {
            tableData[i][0] = selectedAttributes.get(i);
            double[] values = Utils.getVectorDataByAttribute(selectedAttributes.get(i), attributes, pointsMatrix);
            double value = Utils.averageAbsoluteDeviation(values);
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("0.000", dfs);
            String valueString = df.format(value);
            tableData[i][1] = Double.valueOf(valueString);
        }
        return tableData;
    }

    @Override
    public Object[][] calculateMatrix(ArrayList<String> attributes, ArrayList<String> selectedAttributes, AbstractMatrix pointsMatrix,Integer numberThreads) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
