package com.example.mihail.googledrive.presentation.download;

import com.example.mihail.googledrive.BaseContract;

public interface DownloadContract {

    interface View extends BaseContract.BaseView {

        void refreshFiles();

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void provideData();
        void downloadFile(int position);
    }
}