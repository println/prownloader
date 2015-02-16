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
package proto.cederj.prownloader.mvp.view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import proto.cederj.prownloader.mvp.view.DialogView;
import proto.cederj.prownloader.mvp.view.MainView;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class SimpleDialogSwing extends JDialog implements DialogView {

    private static final long serialVersionUID = 1L;
    private JPanel jPanel = null;
    private JLabel jLabel;
    private JFrame view;

    public SimpleDialogSwing(JFrame view) {
        super();
        this.view = view;
        initialize();
    }

    private void initialize() {
        this.setSize(300, 200);
        this.setResizable(false);
        this.setLocationRelativeTo(view);
        this.setTitle("Tree");
        this.setContentPane(getJContentPane());
        //this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private JPanel getJContentPane() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setPreferredSize(new Dimension(300, 100));
            jPanel.setLayout(new BorderLayout());
            jPanel.add(getJLabel(), BorderLayout.CENTER);
        }
        return jPanel;
    }

    private JLabel getJLabel() {
        if (jLabel == null) {
            jLabel = new JLabel("",SwingConstants.CENTER);
        }
        return jLabel;
    }

    @Override
    public void setInfo(String info) {
        jLabel.setText(info);
    }

    @Override
    public void showDialog() {
        this.setModal(true);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void closeDialog() {
        this.dispose();
    }

    @Override
    public void setWindowTitle(String title) {
        this.setTitle(title);
    }

}
