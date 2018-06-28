/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package decisiontree;

/**
 *
 * @author aurea
 */
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JFrame;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

/**
 * Displays a trained J48 as tree.
 * Expects an ARFF filename as first argument.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class VisualizeJ48 {
  public static void main(String args[]) throws Exception {
    // train classifier
      DecisionTree decisionTree = new DecisionTree("Iris-2014-09-19-16:08:04.arff");
     
    // display classifier
    final javax.swing.JFrame jf = 
      new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
    jf.setSize(500,400);
    jf.getContentPane().setLayout(new BorderLayout());
    TreeVisualizer tv = new TreeVisualizer(null,
        decisionTree.getDecissionTree().graph(),
        new PlaceNode2());
    jf.getContentPane().add(tv, BorderLayout.CENTER);
    jf.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent e) {
        jf.dispose();
      }
    });
 
    jf.setVisible(true);
    tv.fitToScreen();
  }
}
