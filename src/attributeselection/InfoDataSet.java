/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attributeselection;

import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author aurea
 */
public class InfoDataSet extends javax.swing.JDialog {

    /**
     * Creates new form InfoDataSet
     *
     * @param parent
     * @param modal
     */
    public InfoDataSet(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void display() {
        this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameInfoDataSetLabel = new javax.swing.JLabel();
        instancesInfoDataSetLabel = new javax.swing.JLabel();
        attributesInfoDataSetLabel = new javax.swing.JLabel();
        classesInfoDataSetLabel = new javax.swing.JLabel();
        okInfoDataSetButton = new javax.swing.JButton();
        nameInfoDataSetText = new javax.swing.JLabel();
        instancesInfoDataSetText = new javax.swing.JLabel();
        attributesInfoDataSetText = new javax.swing.JLabel();
        classesInfoDataSetText = new javax.swing.JLabel();
        distributionInfoDataSetLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        distributionInfoDataSetList = new javax.swing.JList();
        distributionIntervalInfoDataSetLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        distributionIntervalInfoDataSetList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        nameInfoDataSetLabel.setText("DataSet: ");

        instancesInfoDataSetLabel.setText("# Instances:");

        attributesInfoDataSetLabel.setText("# Attributes:");

        classesInfoDataSetLabel.setText("# Classes:");

        okInfoDataSetButton.setText("OK");
        okInfoDataSetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okInfoDataSetButtonActionPerformed(evt);
            }
        });

        distributionInfoDataSetLabel.setText("Distribution of class:");

        jScrollPane1.setViewportView(distributionInfoDataSetList);

        distributionIntervalInfoDataSetLabel.setText("Distribution of interval_time:");

        jScrollPane2.setViewportView(distributionIntervalInfoDataSetList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(distributionInfoDataSetLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(attributesInfoDataSetLabel)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nameInfoDataSetLabel)
                            .addComponent(instancesInfoDataSetLabel))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(attributesInfoDataSetText, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(instancesInfoDataSetText, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(okInfoDataSetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(classesInfoDataSetLabel)
                                    .addGap(18, 18, 18)
                                    .addComponent(classesInfoDataSetText, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(distributionIntervalInfoDataSetLabel)
                                    .addGap(18, 18, 18)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(11, 11, 11)))))
                    .addComponent(nameInfoDataSetText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameInfoDataSetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameInfoDataSetText, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(instancesInfoDataSetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(instancesInfoDataSetText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(attributesInfoDataSetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(attributesInfoDataSetText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(classesInfoDataSetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(classesInfoDataSetText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(distributionInfoDataSetLabel)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(distributionIntervalInfoDataSetLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addComponent(okInfoDataSetButton)
                .addGap(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okInfoDataSetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okInfoDataSetButtonActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_okInfoDataSetButtonActionPerformed

    public void refreshInfoDataSet(String nameDataset, int numberInstances, int numberAttributes, int numberClasses, List<String> listValueCluster, List<String> listValueTimeInterval) {
        this.nameInfoDataSetText.setText(nameDataset);
        this.instancesInfoDataSetText.setText(String.valueOf(numberInstances));
        this.attributesInfoDataSetText.setText(String.valueOf(numberAttributes));
        this.classesInfoDataSetText.setText(String.valueOf(numberClasses));
        DefaultListModel model = new DefaultListModel();
        for (String listValueCluster1 : listValueCluster) {
            model.addElement(listValueCluster1);
        }
        this.distributionInfoDataSetList.setModel(model);
         DefaultListModel model2 = new DefaultListModel();
        for (String listValueTime : listValueTimeInterval) {
            model2.addElement(listValueTime);
        }
        this.distributionIntervalInfoDataSetList.setModel(model2);
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InfoDataSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                InfoDataSet dialog = new InfoDataSet(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel attributesInfoDataSetLabel;
    private javax.swing.JLabel attributesInfoDataSetText;
    private javax.swing.JLabel classesInfoDataSetLabel;
    private javax.swing.JLabel classesInfoDataSetText;
    private javax.swing.JLabel distributionInfoDataSetLabel;
    private javax.swing.JList distributionInfoDataSetList;
    private javax.swing.JLabel distributionIntervalInfoDataSetLabel;
    private javax.swing.JList distributionIntervalInfoDataSetList;
    private javax.swing.JLabel instancesInfoDataSetLabel;
    private javax.swing.JLabel instancesInfoDataSetText;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel nameInfoDataSetLabel;
    private javax.swing.JLabel nameInfoDataSetText;
    private javax.swing.JButton okInfoDataSetButton;
    // End of variables declaration//GEN-END:variables
}
