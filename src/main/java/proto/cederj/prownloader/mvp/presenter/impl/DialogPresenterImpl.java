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
package proto.cederj.prownloader.mvp.presenter.impl;

import com.sun.security.auth.callback.DialogCallbackHandler;
import proto.cederj.prownloader.mvp.presenter.DialogPresenter;
import proto.cederj.prownloader.mvp.view.DialogView;

public class DialogPresenterImpl extends DialogCallbackHandler implements DialogPresenter {

    private DialogView view;

    public DialogPresenterImpl(DialogView view) {
        this.view = view;
    }

    @Override
    public void setWindowTitle(String title) {
        view.setWindowTitle(title);
    }

    @Override
    public void setInfo(String info) {
        view.setInfo(info);
    }

    @Override
    public void show() {
        view.showDialog();
    }

    @Override
    public void close() {
        view.closeDialog();
    }
}
