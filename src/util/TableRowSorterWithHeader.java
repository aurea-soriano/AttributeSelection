/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableRowSorterWithHeader extends JFrame {
  public TableRowSorterWithHeader() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    String[] columns = { "Item", "Price" };

    Object[][] rows = { { "Potatoes", 10.98 }, { "Magazine", 7.99 },
        { "Can of soup", -0.0 }, { "DVD movie", 39.99 } };

    TableModel model = new DefaultTableModel(rows, columns) {
      @Override
      public Class getColumnClass(int column) {
        if (column >= 0 && column <= getColumnCount())
          return getValueAt(0, column).getClass();
        else
          return Object.class;
      }
    };
    JTable table = new JTable(model);
    RowSorter<TableModel> sorter = new TableRowSorter<>(model);
    table.setRowSorter(sorter);
    getContentPane().add(new JScrollPane(table));

    setSize(200, 150);
    setVisible(true);
  }

}