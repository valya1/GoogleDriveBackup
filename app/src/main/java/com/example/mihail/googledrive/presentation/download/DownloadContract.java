package com.example.mihail.googledrive.presentation.download;

import com.example.mihail.googledrive.BaseContract;

public interface DownloadContract {

    interface View extends BaseContract.View {

        void refreshFiles();

    }

    interface Presenter extends BaseContract.Presenter<View> {

        void provideData();
        void downloadFile(int position);
    }
}