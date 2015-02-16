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
package proto.cederj.prownloader;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import static proto.cederj.prownloader.App.bootstrap;
import proto.cederj.prownloader.mvp.presenter.DialogPresenter;
import proto.cederj.prownloader.mvp.presenter.impl.DialogPresenterImpl;
import proto.cederj.prownloader.mvp.view.DialogView;
import proto.cederj.prownloader.mvp.view.DynamicDialogView;
import proto.cederj.prownloader.mvp.view.MainView;
import proto.cederj.prownloader.mvp.view.swing.DynamicDialogSwing;
import proto.cederj.prownloader.mvp.view.swing.MainWindowSwing;
import proto.cederj.prownloader.mvp.view.swing.SimpleDialogSwing;
import static sun.misc.PostVMInitHook.run;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class ViewTest {

    public static void main(String[] a) {
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
            java.util.logging.Logger.getLogger(MainWindowSwing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindowSwing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindowSwing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindowSwing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    DynamicDialogView view = new DynamicDialogSwing(null);
                    
                    view.setWindowTitle("title");
                    view.setInfo("info");
                    view.setDynamicMessage("message");
                    
                    view.showDialog();

                } catch (Exception ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        SwingUtilities.invokeLater(run);
    }
}
