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

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import proto.cederj.prownloader.mvp.view.DynamicDialogActionListener;
import proto.cederj.prownloader.mvp.view.DynamicDialogView;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class DynamicDialogSwing extends JDialog implements DynamicDialogView {

    private static final long serialVersionUID = 1L;
    private JPanel jPanel = null;
    private JFrame view;
    private JLabel jLabelInfo;
    private JLabel jLabelMessage;
    private JPanel jPanelCenter;
    private DynamicDialogActionListener listener;
    private JButton jButtonAction;

    public DynamicDialogSwing(JFrame view) {
        super();
        this.view = view;
        initialize();
    }

    private void initialize() {
        this.setSize(300, 200);
        this.setResizable(false);
        this.setLocationRelativeTo(view);
        this.setTitle("DynamicDialog");
        this.setContentPane(getCenteredContentPanel());
        //this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private JPanel getCenteredContentPanel() {
        if (jPanelCenter == null) {
            jPanelCenter = new JPanel();
            jPanelCenter.setPreferredSize(new Dimension(300, 100));
            jPanelCenter.setLayout(new BoxLayout(jPanelCenter, BoxLayout.X_AXIS));
            jPanelCenter.add(getJContentPane());
        }
        return jPanelCenter;
    }

    private JPanel getJContentPane() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
            jPanel.add(Box.createHorizontalGlue());
            JLabel info = getJLabelInfo();
            JLabel message = getJLabelMessage();
            JButton cancel = getJButtonCancel();
            info.setAlignmentX(Component.CENTER_ALIGNMENT);
            message.setAlignmentX(Component.CENTER_ALIGNMENT);            
            cancel.setAlignmentX(Component.CENTER_ALIGNMENT);
            jPanel.add(info);
            jPanel.add(message);
            jPanel.add(cancel);
            jPanel.add(Box.createHorizontalGlue());
        }
        return jPanel;
    }

    private JLabel getJLabelInfo() {
        if (jLabelInfo == null) {
            jLabelInfo = new JLabel("", SwingConstants.CENTER);
        }
        return jLabelInfo;
    }

    private JLabel getJLabelMessage() {
        if (jLabelMessage == null) {
            jLabelMessage = new JLabel("", SwingConstants.CENTER);
        }
        return jLabelMessage;
    }

    private JButton getJButtonCancel() {
        if (jButtonAction == null) {
            jButtonAction = new JButton();
            jButtonAction.setText("Cancelar");
            jButtonAction.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    onCancel();
                }
            });
        }
        return jButtonAction;
    }

    private void onCancel() {
        if (listener != null) {
            listener.onCancel();
        }
    }

    @Override
    public void setInfo(String info) {
        jLabelInfo.setText(info);
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

    @Override
    public void setActionListener(DynamicDialogActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void setDynamicMessage(String message) {
        jLabelMessage.setText(message);
    }

    @Override
    public void setButtonLabel(String label) {
        jButtonAction.setText(label);
    }
    
}
