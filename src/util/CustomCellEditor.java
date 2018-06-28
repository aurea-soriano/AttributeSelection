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
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

public class CustomCellEditor extends DefaultCellEditor {

    public CustomCellEditor() {
        super(new JTextField());
        // TODO Auto-generated constructor stub
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        table.clearSelection();
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }

}  