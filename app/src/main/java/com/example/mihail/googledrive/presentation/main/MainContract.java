package com.example.mihail.googledrive.presentation.main;

import android.net.Uri;

import com.example.mihail.googledrive.BasePresenter;

public interface MainContract {

    interface View {
        void showFilesToDownload();

        void showFilesToDelete();

        void startUploadFileAction();

        void showSuccessMessage();

        void showErrorMessage();

        Uri getFileUri();
    }

    interface Presenter extends BasePresenter<MainContract.View>{

        void clickToDownloadActivity();
        void clickToDeleteActivity();
        void clickToUploadFile();

        void chooseFile();
        void uploadFile();
    }
}
