package com.example.mihail.googledrive.presentation.delete;

import com.example.mihail.googledrive.BaseContract;

public interface DeleteContract {

    interface View extends BaseContract.BaseView {

        void refreshFileList();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void provideData();

        void deleteFile(int position);
    }
}
