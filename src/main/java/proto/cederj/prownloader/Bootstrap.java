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
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import proto.cederj.prownloader.mvp.presenter.DialogPresenter;
import proto.cederj.prownloader.mvp.presenter.DynamicDialogPresenter;
import proto.cederj.prownloader.mvp.presenter.MainPresenter;
import proto.cederj.prownloader.mvp.presenter.impl.DialogPresenterImpl;
import proto.cederj.prownloader.mvp.presenter.impl.DynamicDialogPresenterImpl;
import proto.cederj.prownloader.mvp.presenter.impl.MainPresenterImpl;
import proto.cederj.prownloader.mvp.view.DialogView;
import proto.cederj.prownloader.mvp.view.DynamicDialogView;
import proto.cederj.prownloader.mvp.view.MainView;
import proto.cederj.prownloader.mvp.view.swing.DynamicDialogSwing;
import proto.cederj.prownloader.mvp.view.swing.MainWindowSwing;
import proto.cederj.prownloader.mvp.view.swing.SimpleDialogSwing;
import proto.cederj.prownloader.persistence.jpa.JpaDaoFactory;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class Bootstrap implements AutoCloseable {

    private JpaDaoFactory factory;
    private MainPresenter presenter;

    public Bootstrap() {
        String persistenceUnitName = "hsqldb";
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
            factory = JpaDaoFactory.newInstance(emf);
            
            MainView view = new MainWindowSwing();
            
            DialogView dialogView = new SimpleDialogSwing((MainWindowSwing)view);
            DialogPresenter dialog = new DialogPresenterImpl(dialogView);
            DynamicDialogView dynamicDialogView = new DynamicDialogSwing((MainWindowSwing)view);
            DynamicDialogPresenter dynamicDialog = new DynamicDialogPresenterImpl(dynamicDialogView);
            
            presenter = new MainPresenterImpl(factory, view, dialog, dynamicDialog);

        } catch (Exception ex) {
            Logger.getLogger(Bootstrap.class.getName()).log(Level.SEVERE, null, ex);
        };
    }

    public void start() throws Exception{
        presenter.initialize();
    }

    @Override
    public void close() {
        try {
            factory.close();
        } catch (Exception ex) {
        }
    }
}
