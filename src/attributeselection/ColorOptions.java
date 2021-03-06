/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attributeselection;

import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author aurea
 */
public class ColorOptions extends javax.swing.JDialog {

    private int buttonClicked = -1;
    public static final int BTN_OK = 0;
    public static final int BTN_CANCEL = 1;
    public String selectedColor = "None";
    public String selectedOrientation = "None";

    /**
     * Creates new form ColorOptions
     * @param parent
     * @param modal
     */
    public ColorOptions(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public int display() {
        this.setVisible(true);
        return buttonClicked;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        colorLabel = new javax.swing.JLabel();
        orientationLabel = new javax.swing.JLabel();
        colorComboBox = new javax.swing.JComboBox();
        orientationComboBox = new javax.swing.JComboBox();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        colorLabel.setText("Color:");

        orientationLabel.setText("Orientation: ");

        colorComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorComboBoxActionPerformed(evt);
            }
        });

        orientationComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orientationComboBoxActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ookButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(orientationLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(orientationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(colorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(74, 74, 74)
                                .addComponent(colorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77)
                        .addComponent(cancelButton)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(colorLabel)
                    .addComponent(colorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(orientationLabel)
                    .addComponent(orientationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.buttonClicked = BTN_CANCEL;
        this.setVisible(false);

    }//GEN-LAST:event_cancelButtonActionPerformed

    private void ookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ookButtonActionPerformed
        this.buttonClicked = BTN_OK;
        this.setVisible(false);
    }//GEN-LAST:event_ookButtonActionPerformed

    private void colorComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorComboBoxActionPerformed
        this.selectedColor = this.colorComboBox.getSelectedItem().toString();


    }//GEN-LAST:event_colorComboBoxActionPerformed

    private void orientationComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orientationComboBoxActionPerformed
        this.selectedOrientation = this.orientationComboBox.getSelectedItem().toString();

    }//GEN-LAST:event_orientationComboBoxActionPerformed
    public void refreshColorsAndOrientation(List<String> colorList, List<String> orientationList, String selectedColor, String selectedOrientation) {
        String[] strColors = new String[colorList.size()];
        colorList.toArray(strColors);
        DefaultComboBoxModel colorModel = new DefaultComboBoxModel(strColors);
        this.colorComboBox.setModel(colorModel);
        this.colorComboBox.setSelectedItem(selectedColor);

        String[] strOrientation = new String[orientationList.size()];
        orientationList.toArray(strOrientation);
        DefaultComboBoxModel orientationModel = new DefaultComboBoxModel(strOrientation);
        this.orientationComboBox.setModel(orientationModel);
        this.orientationComboBox.setSelectedItem(selectedOrientation);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ColorOptions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ColorOptions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ColorOptions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ColorOptions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ColorOptions dialog = new ColorOptions(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox colorComboBox;
    private javax.swing.JLabel colorLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JComboBox orientationComboBox;
    private javax.swing.JLabel orientationLabel;
    // End of variables declaration//GEN-END:variables
}
