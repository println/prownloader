/*
 * Copyright 2015 Felipe Santos <live.proto at hotmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proto.cederj.prownloader.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class Main extends javax.swing.JFrame {

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        //CoursePanel cp = new CoursePanel();
        //cp.setBounds(0, 0, 400, 300);
        //descJPanel.add(cp);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        FlowLayout flowLayout1 = new FlowLayout(FlowLayout.LEFT, 0, 0);
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT, 0, 0);
        FlowLayout flowLayout3 = new FlowLayout(FlowLayout.LEFT, 0, 0);
        FlowLayout flowLayout4 = new FlowLayout(FlowLayout.LEFT, 0, 0);

        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);         
        setTitle("Prownloader");
        getContentPane().setPreferredSize(new Dimension(700, 300));
        setLayout(flowLayout);
        setResizable(false);
        getAccessibleContext().setAccessibleDescription("Proto's video-lesson downloader");
        pack();       
        setLocationRelativeTo(null);

        treeJPanel = new JPanel();
        treeJPanel.setPreferredSize(new Dimension(300, 300));
        treeJPanel.setBackground(Color.blue);
        treeJPanel.setLayout(flowLayout1);

        descJPanel = new JPanel();
        descJPanel.setPreferredSize(new Dimension(400, 300));
        descJPanel.setBackground(Color.red);
        descJPanel.setLayout(flowLayout2);

        JPanel aJPanel = new JPanel();
        aJPanel.setBackground(Color.yellow);
        aJPanel.setPreferredSize(new Dimension(300, 300));
        aJPanel.setLayout(flowLayout3);

        JPanel bJPanel = new JPanel();
        bJPanel.setBackground(Color.green);
        bJPanel.setPreferredSize(new Dimension(400, 300));
        bJPanel.setLayout(flowLayout4);

        treeJPanel.add(aJPanel);
        descJPanel.add(bJPanel);
        add(treeJPanel);
        add(descJPanel);

        
    }// </editor-fold>//GEN-END:initComponents

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
                    javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Main main = new Main();
                main.setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel treeJPanel;
    private javax.swing.JPanel descJPanel;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
