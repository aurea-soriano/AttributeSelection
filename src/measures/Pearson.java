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
 * corr (x,y) = cov (x,y) / (std (x) * std (y))
 *
 * @author aurea
 */
public class Pearson implements Measure {

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
            double value = Utils.pearson(values, values);
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
        this.attributes = attributes;
        this.selectedAttributes = selectedAttributes;
        this.pointsMatrix = pointsMatrix;
        for (int i = 0; i < valuesCount - 1; i++) {
            tableData[i][0] = selectedAttributes.get(i);

        }
        /*for (int i = 0; i < valuesCount - 1; i++) {
         double[] values1 = Utils.getVectorDataByAttribute(selectedAttributes.get(i), attributes, pointsMatrix);
         for (int j = i + 1; j < valuesCount; j++) {

         double[] values2 = Utils.getVectorDataByAttribute(selectedAttributes.get(j - 1), attributes, pointsMatrix);
         try {
         if (i != j - 1) {
         double value = Utils.pearson(values1, values2);
         DecimalFormatSymbols dfs = new DecimalFormatSymbols();
         dfs.setDecimalSeparator('.');
         DecimalFormat df = new DecimalFormat("0.000", dfs);
         String valueString = df.format(value);
         tableData[i][j] = Double.valueOf(valueString);
         tableData[j - 1][i + 1] = Double.valueOf(valueString);
         } else {
         tableData[i][j] = 1.0;
         }
         } catch (Exception e) {
         tableData[i][j] = 0.0;
         }

         if (i % 3 == 0) {
         System.out.println(i + "- " + (j - 1));
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
        //System.out.println("step: " + step);

        for (int i = 1; i <= numberThreads; i++) {
            beginPoint = endPoint + 1;

            int count = 0;
            int tempTotalValues = 0;
            for (int j = (beginPoint); j <= (valuesCount - 1); j++) {
                tempTotalValues += ((valuesCount - 1) - (j));

                if (tempTotalValues >= step || (beginPoint + count) >= (valuesCount - 1)) {
                    endPoint = beginPoint + count;
                    if (endPoint >= (valuesCount - 1)) {
                        endPoint = valuesCount - 1;
                        i = numberThreads + 1;
                    }
                    break;
                }

                count++;
            }
            // endPoint = i * (valuesCount - 1) / (numberThreads);
            System.out.println("#" + i + ": " + (beginPoint) + " - " + (endPoint));
            listThreads.add(new PearsonThread(beginPoint - 1, endPoint - 1, i));
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

    public void calculatePearsonThreads(Integer beginPoint, Integer endPoint, Integer numberThread) {
        for (int i = beginPoint; i <= endPoint; i++) {
            double[] values1 = Utils.getVectorDataByAttribute(selectedAttributes.get(i), attributes, pointsMatrix);
            double std1 = Utils.standarddeviation(values1);
            double mean1 = Utils.mean(values1);
            tableData[i][i + 1] = 1.0;

            for (int j = i + 1; j < (valuesCount - 1); j++) {

                double[] values2 = Utils.getVectorDataByAttribute(selectedAttributes.get(j), attributes, pointsMatrix);
                try {

                    if (i != j) {
                        double value = Utils.pearson(values1, values2, std1, mean1);
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

    class PearsonThread extends Thread {

        Integer beginPoint;
        Integer endPoint;
        Integer numberThread;

        public PearsonThread(Integer beginPoint, Integer endPoint, Integer numberThread) {
            this.beginPoint = beginPoint;
            this.endPoint = endPoint;
            this.numberThread = numberThread;
        }

        @Override
        public void run() {
            calculatePearsonThreads(this.beginPoint, this.endPoint, this.numberThread);
        }
    }
}
