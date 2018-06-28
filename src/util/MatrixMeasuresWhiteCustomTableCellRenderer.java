/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author aurea
 */
public class MatrixMeasuresWhiteCustomTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        this.setHorizontalAlignment(SwingConstants.CENTER);
        if (table.isCellSelected(row, column) && column != 0) {
            c.setBackground(Color.getHSBColor(25, 25, 25));
            c.setForeground(Color.RED);
        } else {
            if (column == 0) {
                c.setBackground(Color.LIGHT_GRAY);
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
        }

        return c;
    }
}
