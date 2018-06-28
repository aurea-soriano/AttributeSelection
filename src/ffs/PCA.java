/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ffs;

/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer (PEx).
 *
 * How to cite this work:
 * 
 @inproceedings{paulovich2007pex,
 author = {Fernando V. Paulovich and Maria Cristina F. Oliveira and Rosane 
 Minghim},
 title = {The Projection Explorer: A Flexible Tool for Projection-based 
 Multidimensional Visualization},
 booktitle = {SIBGRAPI '07: Proceedings of the XX Brazilian Symposium on 
 Computer Graphics and Image Processing (SIBGRAPI 2007)},
 year = {2007},
 isbn = {0-7695-2996-8},
 pages = {27--34},
 doi = {http://dx.doi.org/10.1109/SIBGRAPI.2007.39},
 publisher = {IEEE Computer Society},
 address = {Washington, DC, USA},
 }
 *  
 * PEx is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * PEx is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * This code was developed by members of Computer Graphics and Image
 * Processing Group (http://www.lcad.icmc.usp.br) at Instituto de Ciencias
 * Matematicas e de Computacao - ICMC - (http://www.icmc.usp.br) of 
 * Universidade de Sao Paulo, Sao Carlos/SP, Brazil. The initial developer 
 * of the original code is Fernando Vieira Paulovich <fpaulovich@gmail.com>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */
import matrix.pointsmatrix.AbstractDimensionalityReduction;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import matrix.pointsmatrix.AbstractDissimilarity;
import matrix.pointsmatrix.DenseMatrix;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class PCA extends AbstractDimensionalityReduction {

    public PCA(int targetDimension) {
        super(targetDimension);
    }

    @Override
    protected float[][] execute(DenseMatrix matrix, AbstractDissimilarity diss) throws IOException {
        float[][] points = matrix.toMatrix();
        double[][] covmatrix_aux = this.createCovarianceMatrix(points);

        covmatrix = new DenseDoubleMatrix2D(covmatrix_aux);

        EigenvalueDecomposition dec = new EigenvalueDecomposition(covmatrix);

        DoubleMatrix2D decomp = dec.getV();

        this.eigenvectors = decomp;

        //storing the eigenvalues
        this.eigenvalues = new float[covmatrix_aux.length];
        DoubleMatrix1D eigenvalues_aux = dec.getRealEigenvalues();
        for (int i = 0; i < covmatrix_aux.length; i++) {
            this.eigenvalues[i] = (float) eigenvalues_aux.get((covmatrix_aux.length - i - 1));
        }

        // covmatrix = null;
        dec = null;
        eigenvalues_aux = null;

        Runtime.getRuntime().gc();
        System.gc();

        double[][] points_aux2 = new double[points.length][];
        for (int i = 0; i < points.length; i++) {
            points_aux2[i] = new double[points[i].length];
            for (int j = 0; j < points[i].length; j++) {
                points_aux2[i][j] = points[i][j];
            }
        }

        double[][] decomp_aux = new double[covmatrix_aux.length][];
        for (int i = 0; i < covmatrix_aux.length; i++) {
            decomp_aux[i] = new double[targetDimension];

            for (int j = 0; j < targetDimension; j++) {
                decomp_aux[i][j] = decomp.getQuick(i, covmatrix_aux[0].length - j - 1);
            }
        }

        DoubleMatrix2D decompostion = new DenseDoubleMatrix2D(decomp_aux);
        DoubleMatrix2D points_aux = new DenseDoubleMatrix2D(points_aux2);
        DoubleMatrix2D proj = points_aux.zMult(decompostion, null, 1.0, 1.0, false, false);

        //copying the projection
        float[][] projection = new float[points.length][];
        double[][] projection_aux = proj.toArray();

        for (int i = 0; i < projection_aux.length; i++) {
            projection[i] = new float[targetDimension];
            for (int j = 0; j < projection_aux[i].length; j++) {
                projection[i][j] = (float) projection_aux[i][j];
            }
        }
        return projection;
    }

    public float[][] reduceCovariance(DenseMatrix matrix, AbstractDissimilarity diss) throws IOException {
        float[][] points = matrix.toMatrix();
        double[][] covmatrix_aux = this.createCovarianceMatrix(points);

        covmatrix = new DenseDoubleMatrix2D(covmatrix_aux);

        EigenvalueDecomposition dec = new EigenvalueDecomposition(covmatrix);

        DoubleMatrix2D decomp = dec.getV();
        //the columns are the eigenvectors
        this.eigenvectors = decomp;

        //storing the eigenvalues
        this.eigenvalues = new float[covmatrix_aux.length];
        DoubleMatrix1D eigenvalues_aux = dec.getRealEigenvalues();
        for (int i = 0; i < covmatrix_aux.length; i++) {
            this.eigenvalues[i] = (float) eigenvalues_aux.get((covmatrix_aux.length - i - 1));
        }

        // covmatrix = null;
        dec = null;
        eigenvalues_aux = null;

        Runtime.getRuntime().gc();
        System.gc();

        double[][] points_aux2 = new double[points.length][];
        for (int i = 0; i < points.length; i++) {
            points_aux2[i] = new double[points[i].length];
            for (int j = 0; j < points[i].length; j++) {
                points_aux2[i][j] = points[i][j];
            }
        }

        double[][] decomp_aux = new double[covmatrix_aux.length][];
        for (int i = 0; i < covmatrix_aux.length; i++) {
            decomp_aux[i] = new double[targetDimension];

            for (int j = 0; j < targetDimension; j++) {
                decomp_aux[i][j] = decomp.getQuick(i, covmatrix_aux[0].length - j - 1);
            }
        }

        DoubleMatrix2D decompostion = new DenseDoubleMatrix2D(decomp_aux);
        DoubleMatrix2D points_aux = new DenseDoubleMatrix2D(points_aux2);
        DoubleMatrix2D proj = points_aux.zMult(decompostion, null, 1.0, 1.0, false, false);

        //copying the projection
        float[][] projection = new float[points.length][];
        double[][] projection_aux = proj.toArray();

        for (int i = 0; i < projection_aux.length; i++) {
            projection[i] = new float[targetDimension];
            for (int j = 0; j < projection_aux[i].length; j++) {
                projection[i][j] = (float) projection_aux[i][j];
            }
        }
        return projection;
    }

    public float[][] reduceCorrelation(DenseMatrix matrix, AbstractDissimilarity diss) throws IOException {
        float[][] points = matrix.toMatrix();
        double[][] corrmatrix_aux = this.createCorrelationMatrix(points);

        corrmatrix = new DenseDoubleMatrix2D(corrmatrix_aux);

        EigenvalueDecomposition dec = new EigenvalueDecomposition(corrmatrix);

        DoubleMatrix2D decomp = dec.getV();

        this.eigenvectors = decomp;

        //storing the eigenvalues
        this.eigenvalues = new float[corrmatrix_aux.length];
        DoubleMatrix1D eigenvalues_aux = dec.getRealEigenvalues();
        for (int i = 0; i < corrmatrix_aux.length; i++) {
            this.eigenvalues[i] = (float) eigenvalues_aux.get((corrmatrix_aux.length - i - 1));
        }

        dec = null;
        eigenvalues_aux = null;

        Runtime.getRuntime().gc();
        System.gc();

        double[][] points_aux2 = new double[points.length][];
        for (int i = 0; i < points.length; i++) {
            points_aux2[i] = new double[points[i].length];
            for (int j = 0; j < points[i].length; j++) {
                points_aux2[i][j] = points[i][j];
            }
        }

        double[][] decomp_aux = new double[corrmatrix_aux.length][];
        for (int i = 0; i < corrmatrix_aux.length; i++) {
            decomp_aux[i] = new double[targetDimension];

            for (int j = 0; j < targetDimension; j++) {
                decomp_aux[i][j] = decomp.getQuick(i, corrmatrix_aux[0].length - j - 1);
            }
        }

        DoubleMatrix2D decompostion = new DenseDoubleMatrix2D(decomp_aux);
        DoubleMatrix2D points_aux = new DenseDoubleMatrix2D(points_aux2);
        DoubleMatrix2D proj = points_aux.zMult(decompostion, null, 1.0, 1.0, false, false);

        //copying the projection
        float[][] projection = new float[points.length][];
        double[][] projection_aux = proj.toArray();

        for (int i = 0; i < projection_aux.length; i++) {
            projection[i] = new float[targetDimension];
            for (int j = 0; j < projection_aux[i].length; j++) {
                projection[i][j] = (float) projection_aux[i][j];
            }
        }
        return projection;
    }

    
     public float[][] reduceSimply(DenseMatrix matrix, AbstractDissimilarity diss) throws IOException {
        float[][] points = matrix.toMatrix();
        double[][] simplymatrix_aux = this.createSimplyMatrix(points);

        simplymatrix = new DenseDoubleMatrix2D(simplymatrix_aux);

        EigenvalueDecomposition dec = new EigenvalueDecomposition(simplymatrix);

        DoubleMatrix2D decomp = dec.getV();

        this.eigenvectors = decomp;

        //storing the eigenvalues
        this.eigenvalues = new float[simplymatrix_aux.length];
        DoubleMatrix1D eigenvalues_aux = dec.getRealEigenvalues();
        for (int i = 0; i < simplymatrix_aux.length; i++) {
            this.eigenvalues[i] = (float) eigenvalues_aux.get((simplymatrix_aux.length - i - 1));
        }

        // covmatrix = null;
        dec = null;
        eigenvalues_aux = null;

        Runtime.getRuntime().gc();
        System.gc();

        double[][] points_aux2 = new double[points.length][];
        for (int i = 0; i < points.length; i++) {
            points_aux2[i] = new double[points[i].length];
            for (int j = 0; j < points[i].length; j++) {
                points_aux2[i][j] = points[i][j];
            }
        }

        double[][] decomp_aux = new double[simplymatrix_aux.length][];
        for (int i = 0; i < simplymatrix_aux.length; i++) {
            decomp_aux[i] = new double[targetDimension];

            for (int j = 0; j < targetDimension; j++) {
                decomp_aux[i][j] = decomp.getQuick(i, simplymatrix_aux[0].length - j - 1);
            }
        }

        DoubleMatrix2D decompostion = new DenseDoubleMatrix2D(decomp_aux);
        DoubleMatrix2D points_aux = new DenseDoubleMatrix2D(points_aux2);
        DoubleMatrix2D proj = points_aux.zMult(decompostion, null, 1.0, 1.0, false, false);

        //copying the projection
        float[][] projection = new float[points.length][];
        double[][] projection_aux = proj.toArray();

        for (int i = 0; i < projection_aux.length; i++) {
            projection[i] = new float[targetDimension];
            for (int j = 0; j < projection_aux[i].length; j++) {
                projection[i][j] = (float) projection_aux[i][j];
            }
        }
        return projection;
    }

    public void setUseSamples(boolean useSamples) {
        this.useSamples = useSamples;
    }

    public DoubleMatrix2D getCovmatrix() {
        return covmatrix;
    }

    /* public float[] getVariances() {
     float[] variances = new float[this.covmatrix.columns()];

     for(int i=0; i<this.covmatrix.columns(); i++)
     {
     variances[i] = (float) this.covmatrix.get(i, i);
     }
     return variances;
     }*/
    public DoubleMatrix2D getEigenvectors() {
        return eigenvectors;
    }

    public float[] getEigenvalues() {
        return eigenvalues;
    }

    private float[][] useSamples(float[][] points) {
        float[][] samples = new float[points.length / 4][];
        ArrayList<Integer> indexes = new ArrayList<>();

        int i = 0;
        while (indexes.size() < samples.length) {
            int index = (int) (Math.random() * (points.length - 1));
            if (!indexes.contains(index)) {
                samples[i] = points[index];
                indexes.add(index);
                i++;
            }
        }

        return samples;
    }

    private double[][] createCovarianceMatrix(float[][] points) {
        if (this.useSamples) {
            points = this.useSamples(points);
        }

        //calculating the mean
        double[] mean = new double[points[0].length];
        Arrays.fill(mean, 0.0f);

        for (float[] point : points) {
            //calculating
            for (int j = 0; j < point.length; j++) {
                mean[j] += point[j];
            }
        }

        for (int i = 0; i < mean.length; i++) {
            mean[i] /= points.length;
        }

        //extracting the mean
        for (float[] point : points) {
            for (int j = 0; j < point.length; j++) {
                point[j] -= mean[j];
            }
        }

        double[][] covariance2 = new double[points[0].length][];
        for (int i = 0; i < covariance2.length; i++) {
            covariance2[i] = new double[points[0].length];
        }

        for (int i = 0; i < covariance2.length; i++) {
            for (int j = 0; j < covariance2.length; j++) {
                covariance2[i][j] = this.covariance(points, i, j);
            }
        }
        return covariance2;
    }

    private double[][] createCorrelationMatrix(float[][] points) {
        if (this.useSamples) {
            points = this.useSamples(points);
        }

        //calculating the mean
        double[] mean = new double[points[0].length];
        Arrays.fill(mean, 0.0f);

        for (float[] point : points) {
            //calculating
            for (int j = 0; j < point.length; j++) {
                mean[j] += point[j];
            }
        }

        for (int i = 0; i < mean.length; i++) {
            mean[i] /= points.length;
        }

        //extracting the mean
        for (float[] point : points) {
            for (int j = 0; j < point.length; j++) {
                point[j] -= mean[j];
            }
        }

        double[][] correlation2 = new double[points[0].length][];
        for (int i = 0; i < correlation2.length; i++) {
            correlation2[i] = new double[points[0].length];
        }

        for (int i = 0; i < correlation2.length; i++) {
            for (int j = 0; j < correlation2.length; j++) {
                correlation2[i][j] = this.correlation(points, i, j);
            }
        }
        return correlation2;
    }
    
     private double[][] createSimplyMatrix(float[][] points) {
        if (this.useSamples) {
            points = this.useSamples(points);
        }

 
        double[][] simply2 = new double[points[0].length][];
        for (int i = 0; i < simply2.length; i++) {
            simply2[i] = new double[points[0].length];
        }

        for (int i = 0; i < simply2.length; i++) {
            for (int j = 0; j < simply2.length; j++) {
                simply2[i][j] =  points[i][j];
            }
        }
        return simply2;
    }

    //calculate the covariance between columns a and b
    private float covariance(float[][] points, int a, int b) {
        float covariance = 0.0f;
        for (float[] point : points) {
            covariance += point[a] * point[b];
        }
        covariance /= (points.length - 1);
        return covariance;
    }

    //calculate the standard deviation of the column a
    private float std(float[][] points, int a) {
        float standarddeviation = 0.0f;
        for (float[] point : points) {
            standarddeviation += point[a] * point[a];
        }
        return (float) Math.sqrt(standarddeviation / (points.length - 1));
    }

    /**
     * corr (x,y) = cov (x,y) / (std (x) * std (y)) *calculate the correlation
     * between columns a and b: pearson
     */
    private float correlation(float[][] points, int a, int b) {
        return (covariance(points,a,b)/(std(points,a)* std(points,b)));
    }


    private boolean useSamples = false;
    private float[] eigenvalues;
    private DoubleMatrix2D covmatrix;
    private DoubleMatrix2D corrmatrix;
    private DoubleMatrix2D simplymatrix;
    private DoubleMatrix2D eigenvectors;
}
