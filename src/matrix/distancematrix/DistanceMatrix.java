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

package matrix.distancematrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class represents distances between elements. It stores the
 * distances from one to all other element.
 * 
 * @author Fernando Vieira Paulovich
 */
public class DistanceMatrix implements Cloneable {

    public DistanceMatrix(String filename) throws IOException {
        this.load(filename);
    }

    public DistanceMatrix(int nrElements) {
        this.maxDistance = Float.NEGATIVE_INFINITY;
        this.minDistance = Float.POSITIVE_INFINITY;
        this.nrElements = nrElements;
        this.distmatrix = new float[nrElements - 1][];

        for (int i = 0; i < this.nrElements - 1; i++) {
            this.distmatrix[i] = new float[i + 1];
        }
    }

    /**
     * This constructor create a distance distmatrix with distances for
     * one to all other points passed as argument.
     * @param matrix
     * @param diss
     * @throws java.io.IOException 
     
    public DistanceMatrix(Matrix matrix, Dissimilarity diss) throws IOException {
        this.nrElements = matrix.getRowCount();
        this.maxDistance = Float.NEGATIVE_INFINITY;
        this.minDistance = Float.POSITIVE_INFINITY;

        //Create and fill the distance distmatrix
        this.distmatrix = new float[this.nrElements - 1][];

        for (int i = 0; i < this.nrElements - 1; i++) {
            this.distmatrix[i] = new float[i + 1];

            for (int j = 0; j < this.distmatrix[i].length; j++) {
                float distance = diss.calculate(matrix.getRow(i + 1), matrix.getRow(j));
                this.setDistance(i + 1, j, distance);
            }
        }
        
        this.ids = matrix.getIds();
        this.cdata = matrix.getClassData();
    }

    //ALTERADO POR JOSE GUSTAVO - 11/03/2010
    public DistanceMatrix(Tree tree, boolean useWeights) {

        float[][] dmat = new float[tree.getSize()][tree.getSize()];
        for (int i=0;i<dmat.length;i++)
            for (int j=0;j<dmat[i].length;j++)
                if (i == j) dmat[i][j] = 0;
                else dmat[i][j] = Float.MAX_VALUE;

        for (int k=0;k<tree.getEdges().size();k++) {
            Edge ed = tree.getEdges().get(k);
            int x = tree.getPosition(tree.getNodeById(ed.getSource().getId()));
            int y = tree.getPosition(tree.getNodeById(ed.getTarget().getId()));
            if (useWeights) dmat[x][y] = dmat[y][x] = ed.getLength();
            else dmat[x][y] = dmat[y][x] = 1.0f;
        }

        //Calculating the shortest path, in the tree, among all nodes (including virtual nodes)
        //Floyd Warshall algorithm.
        int n = dmat.length;
        for (int k=0; k<n; k++)
            for (int i=0; i<n; i++)
                for (int j=0; j<n; j++) {
                    float dd = dmat[i][k] + dmat[k][j];
                    if (dmat[i][j] > dd) dmat[i][j] = dd;
                }

        int k = -1;
        ArrayList<ArrayList<Float>> ndmat = new ArrayList<ArrayList<Float>>();
        for (int i=0;i<dmat.length;i++) {
            if (!tree.getNode(i).getLabel().isEmpty()) {
                k++;
                ndmat.add(new ArrayList<Float>());
                for (int j=0;j<dmat[i].length;j++) {
                    if (!tree.getNode(j).getLabel().isEmpty()) {
                        ndmat.get(k).add(dmat[i][j]);
                    }
                }
            }
        }

        //Create and fill the distance distmatrix
        this.nrElements = ndmat.size();

        this.maxDistance = Float.NEGATIVE_INFINITY;
        this.minDistance = Float.POSITIVE_INFINITY;

        this.distmatrix = new float[this.nrElements - 1][];
        for (int i=0; i<this.nrElements-1; i++) {
            this.distmatrix[i] = new float[i + 1];
            for (int j=0; j < this.distmatrix[i].length; j++) {
                float distance = ndmat.get(i+1).get(j);//dmat[i+1][j];
                if (distance < this.minDistance) this.minDistance = distance;
                if (distance > this.maxDistance) this.maxDistance = distance;
                this.setDistance(i+1, j, distance);
            }
        }

        
        //this.ids = matrix.getIds();
        //this.cdata = matrix.getClassData();

    }
    //FIM DA ALTERACAO

    /**
     * This method modify a distance in the distance matriz.
     * @param indexA The number of the first point.
     * @param indexB The number of the second point.
     * @param value The new value for the distance between the two points.
     */
    public void setDistance(int indexA, int indexB, float value) {
        assert (indexA >= 0 && indexA < nrElements && indexB >= 0 && indexB < nrElements) :
                "ERROR: index out of bounds!";

        if (indexA != indexB) {
            if (indexA < indexB) {
                this.distmatrix[indexB - 1][indexA] = value;
            } else {
                this.distmatrix[indexA - 1][indexB] = value;
            }

            if (minDistance > value && value >= 0.0f) {
                minDistance = value;
            } else {
                if (maxDistance < value && value >= 0.0f) {
                    maxDistance = value;
                }
            }
        }
    }

    /**
     * This method returns the distance between two points.
     * @param indexA The number of the first point.
     * @param indexB The number of the second point.
     * @return The distance between the two points.
     */
    public float getDistance(int indexA, int indexB) {
        assert (indexA >= 0 && indexA < nrElements && indexB >= 0 && indexB < nrElements) :
                "ERROR: index out of bounds! "+indexA+","+indexB+".";

        if (indexA == indexB) {
            return 0.0f;
        } else {
            if (indexA < indexB) {
                return this.distmatrix[indexB - 1][indexA];
            } else {
                return this.distmatrix[indexA - 1][indexB];
            }
        }
    }

    /**
     * @param distmatrix the distmatrix to set
     */
    public void setDistmatrix(float[][] distmatrix) {
        this.distmatrix = distmatrix;
    }

    /**
     * @return the distmatrix
     */
    public float[][] getDistmatrix() {
        return distmatrix;
    }

    /**
     * This method returns the maximum distance stored on the distance distmatrix.
     * @return Returns the maximun distance stored.
     */
    public float getMaxDistance() {
        return maxDistance;
    }

    /**
     * This method returns the minimum distance stored on the distance distmatrix.
     * @return Returns the minimun distance stored.
     */
    public float getMinDistance() {
        return minDistance;
    }

    /**
     * This method returns the number of points where distances are stored
     * on the distance distmatrix.
     * @return The number of points.
     */
    public int getElementCount() {
        return nrElements;
    }

    //ALTERADO POR JOSE GUSTAVO
    /**
     * This method sets the number of points where distances are stored
     * on the distance distmatrix.
     * @param the new number of elements.
     */
    public void setElementCount(int nrElements) {
        this.nrElements = nrElements;
    }

    
    //FIM DA ALTERACAO

    @Override
    public Object clone() throws CloneNotSupportedException {
        DistanceMatrix dmat = new DistanceMatrix(this.nrElements);
        dmat.maxDistance = this.maxDistance;
        dmat.minDistance = this.minDistance;

        //ALTERADO POR JOSE GUSTAVO - 23/10/2009
        if (this.ids != null) {
            dmat.ids = new ArrayList<String>();
            for (int j = 0; j < this.ids.size(); j++) dmat.ids.add(this.ids.get(j));
        }

        if (this.cdata != null) {
            dmat.cdata = new float[this.cdata.length];
            for (int i = 0; i < this.cdata.length; i++) cdata[i] = this.cdata[i];
        }
        
        for (int i = 0; i < this.distmatrix.length; i++) {
            for (int j = 0; j < this.distmatrix[i].length; j++) {
                dmat.distmatrix[i][j] = this.distmatrix[i][j];
            }
        }
        //FIM DA ALTERACAO

        //COMENTADO POR JOSE GUSTAVO
//        for (int i = 0; i < this.distmatrix.length; i++) {
//            for (int j = 0; j < this.distmatrix[i].length; j++) {
//                dmat.distmatrix[i][j] = this.distmatrix[i][j];
//            }
//        }
        //FIM COMENTARIO

        return dmat;
    }

    public void save(File fileName) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        saveMatrix(out);        
    }

    public void save(String fileName) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        saveMatrix(out);
    }

    private void saveMatrix(BufferedWriter out) throws IOException {

        try {

            //writting the number of elements
            out.write(Integer.toString(this.nrElements));
            out.write("\r\n");

            //writing the ids
            if (this.ids != null) {
                for (int i = 0; i < ids.size() - 1; i++) {
                    out.write(ids.get(i));
                    out.write(";");
                }

                out.write(ids.get(ids.size() - 1));
                out.write("\r\n");
            } else {
                for (int i = 0; i < this.nrElements - 1; i++) {
                    out.write(Integer.toString(i) + ";");
                }

                out.write(Integer.toString(this.nrElements - 1) + "\r\n");
            }

            //writing the cdata
            if (cdata != null) {
                for (int i = 0; i < cdata.length - 1; i++) {
                    out.write(Float.toString(cdata[i]));
                    out.write(";");
                }

                out.write(Float.toString(cdata[cdata.length - 1]));
                out.write("\r\n");
            } else {
                for (int i = 0; i < this.nrElements - 1; i++) {
                    out.write("0;");
                }

                out.write("0\r\n");
            }

            for (int i = 0; i < this.distmatrix.length; i++) {
                for (int j = 0; j < this.distmatrix[i].length; j++) {
                    out.write(Float.toString(this.distmatrix[i][j]));

                    if (j < this.distmatrix[i].length - 1) {
                        out.write(";");
                    }
                }

                out.write("\r\n");
            }

        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            //fechar o arquivo
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void load(String filename) throws IOException {
        BufferedReader in = null;

        try {
            ///////////////////////////////////////////////////////////////////
            //getting the header information
            in = new BufferedReader(new java.io.FileReader(filename));

            //getting the number of elements
            this.nrElements = Integer.parseInt(in.readLine());

            //getting the elements ids
            StringTokenizer tUrls = new StringTokenizer(in.readLine(), ";");
            this.ids = new ArrayList<String>();
            while (tUrls.hasMoreTokens()) {
                this.ids.add(tUrls.nextToken().trim());
            }

            //checking
            if (this.ids.size() != this.nrElements) {
                throw new IOException("The number of ids does not match " +
                        "with the size of matrix (" + this.ids.size() +
                        " - " + this.nrElements + ").");
            }

            //getting the class data
            StringTokenizer tCdata = new StringTokenizer(in.readLine(), ";");
            ArrayList<Float> cdata_aux = new ArrayList<Float>();

            while (tCdata.hasMoreTokens()) {
                String token = tCdata.nextToken();
                cdata_aux.add(Float.parseFloat(token.trim()));
            }

            //checking
            if (this.ids.size() != cdata_aux.size()) {
                throw new IOException("The number of class data items does not match " +
                        "with the size of matrix (" + this.ids.size() +
                        " - " + this.nrElements + ").");
            }

            this.cdata = new float[cdata_aux.size()];
            for (int i = 0; i < this.cdata.length; i++) {
                this.cdata[i] = cdata_aux.get(i);
            }

            ///////////////////////////////////////////////////////////////////
            //creating the distance matrix
            this.maxDistance = Float.NEGATIVE_INFINITY;
            this.minDistance = Float.POSITIVE_INFINITY;
            this.distmatrix = new float[this.nrElements - 1][];

            for (int i = 0; i < this.distmatrix.length; i++) {
                this.distmatrix[i] = new float[i + 1];
            }

            for (int i = 0; i < this.distmatrix.length; i++) {
                String line = in.readLine();

                if (line != null) {
                    StringTokenizer tDistance = new StringTokenizer(line, ";");

                    for (int j = 0; j < this.distmatrix[i].length; j++) {
                        if (tDistance.hasMoreTokens()) {
                            String token = tDistance.nextToken();
                            float dist = Float.parseFloat(token.trim());
                            this.setDistance(i + 1, j, dist);
                        } else {
                            throw new IOException("Wrong distance matrix file format.");
                        }
                    }
                } else {
                    throw new IOException("Wrong distance matrix file format.");
                }
            }

        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(DistanceMatrix.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public float[] getClassData() {
        return this.cdata;
    }

    public ArrayList<String> getIds() {
        return this.ids;
    }

    public void setClassData(float[] cdata) {
        this.cdata = cdata;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

     //ALTERADO POR JOSE GUSTAVO
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }

    public void setMinDistance(float minDistance) {
        this.minDistance = minDistance;
    }

    public void printMatrix() {
        System.out.println("-------------------------------------");
        System.out.println("Number of elements -> "+nrElements);
        for (int i=0;i<ids.size();i++) {
            System.out.println(i+" - ID: "+ids.get(i)+" - classe: "+cdata[i]+".");
        }
        System.out.println("-------------------------------------");
        System.out.println(" ");
    }

    //FIM DA ALTERACAO

    private ArrayList<String> ids;
    private float[] cdata;
    private float[][] distmatrix;
    private int nrElements;	//the number of points
    private float maxDistance;		//Maximun distance in the distmatrix
    private float minDistance;		//Minimum distance in the distmatrix

}
