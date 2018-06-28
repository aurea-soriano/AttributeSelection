/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import matrix.pointsmatrix.AbstractMatrix;
import matrix.pointsmatrix.AbstractVector;
import matrix.pointsmatrix.DenseMatrix;
import matrix.pointsmatrix.DenseVector;
import matrix.pointsmatrix.SparseMatrix;

/**
 *
 * @author aurea
 */
public class StandarizationColumns {

    float[][] points = null;
    double[] mean;
    double[] deviation;
    int[] indices;
    Lock lock = new java.util.concurrent.locks.ReentrantLock();

    public AbstractMatrix standarizationColumns(AbstractMatrix matrix, int[] ind) {

        if (ind.length > 0 && matrix.getRowCount() > 0) {
            if (matrix instanceof DenseMatrix) {
                points = new float[matrix.getRowCount()][];

                for (int i = 0; i < points.length; i++) {
                    points[i] = matrix.getRow(i).getValues();
                    matrix.getRow(i).shouldUpdateNorm();
                }
            } else {
                points = matrix.toMatrix();
            }
            this.indices = ind;
            mean = new double[this.indices.length];
            Arrays.fill(mean, 0.0f);

            //calculating the standard deviation
            deviation = new double[this.indices.length];
            Arrays.fill(deviation, 0.0f);

            //calculating the mean
            for (float[] point : points) {
                //calculating
                for (int j = 0; j < indices.length; j++) {
                    //for (int j = 0; j < points[i].length; j++) {
                    mean[j] += point[indices[j]];
                }
            }

            for (int i = 0; i < mean.length; i++) {
                mean[i] /= points.length;
            }

            //extracting the mean
            for (float[] point : points) {
                for (int j = 0; j < indices.length; j++) {
                    //for (int j = 0; j < points[i].length; j++) {
                    point[indices[j]] -= mean[j];
                }
            }

            //calculating the standard deviation
            for (float[] point : points) {
                for (int j = 0; j < indices.length; j++) {
                    //for (int j = 0; j < points[i].length; j++) {
                    deviation[j] += (((double) (point[indices[j]])) * ((double) (point[indices[j]])));
                }
            }

            for (int i = 0; i < mean.length; i++) {
                deviation[i] = Math.sqrt((deviation[i] / (points.length - 1)));
            }

            //normalization
            for (float[] point : points) {
                for (int j = 0; j < indices.length; j++) {
                    //for (int j = 0; j < points[i].length; j++) {
                    if (deviation[j] != 0.0f) {
                        point[indices[j]] /= deviation[j];
                    }
                }
            }
            if (matrix instanceof SparseMatrix) {
                AbstractMatrix stdmatrix = new DenseMatrix();
                stdmatrix.setAttributes(matrix.getAttributes());

                for (int i = 0; i < matrix.getRowCount(); i++) {
                    AbstractVector oldv = matrix.getRow(i);
                    stdmatrix.addRow(new DenseVector(points[i], oldv.getId(), oldv.getKlass()));
                }

                return stdmatrix;
            } else {
                return matrix;
            }
        }
        return null;
    }

    public AbstractMatrix standarizationColumnsThread(AbstractMatrix matrix, int[] ind,  Integer numberThreads) {

        if(ind==null)
        {
            return null;
        }
        if (ind.length > 0 && matrix.getRowCount() > 0) {
            if (matrix instanceof DenseMatrix) {
                points = new float[matrix.getRowCount()][];

                for (int i = 0; i < points.length; i++) {
                    points[i] = matrix.getRow(i).getValues();
                    matrix.getRow(i).shouldUpdateNorm();
                }
            } else {
                points = matrix.toMatrix();
            }
            this.indices = ind;
            mean = new double[this.indices.length];
            Arrays.fill(mean, 0.0f);

            //calculating the standard deviation
            deviation = new double[this.indices.length];
            Arrays.fill(deviation, 0.0f);

            List<Thread> listThreads = new ArrayList<>();
            Integer beginPoint = 0;
            Integer endPoint = 0;

            if (this.indices.length < numberThreads) {
                numberThreads = this.indices.length;
            }
            System.out.println("# Threads: " + numberThreads);
            for (int i = 1; i <= numberThreads; i++) {
                beginPoint = endPoint + 1;
                endPoint = i * this.indices.length / (numberThreads);

                listThreads.add(new StandarizationColumnsThread(beginPoint - 1, endPoint - 1, i));
                System.out.println("# " + i + " : " + (beginPoint - 1) + " x " + (endPoint - 1));
                listThreads.get(listThreads.size() - 1).start();
            }

            for (Thread listThread : listThreads) {
                try {
                    listThread.join();
                } catch (InterruptedException ex) {
                    System.out.print("Join interrupted\n");
                }
            }

            if (matrix instanceof SparseMatrix) {
                AbstractMatrix stdmatrix = new DenseMatrix();
                stdmatrix.setAttributes(matrix.getAttributes());

                for (int i = 0; i < matrix.getRowCount(); i++) {
                    AbstractVector oldv = matrix.getRow(i);
                    stdmatrix.addRow(new DenseVector(points[i], oldv.getId(), oldv.getKlass()));
                }

                return stdmatrix;
            } else {
                return matrix;
            }
        }
        return null;
    }

    public void calculateStandarizationColumnsThread(Integer beginPoint, Integer endPoint, Integer numberThread) {

        //calculating the mean
        for (int j = beginPoint; j <= endPoint; j++) {
            for (float[] point : points) {
                //calculating
                //for (int j = 0; j < points[i].length; j++) {
                mean[j] += point[indices[j]];
            }
        }

        for (int i = beginPoint; i <= endPoint; i++) {
            mean[i] /= points.length;
        }


        //extracting the mean
        for (int j = beginPoint; j <= endPoint; j++) {
            for (float[] point : points) {
                //for (int j = 0; j < points[i].length; j++) {
                point[indices[j]] -= mean[j];
            }
        }

    

        //calculating the standard deviation
        for (int j = beginPoint; j <= endPoint; j++) {
            for (float[] point : points) {
                deviation[j] += (((double) (point[indices[j]])) * ((double) (point[indices[j]])));
            }
        }

        for (int i = beginPoint; i <= endPoint; i++) {
            deviation[i] = Math.sqrt((deviation[i] / (points.length - 1)));
        }


        //normalization
        for (int j = beginPoint; j <= endPoint; j++) {
            for (float[] point : points) {
                point[indices[j]] /= deviation[j];
            }
        }


    }

    class StandarizationColumnsThread extends Thread {

        Integer beginPoint;
        Integer endPoint;
        Integer numberThread;

        public StandarizationColumnsThread(Integer beginPoint, Integer endPoint, Integer numberThread) {
            this.beginPoint = beginPoint;
            this.endPoint = endPoint;
            this.numberThread = numberThread;
        }

        @Override
        public void run() {
            calculateStandarizationColumnsThread(this.beginPoint, this.endPoint, this.numberThread);
        }
    }

}
