/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 *
 * @author aurea
 */
public class PNGFilter extends PExFileFilter {

    @Override
    public String getDescription() {
        return "PNG images (*.png)";
    }

    @Override
    public String getProperty() {
        return "PNG.DIR";
    }

    @Override
    public String getFileExtension() {
        return "png";
    }

}