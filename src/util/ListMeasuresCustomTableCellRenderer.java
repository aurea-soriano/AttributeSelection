/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author aurea
 */
public class ListMeasuresCustomTableCellRenderer extends DefaultTableCellRenderer {

    Color[] rgb;
    List<Color> listColors = new ArrayList<>();
    List<Color> listColorsComplete = new ArrayList<>();
    Double maxValue, minValue;

    public ListMeasuresCustomTableCellRenderer(String colorScaleString, Double max, Double min) {
        try {
            Class classSelected = Class.forName("color." + colorScaleString);
            ColorScale colorScale = (ColorScale) classSelected.newInstance();
            maxValue = max;
            minValue = min;
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
        this.setHorizontalAlignment(SwingConstants.CENTER);
        if (table.isCellSelected(row, column)) {
            c.setBackground(Color.getHSBColor(25, 25, 25));
            c.setForeground(Color.RED);
        } else {

            try {
                Double value1 = Double.valueOf(value.toString());
                int position = (int) Math.round(Utils.minmax(maxValue, minValue, (rgb.length - 2) * 1.0, 0.0, value1));
                Color color = rgb[255-position];

                if (!value1.isNaN() && maxValue != minValue) {
                    c.setBackground(color);
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
        return c;
    }
}
