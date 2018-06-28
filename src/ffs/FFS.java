/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ffs;

import cern.colt.matrix.DoubleMatrix2D;
import clustering.Kmeans;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.pointsmatrix.AbstractMatrix;
import matrix.pointsmatrix.DenseMatrix;
import matrix.pointsmatrix.DenseVector;
import matrix.pointsmatrix.DissimilarityFactory;
import matrix.pointsmatrix.DissimilarityFactory.DissimilarityType;
import matrix.pointsmatrix.EuclideanSimilarity;
import util.Pair;
import static util.Utils.getMultiplicationFloat2Matrix;
import static util.Utils.getSumFloat1Vector;
import static util.Utils.getSumFloat2Matrix;
import static util.Utils.getTranposeFloatMatrix;
import static util.Utils.standarddeviation;

/**
 *
 * @author aurea
 */
public class FFS {

    List<DoubleMatrix2D> listMTSEigenVectors = new ArrayList<>();
    List<Integer> listMTSPi = new ArrayList<>();
    List<DenseMatrix> listMTSItens = new ArrayList<>();
    List listSelectedValues;
    Pair[] scoreVariables;
    float percentVariance;
    Lock lock = new java.util.concurrent.locks.ReentrantLock();

    public List<DenseMatrix> getMatrixTimeSeries(List listSelectedValues, ArrayList<String> selectedAttributes, AbstractMatrix pointsMatrix, Integer indexIdTime) {

        List<Integer> positions = new ArrayList<>();
        List<List<float[]>> listMatrices = new ArrayList<>();
        List<DenseMatrix> listMTSItem = new ArrayList<>();

        for (Object listSelectedValue : listSelectedValues) {
            positions.add(selectedAttributes.indexOf(listSelectedValue));
        }

        HashSet<Float> hashsetIds = new HashSet<>();

        for (int i = 0; i < pointsMatrix.getRowCount(); i++) {
            Float posId = pointsMatrix.getRow(i).getValue(indexIdTime);
            hashsetIds.add(posId);
        }

        List<Float> listIds = new ArrayList<>(hashsetIds);
        Collections.sort(listIds);

        for (Float idItem : listIds) {
            List<float[]> listValues = new ArrayList<>();
            listMatrices.add(listValues);
        }

        for (int i = 0; i < pointsMatrix.getRowCount(); i++) {
            float[] values = new float[positions.size()];
            int count = 0;
            for (int j = 0; j < pointsMatrix.getRow(i).getValues().length; j++) {
                if (positions.contains(j)) {
                    values[count] = pointsMatrix.getRow(i).getValue(j);
                    count++;
                }
            }
            Integer posId = listIds.indexOf(pointsMatrix.getRow(i).getValue(indexIdTime));
            listMatrices.get(posId).add(values);
        }

        for (List<float[]> listMatrice : listMatrices) {

            DenseMatrix mtsItem = new DenseMatrix();

            for (float[] listMatrice1 : listMatrice) {
                mtsItem.addRow(new DenseVector(listMatrice1));
            }

            listMTSItem.add(mtsItem);
        }

        return listMTSItem;
    }

    public float[][] pcDcpcCorrelation(List listSelectedValues, ArrayList<String> selectedAttributes, AbstractMatrix pointsMatrix, String ffsThreshold, String idTimeName, Integer numberThreads) throws IOException {

        Integer indexIdTime = selectedAttributes.indexOf(idTimeName);
        this.listSelectedValues = listSelectedValues;
        this.listMTSItens = getMatrixTimeSeries(listSelectedValues, selectedAttributes, pointsMatrix, indexIdTime);
        this.listMTSEigenVectors = new ArrayList<>();
        this.listMTSPi = new ArrayList<>();
        this.percentVariance = Float.valueOf(ffsThreshold);
        int p = 0;

        List<Thread> listThreads = new ArrayList<>();
        Integer beginPoint = 0;
        Integer endPoint = 0;

        if (listMTSItens.size() < numberThreads) {
            numberThreads = listMTSItens.size();
        }
        for (int i = 1; i <= numberThreads; i++) {
            beginPoint = endPoint + 1;
            endPoint = i * listMTSItens.size() / (numberThreads);

            listThreads.add(new pcDcpcCorrelationThread(beginPoint - 1, endPoint - 1, i));
            listThreads.get(listThreads.size() - 1).start();

        }

        for (Thread listThread : listThreads) {
            try {
                listThread.join();
            } catch (InterruptedException ex) {
                System.out.print("Join interrupted\n");
            }
        }

        /*for (int index = 0; index < listMTSItens.size(); index++) {
         DenseMatrix mtsItem = listMTSItens.get(index);
         try {
         PCA pca = new PCA(listSelectedValues.size() - 1);
         pca.reduceCorrelation(mtsItem, DissimilarityFactory.getInstance(DissimilarityType.EUCLIDEAN));

         listMTSEigenVectors.add(pca.getEigenvectors());

         float sumVariance = 0.f;
         for (float i : pca.getEigenvalues()) {
         sumVariance += i;
         }

         float countVariance = 0.f;
         int countPi = 0;
         for (int j = 0; j < pca.getEigenvalues().length; j++) {
         countVariance += pca.getEigenvalues()[j];
         countPi++;
         float percentVar = countVariance / sumVariance * 100.f;
         if (percentVar >= Float.valueOf(ffsThreshold)) {
         break;
         }

         }

         listMTSPi.add(countPi);
         } catch (IOException ex) {
         Logger.getLogger(FFS.class.getName()).log(Level.SEVERE, null, ex);
         }

         }*/
        p = Collections.max(listMTSPi);
        List<float[][]> listLMTS = new ArrayList<>();
        List<float[][]> listHMTS = new ArrayList<>();

        for (DoubleMatrix2D listMTSEigenVector : listMTSEigenVectors) {
            float[][] LMTSitem = getPRowsDoubleMatrix2D(listMTSEigenVector, p);
            float[][] LMTStransposedItem = getTranposeFloatMatrix(LMTSitem);

            listLMTS.add(LMTSitem);
            if (listHMTS.isEmpty()) {
                //LTXL
                listHMTS.add(getMultiplicationFloat2Matrix(LMTStransposedItem, LMTSitem));

            } else {
                // H(i-1) + LTXL
                listHMTS.add(getSumFloat2Matrix(listHMTS.get(listHMTS.size() - 1), getMultiplicationFloat2Matrix(LMTStransposedItem, LMTSitem)));
            }
        }

        DenseMatrix HMTSMatrix = new DenseMatrix();
        float[][] HMTS = listHMTS.get(listHMTS.size() - 1);
        for (float[] HMTS1 : HMTS) {
            HMTSMatrix.addRow(new DenseVector(HMTS1));
        }
        PCA pcaH = new PCA(HMTSMatrix.getDimensions() - 1);
        pcaH.reduceSimply(HMTSMatrix, DissimilarityFactory.getInstance(DissimilarityType.EUCLIDEAN));

        return getPRowsDoubleMatrix2D(pcaH.getEigenvectors(), p);
    }
    
    
    
      public float[][] pcDcpcCovariance(List listSelectedValues, ArrayList<String> selectedAttributes, AbstractMatrix pointsMatrix, String ffsThreshold, String idTimeName, Integer numberThreads) throws IOException {

        Integer indexIdTime = selectedAttributes.indexOf(idTimeName);
        this.listSelectedValues = listSelectedValues;
        this.listMTSItens = getMatrixTimeSeries(listSelectedValues, selectedAttributes, pointsMatrix, indexIdTime);
        this.listMTSEigenVectors = new ArrayList<>();
        this.listMTSPi = new ArrayList<>();
        this.percentVariance = Float.valueOf(ffsThreshold);
        int p = 0;

        List<Thread> listThreads = new ArrayList<>();
        Integer beginPoint = 0;
        Integer endPoint = 0;

        if (listMTSItens.size() < numberThreads) {
            numberThreads = listMTSItens.size();
        }
        for (int i = 1; i <= numberThreads; i++) {
            beginPoint = endPoint + 1;
            endPoint = i * listMTSItens.size() / (numberThreads);

            listThreads.add(new pcDcpcCovarianceThread(beginPoint - 1, endPoint - 1, i));
            listThreads.get(listThreads.size() - 1).start();

        }

        for (Thread listThread : listThreads) {
            try {
                listThread.join();
            } catch (InterruptedException ex) {
                System.out.print("Join interrupted\n");
            }
        }

        p = Collections.max(listMTSPi);
        //System.out.println("p " + p);
        List<float[][]> listLMTS = new ArrayList<>();
        List<float[][]> listHMTS = new ArrayList<>();

        for (DoubleMatrix2D listMTSEigenVector : listMTSEigenVectors) {
            float[][] LMTSitem = getPRowsDoubleMatrix2D(listMTSEigenVector, p);
            float[][] LMTStransposedItem = getTranposeFloatMatrix(LMTSitem);

            listLMTS.add(LMTSitem);
            if (listHMTS.isEmpty()) {
                //LTXL
                listHMTS.add(getMultiplicationFloat2Matrix(LMTStransposedItem, LMTSitem));

            } else {
                // H(i-1) + LTXL
                listHMTS.add(getSumFloat2Matrix(listHMTS.get(listHMTS.size() - 1), getMultiplicationFloat2Matrix(LMTStransposedItem, LMTSitem)));
            }
        }

        DenseMatrix HMTSMatrix = new DenseMatrix();
        float[][] HMTS = listHMTS.get(listHMTS.size() - 1);
        for (float[] HMTS1 : HMTS) {
            HMTSMatrix.addRow(new DenseVector(HMTS1));
        }
        PCA pcaH = new PCA(HMTSMatrix.getDimensions() - 1);
        pcaH.reduceSimply(HMTSMatrix, DissimilarityFactory.getInstance(DissimilarityType.EUCLIDEAN));

        return getPRowsDoubleMatrix2D(pcaH.getEigenvectors(), p);
    }

    private float[][] getPRowsDoubleMatrix2D(DoubleMatrix2D matrix2D, int p) {
        double[][] matrixDouble = matrix2D.toArray();
        float[][] newMatrixP = new float[p][matrixDouble.length];

        for (int i = 0; i < p; i++) {
            float[] itemMatrixP = new float[matrixDouble.length];
            for (int j = 0; j < matrixDouble.length; j++) {
                itemMatrixP[j] = (float) matrixDouble[j][i];
            }
            newMatrixP[i] = itemMatrixP;
        }
        return newMatrixP;
    }

    /**
     * Proposed by Soriano
     *
     * @param listSelectedValues
     * @param selectedAttributes
     * @param pointsMatrix
     * @param ffsThreshold
     * @param idTimeName
     * @param numberThreads
     * @return
     */
    public Pair[] ffsPCAApproximation(List listSelectedValues, ArrayList<String> selectedAttributes, AbstractMatrix pointsMatrix, String ffsThreshold, String idTimeName, Integer numberThreads) {
        Integer indexTimeName = selectedAttributes.indexOf(idTimeName);

        this.listMTSItens = getMatrixTimeSeries(listSelectedValues, selectedAttributes, pointsMatrix, indexTimeName);
        this.listSelectedValues = listSelectedValues;

        this.percentVariance = Float.valueOf(ffsThreshold);
        scoreVariables = new Pair[listSelectedValues.size()];

        for (int l = 0; l < listSelectedValues.size(); l++) {
            scoreVariables[l] = new Pair(l, 0.f);
        }

        List<Thread> listThreads = new ArrayList<>();
        Integer beginPoint = 0;
        Integer endPoint = 0;

        if (listMTSItens.size() < numberThreads) {
            numberThreads = listMTSItens.size();
        }
        for (int i = 1; i <= numberThreads; i++) {
            beginPoint = endPoint + 1;
            endPoint = i * listMTSItens.size() / (numberThreads);

            listThreads.add(new ffsPCAApproximationThread(beginPoint - 1, endPoint - 1, i));
            listThreads.get(listThreads.size() - 1).start();

        }

        for (Thread listThread : listThreads) {
            try {
                listThread.join();
            } catch (InterruptedException ex) {
                System.out.print("Join interrupted\n");
            }
        }

        /*
         for (DenseMatrix mtsItem : listMTSItens) {

         try {
         PCA pca = new PCA(listSelectedValues.size() - 1);
         pca.reduceCovariance(mtsItem, DissimilarityFactory.getInstance(DissimilarityType.EUCLIDEAN));
         double[][] matrixEigenVectors = pca.getEigenvectors().toArray();
         float varianceTotal = getSumFloat1Vector(pca.getEigenvalues());
         float varianceParcial = 0.f;
         int pItem = 0;
         for (int j = 0; j < pca.getEigenvalues().length; j++) {
         varianceParcial += pca.getEigenvalues()[j];
         pItem++;
         float percentVarItem = (varianceParcial / varianceTotal) * 100.f;
         if (percentVarItem >= percentVariance) {
         break;
         }
         }
         float[][] matrixFactorLoadings = new float[pItem][listSelectedValues.size()];

         for (int i = 0; i < listSelectedValues.size(); i++) {
         for (int j = 0; j < pItem; j++) {

         float[] eigenVector = new float[matrixEigenVectors.length];
         float[] valueColumnX = new float[mtsItem.getRowCount()];
         matrixFactorLoadings[j][i] = 0.f;

         for (int k = 0; k < matrixEigenVectors.length; k++) {
         eigenVector[k] = (float) matrixEigenVectors[k][j];
         }

         for (int k = 0; k < mtsItem.getRowCount(); k++) {
         valueColumnX[k] = mtsItem.getRow(k).getValue(i);

         }
         float stdValueColumnX = standarddeviation(valueColumnX);
         float eigenValue = pca.getEigenvalues()[pItem];
         float sqrtEigenValue = (float) Math.sqrt(eigenValue);
         for (int k = 0; k < mtsItem.getRowCount(); k++) {
         matrixFactorLoadings[j][i] += ((mtsItem.getRow(k).getValue(i)) / (stdValueColumnX * sqrtEigenValue));
         }

         matrixFactorLoadings[j][i] /= mtsItem.getRowCount();

         }
         }

         for (int l = 0; l < listSelectedValues.size(); l++) {
         float score = 0.f;
         for (int m = 0; m < pItem; m++) {
         score += (matrixFactorLoadings[m][l] * matrixFactorLoadings[m][l]);
         }
         scoreVariables[l].value += Math.sqrt(score);
         }
         } catch (IOException ex) {
         Logger.getLogger(FFS.class.getName()).log(Level.SEVERE, null, ex);
         }
         }*/
        return scoreVariables;
    }

    public Pair[] ffsRank(float[][] DCPC) {
        Pair[] scoreVariables = new Pair[DCPC[0].length];
        for (int i = 0; i < DCPC[0].length; i++) {
            float scoreItem = 0.f;
            for (float[] DCPC1 : DCPC) {
                scoreItem += (DCPC1[i] * DCPC1[i]);
            }
            scoreVariables[i] = new Pair(i, (float) Math.sqrt(scoreItem));
        }

        return scoreVariables;
    }

    public Pair[] ffsCluster(float[][] DCPC, int ffsKInteger) throws IOException {
        float[][] transposeDCPC = getTranposeFloatMatrix(DCPC);
        DenseMatrix kmeansMatrix = new DenseMatrix();
        for (float[] DCPC1 : transposeDCPC) {
            kmeansMatrix.addRow(new DenseVector(DCPC1));
        }
        Kmeans bestKmeans = null;
        float lowestWithinCluster = Float.POSITIVE_INFINITY;
        ArrayList<ArrayList<Integer>> idMatrix = null;
        AbstractMatrix centroidMatrix = null;
        for (int i = 0; i < 20; i++) {
            Kmeans kmeans = new Kmeans(ffsKInteger);
            ArrayList<ArrayList<Integer>> idMatrixtemp = kmeans.execute(DissimilarityFactory.getInstance(DissimilarityType.EUCLIDEAN), kmeansMatrix);
            AbstractMatrix centroidMatrixtemp = kmeans.getCentroids();
            float resultWithinCluster = getWithinClusterSum(transposeDCPC, idMatrixtemp, centroidMatrixtemp);
            if (lowestWithinCluster > resultWithinCluster) {
                lowestWithinCluster = resultWithinCluster;
                bestKmeans = kmeans;
                idMatrix = idMatrixtemp;
                centroidMatrix = bestKmeans.getCentroids();
            }
        }

        Pair[] scoreVariables = new Pair[idMatrix.size()];

        for (int i = 0; i < idMatrix.size(); i++) {
            Pair scoreItem = getClosestVariableK(transposeDCPC, idMatrix.get(i), (DenseVector) centroidMatrix.getRow(i));
            scoreVariables[i] = scoreItem;
        }

        return scoreVariables;
    }

    private float getWithinClusterSum(float[][] transposeDCPC, ArrayList<ArrayList<Integer>> idMatrix, AbstractMatrix centroidMatrix) {
        float sum = 0.f;
        for (int i = 0; i < idMatrix.size(); i++) {
            for (int j = 0; j < idMatrix.get(i).size(); j++) {
                sum += new EuclideanSimilarity().calculate(centroidMatrix.getRow(i), new DenseVector(transposeDCPC[idMatrix.get(i).get(j)]));

            }
        }
        return sum;
    }

    private Pair getClosestVariableK(float[][] transposeDCPC, ArrayList<Integer> ids, DenseVector centroid) {
        float minDistance = Float.POSITIVE_INFINITY;
        int position;
        position = Integer.MAX_VALUE;
        for (Integer id : ids) {
            float value = new EuclideanSimilarity().calculate(centroid, new DenseVector(transposeDCPC[id]));
            if (value < minDistance) {
                minDistance = value;
                position = id;
            }
        }
        return new Pair(position, minDistance);
    }

    public Pair[] ffsHybrid(float[][] DCPC, int ffsKInteger) throws IOException {
        float[][] transposeDCPC = getTranposeFloatMatrix(DCPC);
        DenseMatrix kmeansMatrix = new DenseMatrix();
        for (float[] DCPC1 : transposeDCPC) {
            kmeansMatrix.addRow(new DenseVector(DCPC1));
        }
        Kmeans bestKmeans = null;
        float lowestWithinCluster = Float.POSITIVE_INFINITY;
        ArrayList<ArrayList<Integer>> idMatrix = null;
        AbstractMatrix centroidMatrix = null;
        for (int i = 0; i < 20; i++) {
            Kmeans kmeans = new Kmeans(ffsKInteger);
            ArrayList<ArrayList<Integer>> idMatrixtemp = kmeans.execute(DissimilarityFactory.getInstance(DissimilarityType.EUCLIDEAN), kmeansMatrix);
            AbstractMatrix centroidMatrixtemp = kmeans.getCentroids();
            float resultWithinCluster = getWithinClusterSum(transposeDCPC, idMatrixtemp, centroidMatrixtemp);
            if (lowestWithinCluster > resultWithinCluster) {
                lowestWithinCluster = resultWithinCluster;
                bestKmeans = kmeans;
                idMatrix = idMatrixtemp;
                centroidMatrix = bestKmeans.getCentroids();
            }
        }

        Pair[] scoreVariables = new Pair[idMatrix.size()];
        for (int i = 0; i < idMatrix.size(); i++) {
            Pair[] scoreCluster = new Pair[idMatrix.get(i).size()];

            for (int j = 0; j < idMatrix.get(i).size(); j++) {
                float scoreItem = 0.f;
                for (int k = 0; k < transposeDCPC[idMatrix.get(i).get(j)].length; k++) {
                    scoreItem += (transposeDCPC[idMatrix.get(i).get(j)][k] * transposeDCPC[idMatrix.get(i).get(j)][k]);
                }
                scoreCluster[j] = new Pair(idMatrix.get(i).get(j), scoreItem);
            }
            Arrays.sort(scoreCluster);

            scoreVariables[i] = scoreCluster[scoreCluster.length - 1];
        }
        return scoreVariables;
    }

    private void calculatepcDcpcCorrelationThreads(Integer beginPoint, Integer endPoint, Integer numberThread) {
        for (int index = beginPoint; index <= endPoint; index++) {

            DenseMatrix mtsItem = listMTSItens.get(index);
            try {
                PCA pca = new PCA(listSelectedValues.size() - 1);
                pca.reduceCorrelation(mtsItem, DissimilarityFactory.getInstance(DissimilarityType.EUCLIDEAN));

                lock.lock();
                try {
                    listMTSEigenVectors.add(pca.getEigenvectors());
                } finally {
                    lock.unlock();
                }
                float sumVariance = 0.f;
                for (float i : pca.getEigenvalues()) {
                    sumVariance += i;
                }

                float countVariance = 0.f;
                int countPi = 0;
                for (int j = 0; j < pca.getEigenvalues().length; j++) {
                    countVariance += pca.getEigenvalues()[j];
                    countPi++;
                    float percentVar = countVariance / sumVariance * 100.f;
                    if (percentVar >= this.percentVariance) {
                        break;
                    }

                }
                lock.lock();
                try {
                    listMTSPi.add(countPi);
                } finally {
                    lock.unlock();
                }
            } catch (IOException ex) {
                Logger.getLogger(FFS.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        // System.out.println(numberThread + " finished.");
    }
    
    private void calculatepcDcpcCovarianceThreads(Integer beginPoint, Integer endPoint, Integer numberThread) {
        for (int index = beginPoint; index <= endPoint; index++) {

            DenseMatrix mtsItem = listMTSItens.get(index);
            try {
                PCA pca = new PCA(listSelectedValues.size() - 1);
                pca.reduceCovariance(mtsItem, DissimilarityFactory.getInstance(DissimilarityType.EUCLIDEAN));

                lock.lock();
                try {
                    listMTSEigenVectors.add(pca.getEigenvectors());
                } finally {
                    lock.unlock();
                }
                float sumVariance = 0.f;
                for (float i : pca.getEigenvalues()) {
                    sumVariance += i;
                }

                float countVariance = 0.f;
                int countPi = 0;
                for (int j = 0; j < pca.getEigenvalues().length; j++) {
                    countVariance += pca.getEigenvalues()[j];
                    countPi++;
                    float percentVar = countVariance / sumVariance * 100.f;
                    if (percentVar >= this.percentVariance) {
                        break;
                    }

                }
                lock.lock();
                try {
                    listMTSPi.add(countPi);
                } finally {
                    lock.unlock();
                }
            } catch (IOException ex) {
                Logger.getLogger(FFS.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        // System.out.println(numberThread + " finished.");
    }

    private void calculateffsPCAApproximationThreads(Integer beginPoint, Integer endPoint, Integer numberThread) {
        for (int index = beginPoint; index <= endPoint; index++) {

            DenseMatrix mtsItem = listMTSItens.get(index);
            try {
                PCA pca = new PCA(listSelectedValues.size() - 1);
                pca.reduceCovariance(mtsItem, DissimilarityFactory.getInstance(DissimilarityType.EUCLIDEAN));
                double[][] matrixEigenVectors = pca.getEigenvectors().toArray();
                float varianceTotal = getSumFloat1Vector(pca.getEigenvalues());
                float varianceParcial = 0.f;
                int pItem = 0;
                for (int j = 0; j < pca.getEigenvalues().length; j++) {
                    varianceParcial += pca.getEigenvalues()[j];
                    pItem++;
                    float percentVarItem = (varianceParcial / varianceTotal) * 100.f;
                    if (percentVarItem >= percentVariance) {
                        break;
                    }
                }
                float[][] matrixFactorLoadings = new float[pItem][listSelectedValues.size()];

                for (int i = 0; i < listSelectedValues.size(); i++) {
                    for (int j = 0; j < pItem; j++) {

                        float[] eigenVector = new float[matrixEigenVectors.length];
                        float[] valueColumnX = new float[mtsItem.getRowCount()];
                        matrixFactorLoadings[j][i] = 0.f;

                        for (int k = 0; k < matrixEigenVectors.length; k++) {
                            eigenVector[k] = (float) matrixEigenVectors[k][j];
                        }

                        for (int k = 0; k < mtsItem.getRowCount(); k++) {
                            valueColumnX[k] = mtsItem.getRow(k).getValue(i);

                        }
                        float stdValueColumnX = standarddeviation(valueColumnX);
                        float eigenValue = pca.getEigenvalues()[pItem];
                        float sqrtEigenValue = (float) Math.sqrt(eigenValue);
                        for (int k = 0; k < mtsItem.getRowCount(); k++) {
                            matrixFactorLoadings[j][i] += ((mtsItem.getRow(k).getValue(i)) / (stdValueColumnX * sqrtEigenValue));
                        }

                        matrixFactorLoadings[j][i] /= mtsItem.getRowCount();

                    }
                }

                for (int l = 0; l < listSelectedValues.size(); l++) {
                    float score = 0.f;
                    for (int m = 0; m < pItem; m++) {
                        score += (matrixFactorLoadings[m][l] * matrixFactorLoadings[m][l]);
                    }
                    lock.lock();
                    try {
                        scoreVariables[l].value += Math.sqrt(score);
                    } finally {
                        lock.unlock();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(FFS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class pcDcpcCorrelationThread extends Thread {

        Integer beginPoint;
        Integer endPoint;
        Integer numberThread;

        public pcDcpcCorrelationThread(Integer beginPoint, Integer endPoint, Integer numberThread) {
            this.beginPoint = beginPoint;
            this.endPoint = endPoint;
            this.numberThread = numberThread;
        }

        @Override
        public void run() {
            calculatepcDcpcCorrelationThreads(this.beginPoint, this.endPoint, this.numberThread);
        }
    }
      class pcDcpcCovarianceThread extends Thread {

        Integer beginPoint;
        Integer endPoint;
        Integer numberThread;

        public pcDcpcCovarianceThread(Integer beginPoint, Integer endPoint, Integer numberThread) {
            this.beginPoint = beginPoint;
            this.endPoint = endPoint;
            this.numberThread = numberThread;
        }

        @Override
        public void run() {
            calculatepcDcpcCovarianceThreads(this.beginPoint, this.endPoint, this.numberThread);
        }
    }

    class ffsPCAApproximationThread extends Thread {

        Integer beginPoint;
        Integer endPoint;
        Integer numberThread;

        public ffsPCAApproximationThread(Integer beginPoint, Integer endPoint, Integer numberThread) {
            this.beginPoint = beginPoint;
            this.endPoint = endPoint;
            this.numberThread = numberThread;
        }

        @Override
        public void run() {
            calculateffsPCAApproximationThreads(this.beginPoint, this.endPoint, this.numberThread);
        }
    }
}
