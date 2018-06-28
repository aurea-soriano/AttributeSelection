/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import java.io.File;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.ImageIcon;
import matrix.pointsmatrix.DenseMatrix;
import matrix.pointsmatrix.DenseVector;
import matrix.pointsmatrix.AbstractMatrix;
import matrix.pointsmatrix.SparseMatrix;
import matrix.pointsmatrix.SparseVector;
import matrix.pointsmatrix.AbstractVector;

/* Utils.java is used by FileChooserDemo2.java. */
public class Utils {

    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     *
     * @param path
     * @return
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Utils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static Float minmax(Float maxValue1, Float minValue1, Float maxValue2, Float minValue2, Float value) {
        return (((value - minValue1) * (maxValue2 - minValue2)) / (maxValue1 - minValue1)) + minValue2;
    }

    public static Double minmax(Double maxValue1, Double minValue1, Double maxValue2, Double minValue2, Double value) {
        return (((value - minValue1) * (maxValue2 - minValue2)) / (maxValue1 - minValue1)) + minValue2;
    }

    public static Integer minmax(Integer maxValue1, Integer minValue1, Integer maxValue2, Integer minValue2, Integer value) {
        return (((value - minValue1) * (maxValue2 - minValue2)) / (maxValue1 - minValue1)) + minValue2;
    }

    public static ArrayList<Float> getDataByAttribute(String value, ArrayList<String> attributes, AbstractMatrix pointsMatrix) {
        ArrayList<Float> listValues = new ArrayList<>();
        int index = -1;
        for (int i = 0; i < attributes.size(); i++) {
            if (value.equals(attributes.get(i))) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            for (int i = 0; i < pointsMatrix.getRowCount(); i++) {
                listValues.add(pointsMatrix.getRow(i).getValue(index));

            }
        }
        return listValues;
    }

    public static double[] getVectorDataByAttribute(String value, ArrayList<String> attributes, AbstractMatrix pointsMatrix) {
        ArrayList<Float> listValues = getDataByAttribute(value, attributes, pointsMatrix);
        double[] values = new double[listValues.size()];
        for (int i = 0; i < listValues.size(); i++) {
            values[i] = listValues.get(i);
        }
        return values;

    }

    public static ArrayList<Float> getDataByAttributeByTimeVal(String value, ArrayList<String> attributes, AbstractMatrix pointsMatrix, String timeVal) {
        ArrayList<Float> listValues = new ArrayList<>();
        int index = -1;
        int indexAttributeTimeVal = -1;
        for (int i = 0; i < attributes.size(); i++) {
            if (value.equals(attributes.get(i))) {
                index = i;
                break;
            }
        }

        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).equals("time_val")) {
                indexAttributeTimeVal = i;
                break;
            }
        }

        if (index != -1) {
            for (int i = 0; i < pointsMatrix.getRowCount(); i++) {
                if (pointsMatrix.getRow(i).getValue(indexAttributeTimeVal) == Float.valueOf(timeVal)) {
                    listValues.add(pointsMatrix.getRow(i).getValue(index));
                }

            }
        }
        return listValues;
    }

    public static double[] getVectorDataByAttributeByTimeVal(String value, ArrayList<String> attributes, AbstractMatrix pointsMatrix, String timeVal) {
        ArrayList<Float> listValues = getDataByAttributeByTimeVal(value, attributes, pointsMatrix, timeVal);
        double[] values = new double[listValues.size()];
        for (int i = 0; i < listValues.size(); i++) {
            values[i] = listValues.get(i);
        }
        return values;

    }

    public static double[] getListValuesByAttribute(String value, int blocks, ArrayList<String> attributes, AbstractMatrix pointsMatrix) {
        ArrayList<Float> listValues = getDataByAttribute(value, attributes, pointsMatrix);
        Float maxValue = Collections.max(listValues);
        Float minValue = Collections.min(listValues);

        double[] listValuesHistogram = new double[blocks];
        for (int i = 0; i < blocks; i++) {
            listValuesHistogram[i] = 0;
        }
        for (Float listValue : listValues) {
            int index = Math.round(Utils.minmax(maxValue, minValue, (blocks - 1) * 1.f, 0.f, listValue));
            listValuesHistogram[index] += 1;
        }
        return listValuesHistogram;

    }
/*
    public static List<String> getClassesPackage(String packageName) {
        List<Class<?>> classes = ClassFinder.find(packageName);
        List<String> nameClasses = new ArrayList<>();
        for (Class<?> classe : classes) {
            nameClasses.add(classe.getSimpleName());
        }
        return nameClasses;
    }
*/
    public static double mean(double[] vector) {

        double sum = 0.0;
        for (int i = 0; i < vector.length; i++) {
            sum += vector[i];
        }
        return sum / vector.length;
    }

    public static float mean(float[] vector) {

        float sum = 0.f;
        for (int i = 0; i < vector.length; i++) {
            sum += vector[i];
        }
        return sum / vector.length;
    }

    /**
     * cov (x) = 1/N-1 * SUM_i (x(i) - mean(x)) * (y(i) - mean(y))
     *
     * @param vector1
     * @param vector2
     * @return
     */
    public static double covariance(double[] vector1, double[] vector2) {

        double cov = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            cov += (vector1[i] - mean(vector1)) * (vector2[i] - mean(vector2));
        }
        return cov / (vector1.length - 1);
    }

    /**
     * cov (x) = 1/N-1 * SUM_i (x(i) - mean(x)) * (y(i) - mean(y))
     *
     * @param vector1
     * @param vector2
     * @param mean1
     * @return
     */
    public static double covariance(double[] vector1, double[] vector2, double mean1) {

        double cov = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            cov += (vector1[i] - mean1) * (vector2[i] - mean(vector2));
        }
        return cov / (vector1.length - 1);
    }

    /**
     * sqrt ( 1/(N-1) SUM_i (x(i) - mean(x))^2 )
     *
     * @param vector1
     * @return
     */
    public static double standarddeviation(double[] vector1) {
        double mean = mean(vector1);
        double std = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            std += Math.pow(vector1[i] - mean, 2);
        }
        return Math.sqrt(std / (vector1.length - 1));

    }

    /**
     * sqrt ( 1/(N-1) SUM_i (x(i) - mean(x))^2 )
     *
     * @param vector1
     * @return
     */
    public static float standarddeviation(float[] vector1) {
        float mean = mean(vector1);
        float std = 0.f;
        for (int i = 0; i < vector1.length; i++) {
            std += Math.pow(vector1[i] - mean, 2);
        }
        return (float) Math.sqrt(std / (vector1.length - 1));

    }

    /**
     * corr (x,y) = cov (x,y) / (std (x) * std (y))
     *
     * @param vector1
     * @param vector2
     * @return
     */
    public static double pearson(double[] vector1, double[] vector2) {
        return covariance(vector1, vector2) / (standarddeviation(vector1) * standarddeviation(vector2));

    }

    /**
     * corr (x,y) = cov (x,y) / (std (x) * std (y))
     *
     * @param vector1
     * @param vector2
     * @param std1
     * @param mean1
     * @return
     */
    public static double pearson(double[] vector1, double[] vector2, double std1, double mean1) {
        return covariance(vector1, vector2, mean1) / (std1 * standarddeviation(vector2));

    }

    public static double averageAbsoluteDeviation(double[] vector1) {
        double mean = Utils.mean(vector1);
        double std = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            std += Math.abs(vector1[i] - mean);
        }
        return std / vector1.length;

    }

    /**
     *
     * @param vector
     * @return
     */
    public static double median(double[] vector) {
        Arrays.sort(vector);
        int index = 0;
        if (vector.length % 2 == 0) {
            index = vector.length / 2;
        } else {
            index = (vector.length + 1) / 2;
        }

        return vector[index];

    }

    /**
     * -1, x < 0; sign (x) = 0, x = 0; 1, x > 0.
     *
     * @param value
     * @return
     */
    public static double sign(double value) {
        if (value > 0) {
            return 1;
        } else if (value == 0) {
            return 0;
        } else {
            return -1;
        }

    }

    /**
     * signiﬁca simetria, caso contrário, uma tendência à esquerda para números
     * negativos e, à direita para números positivos.
     *
     * @param vector
     * @return
     */
    public static double skewness(double[] vector) {
        double mean = Utils.mean(vector);
        double median = Utils.median(vector);
        double standardDeviation = Utils.standarddeviation(vector);
        return (3 * (mean - median)) / standardDeviation;
    }

    public static double max(double[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }

        // Finds and returns max
        double max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Double.isNaN(array[j])) {
                return Double.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }

    public static double min(double[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }

        // Finds and returns max
        double min = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Double.isNaN(array[j])) {
                return Double.NaN;
            }
            if (array[j] < min) {
                min = array[j];
            }
        }

        return min;
    }

    public static double amplitude(double[] vector) {
        double max = Utils.max(vector);
        double min = Utils.min(vector);
        return max - min;
    }

    public static Integer max(Integer number1, Integer number2) {
        if (number1 >= number2) {
            return number1;
        }
        return number2;
    }

    public static double statisticalOrder(double[] orderedValues, double value) {

        int sumIndex = 0;
        int countIndex = 0;
        for (int i = 0; i < orderedValues.length; i++) {
            if (value == orderedValues[i]) {
                sumIndex = sumIndex + i + 1;
                countIndex++;
            } else {
                if (countIndex > 0) {
                    return (double) sumIndex / (double) countIndex;
                }
            }

        }
        return (double) sumIndex / (double) countIndex;
    }

    /**
     * p= 1- (6 sum(D^2)) / N(N^2-1)
     *
     * @param values
     * @param values1
     * @return
     */
    public static double spearman(double[] values, double[] values1) {
        double[] orderedValues = values.clone();
        double[] orderedValues1 = values1.clone();
        java.util.Arrays.sort(orderedValues);
        java.util.Arrays.sort(orderedValues1);
        double spearman = 0;
        for (int i = 0; i < values.length; i++) {
            double d = Utils.statisticalOrder(orderedValues, values[i]) - Utils.statisticalOrder(orderedValues1, values1[i]);
            spearman += Math.pow(d, 2);
        }
        spearman = (spearman * 6) / (values.length * (Math.pow(values.length, 2) - 1));
        return 1 - spearman;
    }

    /**
     * 1
     * tau = ------- SUM sign (q(i) - q(j)) * sign (r(i) - r(j)) n (n-1) i,j
     *
     * @param values
     * @param values1
     * @return
     */
    public static double kendall(double[] values, double[] values1) {
        double[] orderedValues = values.clone();
        double[] orderedValues1 = values1.clone();
        java.util.Arrays.sort(orderedValues);
        java.util.Arrays.sort(orderedValues1);
        double kendall = 0;
        for (int i = 0; i < values.length; i++) {
            double st1 = Utils.statisticalOrder(orderedValues, values[i]);
            double st2 = Utils.statisticalOrder(orderedValues1, values1[i]);
            for (int j = 0; j < values.length; j++) {
                double term1 = st1 - Utils.statisticalOrder(orderedValues, values[j]);
                double term2 = st2 - Utils.statisticalOrder(orderedValues1, values1[j]);
                kendall += (Utils.sign(term1) * Utils.sign(term2));
            }
        }

        return kendall / (values.length * (values.length - 1));
    }

    public static double log(double x, int base) {
        return Math.log(x) / Math.log(base);
    }

    public static double decimals(double value, int decim) {
        return (double) Math.round(value * Math.pow(10, decim)) / Math.pow(10, decim);
    }

    /**
     * -sum (P .* log2 (P)
     *
     * @param values
     * @return
     */
    public static double entropy(double[] values) {
        double entr = 0.0;
        for (int i = 0; i < values.length; i++) {
            //entr = Utils.decimals(entr, 4);
            if (values[i] != 0) {
                entr += (values[i] * Utils.log(values[i], 2));
            }
        }
        entr *= (-1);
        return entr;
    }

    public static double probability(double value, double[] values) {
        int count = 0;
        for (int i = 0; i < values.length; i++) {
            if (value == values[i]) {
                count++;
            }
        }
        return Utils.decimals(count / values.length, 4);
    }

    public static double jointEntropy(double[] values, double[] values1) {
        double jointEntr = 0.0;
        for (int i = 0; i < values.length; i++) {
            double pr1 = probability(values[i], values);
            for (int j = 0; j < values.length; j++) {
                double pr2 = probability(values1[j], values1);
                jointEntr = Utils.decimals(jointEntr, 4);
                jointEntr += (pr1 * pr2) * log((pr1 * pr2), 2);
            }
        }
        return jointEntr * (-1);
    }

    public static double variance(double[] values) {
        double std = Utils.standarddeviation(values);
        return std * std;
    }

    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static float[][] getTranposeFloatMatrix(float[][] matrix) {
        float[][] transposedMatrix = new float[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                transposedMatrix[i][j] = matrix[j][i];
            }
        }
        return transposedMatrix;
    }

    public static float[][] getSumFloat2Matrix(float[][] matrix1, float[][] matrix2) {
        if ((matrix1.length != matrix2.length) || (matrix1[0].length != matrix2[0].length)) {
            throw new IllegalArgumentException("Different number of columns of rows.");
        }
        float[][] sumMatrix = new float[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                sumMatrix[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return sumMatrix;
    }

    public static float getSumFloat1Vector(float[] vector) {

        float sum = 0.f;
        for (int i = 0; i < vector.length; i++) {
            sum += vector[i];
        }
        return sum;
    }

    public static float[][] getMultiplicationFloat2Matrix(float[][] matrix1, float[][] matrix2) {
        if (matrix1[0].length != matrix2.length) {
            throw new IllegalArgumentException("Different number of columns of rows.");
        }
        float[][] multMatrix = new float[matrix1.length][matrix2[0].length];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                multMatrix[i][j] = 0.f;
            }

        }
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    multMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return multMatrix;

    }

    public static float getMultiplicationFloat2Vector(float[] vector1, float[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Different number of columns of rows.");
        }
        float multArray = 0.f;

        for (int i = 0; i < vector1.length; i++) {
            multArray += (vector1[i] * vector2[i]);
        }

        return multArray;

    }

    public static AbstractVector mean(AbstractMatrix matrix) {
        assert (matrix.getRowCount() > 0) : "More than zero vectors must be used!";

        if (matrix instanceof SparseMatrix) {
            float[] mean = new float[matrix.getDimensions()];
            Arrays.fill(mean, 0.0f);

            int size = matrix.getRowCount();
            for (int i = 0; i < size; i++) {
                int[] index = ((SparseVector) matrix.getRow(i)).getIndex();
                float[] values = matrix.getRow(i).getValues();

                for (int j = 0; j < index.length; j++) {
                    mean[index[j]] += values[j];
                }
            }

            for (int j = 0; j < mean.length; j++) {
                mean[j] /= size;
            }

            return new SparseVector(mean);

        } else if (matrix instanceof DenseMatrix) {
            float[] mean = new float[matrix.getDimensions()];
            Arrays.fill(mean, 0.0f);

            int size = matrix.getRowCount();
            for (int i = 0; i < size; i++) {
                float[] values = matrix.getRow(i).getValues();

                for (int j = 0; j < values.length; j++) {
                    mean[j] += values[j];
                }
            }

            for (int j = 0; j < mean.length; j++) {
                mean[j] /= size;
            }

            return new DenseVector(mean);
        }

        return null;

    }

}
