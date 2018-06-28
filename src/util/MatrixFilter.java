/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;

;

/**
 *
 * @author Renato R O da Silva
 */
public class MatrixFilter extends PExFileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            return extension.equals("dmat")
                    || extension.equals("data") || extension.equals("txt");
        }
        return false;
    }

    public String getTypeDescription(File f) {
        String extension = Utils.getExtension(f);
        String type = null;

        if (extension != null) {
            if (extension.equals("data")) {
                type = "Multidimensional Points File (*.data)";
            } else {
                if (extension.equals("dmat")) {
                    type = "Distance Matrix File (*.dmat)";
                } else {
                    if (extension.equals("txt")) {
                        type = "Txt Matrix File (*.txt)";
                    } else {
                        
                    }
                }
            }
        }
        return type;
    }

    @Override
    public String getDescription() {
        return "Distance (*.dmat), Points (*.data), Txt (*.txt) matrices";
    }

    @Override
    public String getProperty() {
        return "POINTS.DIR";
    }

    @Override
    public String getFileExtension() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
