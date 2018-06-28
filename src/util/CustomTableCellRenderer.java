/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author aurea
 */
public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    Color[] rgb;
    String orientation;
    List<Color> listColors = new ArrayList<>();
    List<Float> listClusters = new ArrayList<>();
    Integer positionClass;

    public CustomTableCellRenderer(String colorScaleString, String orient, List<Float> listClusters, Integer positionClass) {
        try {
            this.positionClass = positionClass;
            this.listClusters = listClusters;
            this.orientation = orient;
            Class classSelected = Class.forName("color." + colorScaleString);
            ColorScale colorScale = (ColorScale) classSelected.newInstance();
            rgb = colorScale.getColorScale();

        

            for (int i = 50; i < 215; i = i + 50) {
                listColors.add(rgb[i]);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            Logger.getLogger(CustomTableCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (orientation.equals("Vertical")) {

            try {
                Float value1 = Float.valueOf(value.toString());
                Color color = Color.BLACK;
                if (!value1.isNaN()) {
                    color = listColors.get(column % listColors.size());
                    c.setBackground(color);
                    Float grayColor = (color.getRed() + color.getGreen() + color.getBlue()) * 1.f / 3.f;
                    Float brightness = (Utils.max(Utils.max(color.getRed(), color.getGreen()), color.getBlue()) / 255.f) * 100.f;
                    if (brightness < 65) {
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);

                }
            } catch (NumberFormatException e) {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
        } else {
            if (orientation.equals("Horizontal")) {
                try {
                    Float value1 = Float.valueOf(value.toString());
                    Color color = Color.BLACK;
                    if (!value1.isNaN()) {
                        color = listColors.get(row % listColors.size());
                        c.setBackground(color);
                        Float grayColor = (color.getRed() + color.getGreen() + color.getBlue()) * 1.f / 3.f;
                        Float brightness = (Utils.max(Utils.max(color.getRed(), color.getGreen()), color.getBlue()) / 255.f) * 100.f;
                        if (brightness < 65) {
                            c.setForeground(Color.WHITE);
                        } else {
                            c.setForeground(Color.BLACK);
                        }
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);

                    }
                } catch (NumberFormatException e) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
            } else {
                //By Cluster

                try {
                    Float cluster = Float.valueOf(table.getValueAt(row, positionClass).toString());
                    Integer position = listClusters.indexOf(cluster);
                    Float value1 = Float.valueOf(value.toString());
                    if (!value1.isNaN()) {
                        
                        int indexColor = Math.round(Utils.minmax(Collections.max(listClusters), Collections.min(listClusters),  254.f, 0.f,   cluster));
                        Color color;
                        //System.out.println(indexColor);
                        color = rgb[indexColor];
                        c.setBackground(color);

                        Float grayColor = (color.getRed() + color.getGreen() + color.getBlue()) * 1.f / 3.f;
                        Float brightness = (Utils.max(Utils.max(color.getRed(), color.getGreen()), color.getBlue()) / 255.f) * 100.f;
                        if (brightness < 65) {
                            c.setForeground(Color.WHITE);
                        } else {
                            c.setForeground(Color.BLACK);
                        }

                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                } catch (NumberFormatException e) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
            }

        }

        if (table.isCellSelected(row, column)) {
            c.setBackground(Color.RED);
        }/* else if (table.isRowSelected(row)) {
         setForeground(Color.green);
         } else if (table.isColumnSelected(column)) {
         setForeground(Color.blue);
         } else {
         setForeground(Color.black);
         }*/

        return c;
    }
}
