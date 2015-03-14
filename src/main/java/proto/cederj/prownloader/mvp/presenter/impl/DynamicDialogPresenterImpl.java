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

import proto.cederj.prownloader.mvp.presenter.DynamicDialogCallbackListener;
import proto.cederj.prownloader.mvp.presenter.DynamicDialogPresenter;
import proto.cederj.prownloader.mvp.view.DynamicDialogActionListener;
import proto.cederj.prownloader.mvp.view.DynamicDialogView;

public class DynamicDialogPresenterImpl implements DynamicDialogPresenter, DynamicDialogActionListener {

    private DynamicDialogView view;
    private DynamicDialogCallbackListener listener;

    public DynamicDialogPresenterImpl(DynamicDialogView view) {
        this.view = view;
        view.setActionListener(this);
    }

    @Override
    public void setDynamicMessage(String message) {
        view.setDynamicMessage(message);
    }

    @Override
    public void setButtonLabel(String label) {
        view.setButtonLabel(label);
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
        listener = null;
        view.closeDialog();
    }

    @Override
    public void setDynamicCallbackListener(DynamicDialogCallbackListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCancel() {
        if (listener != null) {
            listener.onCancel();
        }
    }
}
