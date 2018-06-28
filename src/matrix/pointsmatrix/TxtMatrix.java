/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.pointsmatrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aurea64
 */
public class TxtMatrix extends AbstractMatrix {

    @Override
    public void addRow(AbstractVector vector) {
        assert (vector instanceof DenseVector) : "ERROR: vector of wrong type!";

        super.addRow(vector);
    }

    @Override
    public void setRow(int index, AbstractVector vector) {
        assert (vector instanceof DenseVector) : "ERROR: vector of wrong type!";

        super.setRow(index, vector);
    }

    @Override
    public void load(String filename) throws IOException {
        this.rows = new ArrayList<AbstractVector>();
        this.attributes = new ArrayList<String>();
        int indexClass = -1;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filename));

            //read the number of objects
            int nrobjs = 0;

            //read the number of dimensions
            int nrdims = 0;

            //read the attributes
            String line = in.readLine();
            StringTokenizer t1 = new StringTokenizer(line, ";");


            while (t1.hasMoreTokens()) {
                nrdims++;
                String token = t1.nextToken();
                this.attributes.add(token.trim());
            }
            for (int k = 0; k < this.attributes.size(); k++) {
                if (this.attributes.get(k).toLowerCase().trim().equals("class")) {
                    this.attributes.remove(k);
                    nrdims--;
                    indexClass = k;
                    break;
                }
            }

            //checking
            if (this.attributes.size() > 0 && this.attributes.size() != nrdims) {
                throw new IOException("The number of attributes does not match "
                        + "with the dimensionality of matrix (" + this.attributes.size()
                        + " - " + nrdims + ").");
            }

            //read the vectors
            while ((line = in.readLine()) != null && line.trim().length() > 0) {
                nrobjs++;
                StringTokenizer t2 = new StringTokenizer(line, ";");

                //read the id
                String id = String.valueOf(nrobjs - 1);

                //class data
                float klass = 0.0f;

                float[] vector = new float[nrdims];

                int index = 0;
                while (t2.hasMoreTokens()) {
                    String token = t2.nextToken();

                    float value;
                    try {
                        value = Float.parseFloat(token.trim());
                    } catch (Exception e) {
                        value = Float.NaN;
                    }
                   int tmp = nrdims;
                    if(indexClass!=-1)
                    {
                        tmp=nrdims+1;
                    }
                    if (index < tmp) {
                        if (indexClass == index) {
                            klass = value;
                        }   
                        else
                        { 
                            vector[index] = value;
                            index++;
                        }
                            
                        
                    } else {
                        throw new IOException("Vector with wrong number of "
                                + "dimensions!");
                    }

                }

                this.addRow(new DenseVector(vector, id, klass));
            }

            //checking
            if (this.getRowCount() != nrobjs) {
                throw new IOException("The number of vectors does not match "
                        + "with the matrix size (" + this.getRowCount()
                        + " - " + nrobjs + ").");
            }

        } catch (FileNotFoundException e) {
            throw new IOException("File " + filename + " does not exist!");
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            //fechar o arquivo
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(TxtMatrix.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void save(String filename) throws IOException {
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new FileWriter(filename));

            //Writting the file header
            out.write("DY\r\n");
            out.write(Integer.toString(this.getRowCount()));
            out.write("\r\n");
            out.write(Integer.toString(this.getDimensions()));
            out.write("\r\n");

            //Writting the attributes
            if (attributes != null) {
                for (int i = 0; i < attributes.size(); i++) {
                    out.write(attributes.get(i).replaceAll("<>", " ").trim());

                    if (i < attributes.size() - 1) {
                        out.write(";");
                    }
                }

                out.write("\r\n");
            } else {
                out.write("\r\n");
            }

            //writting the vectors            
            for (int i = 0; i < this.getRowCount(); i++) {
                this.rows.get(i).write(out);
                out.write("\r\n");
            }

        } catch (IOException ex) {
            throw new IOException("Problems written \"" + filename + "\"!");
        } finally {
            //close the file
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(TxtMatrix.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
