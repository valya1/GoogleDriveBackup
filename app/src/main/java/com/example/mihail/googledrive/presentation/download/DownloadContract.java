package com.example.mihail.googledrive.presentation.download;

import com.example.mihail.googledrive.BasePresenter;

public interface DownloadContract {

    interface View {

        void refreshFiles();

        void showSuccessMessage(String result);
        void showErrorMessage();

    }

    interface Presenter extends BasePresenter<DownloadContract.View>{

        void provideData();
        void downloadFile(int position);
    }
}