/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package measures;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import matrix.pointsmatrix.AbstractMatrix;
import util.Utils;

/**
 *
 * @author aurea
 */
public class Kendall implements Measure {

    Object[][] tableData;
    int valuesCount;
    ArrayList<String> attributes;
    ArrayList<String> selectedAttributes;
    AbstractMatrix pointsMatrix;

    @Override
    public Object[][] calculateRanking(ArrayList<String> attributes, ArrayList<String> selectedAttributes, AbstractMatrix pointsMatrix) {
        valuesCount = selectedAttributes.size();
        tableData = new Object[valuesCount][2];

        for (int i = 0; i < valuesCount; i++) {
            tableData[i][0] = selectedAttributes.get(i);
            double[] values = Utils.getVectorDataByAttribute(selectedAttributes.get(i), attributes, pointsMatrix);
            double value = Utils.kendall(values, values);
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("0.000", dfs);
            String valueString = df.format(value);
            tableData[i][1] = Double.valueOf(valueString);

        }
        return tableData;
    }

    @Override
    public Object[][] calculateMatrix(ArrayList<String> attributes, ArrayList<String> selectedAttributes, AbstractMatrix pointsMatrix, Integer numberThreads) {
        valuesCount = selectedAttributes.size() + 1;
        tableData = new Object[valuesCount - 1][valuesCount];
        for (int i = 0; i < valuesCount - 1; i++) {
            tableData[i][0] = selectedAttributes.get(i);

        }
      /*  for (int i = 0; i < valuesCount - 1; i++) {
                double[] values1 = Utils.getVectorDataByAttribute(selectedAttributes.get(i), attributes, pointsMatrix);
            for (int j = 1; j < valuesCount; j++) {
                double[] values2 = Utils.getVectorDataByAttribute(selectedAttributes.get(j - 1), attributes, pointsMatrix);
                try{
                if (i != j - 1) {
                    double value = Utils.kendall(values1, values2);
                    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                    dfs.setDecimalSeparator('.');
                    DecimalFormat df = new DecimalFormat("0.000", dfs);
                    String valueString = df.format(value);
                    tableData[i][j] = Double.valueOf(valueString);
                } else {
                    tableData[i][j] = 1.0;
                }}
                catch(Exception e)
                {
                    tableData[i][j] = 0.0;
                }
            }
        }*/
      List<Thread> listThreads = new ArrayList<>();
        Integer beginPoint = 0;
        Integer endPoint = 0;
      

        if ((valuesCount - 1) < numberThreads) {
            numberThreads = (valuesCount - 1);
        }

        int totalValues = 0;
        for (int i = 0; i < (valuesCount - 1); i++) {
            totalValues += ((valuesCount - 1) - (i + 1));

        }
        int step = totalValues / numberThreads;
        System.out.println("step: " + step);

        for (int i = 1; i <= numberThreads; i++) {
            beginPoint = endPoint + 1;

            int count = 0;
            int tempTotalValues = 0;
            for (int j = (beginPoint); j <= (valuesCount - 1); j++) {
                tempTotalValues += ((valuesCount - 1) - (j));

                //System.out.println(tempTotalValues + " " + count);
                if (tempTotalValues >= step || (beginPoint + count) >= (valuesCount - 1)) {
                    endPoint = beginPoint + count;
                    //System.out.println("endPoint: " +endPoint);
                    if (endPoint >= (valuesCount - 1)) {
                        //System.out.println("entra");
                        endPoint = valuesCount - 1;
                        i = numberThreads + 1;
                    }
                    break;
                }

                count++;
            }
            // endPoint = i * (valuesCount - 1) / (numberThreads);
            System.out.println("#" + i + ": " + (beginPoint) + " - " + (endPoint));
            listThreads.add(new KendallThread(beginPoint - 1, endPoint - 1, i));
            listThreads.get(listThreads.size() - 1).start();

        }

        for (Thread listThread : listThreads) {
            try {
                listThread.join();
            } catch (InterruptedException ex) {
                System.out.print("Join interrupted\n");
            }
        }
        return tableData;
    }

    public void calculateKendallThreads(Integer beginPoint, Integer endPoint, Integer numberThread) {
        for (int i = beginPoint; i <= endPoint; i++) {
            double[] values1 = Utils.getVectorDataByAttribute(selectedAttributes.get(i), attributes, pointsMatrix);
            tableData[i][i + 1] = 1.0;

            for (int j = i + 1; j < (valuesCount - 1); j++) {

                double[] values2 = Utils.getVectorDataByAttribute(selectedAttributes.get(j), attributes, pointsMatrix);
                try {

                    if (i != j) {
                        double value = Utils.kendall(values1, values2);
                        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                        dfs.setDecimalSeparator('.');
                        DecimalFormat df = new DecimalFormat("0.000", dfs);
                        String valueString = df.format(value);

                        tableData[i][j + 1] = Double.valueOf(valueString);
                        tableData[j][i + 1] = Double.valueOf(valueString);

                    }

                } catch (Exception e) {

                    tableData[i][j + 1] = 0.0;

                }

            }

        }
        System.out.println("#" + numberThread + ": " + (beginPoint) + " - " + (endPoint) + " finished.");

    }

    class KendallThread extends Thread {

        Integer beginPoint;
        Integer endPoint;
        Integer numberThread;

        public KendallThread(Integer beginPoint, Integer endPoint, Integer numberThread) {
            this.beginPoint = beginPoint;
            this.endPoint = endPoint;
            this.numberThread = numberThread;
        }

        @Override
        public void run() {
            calculateKendallThreads(this.beginPoint, this.endPoint, this.numberThread);
        }
    }
}
