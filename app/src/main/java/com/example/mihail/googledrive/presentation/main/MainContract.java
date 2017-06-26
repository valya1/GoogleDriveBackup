package com.example.mihail.googledrive.presentation.main;

import android.content.Intent;
import android.net.Uri;

import com.example.mihail.googledrive.BaseContract;

public interface MainContract {

    interface View extends BaseContract.BaseView {
        void showFilesToDownload();

        void showFilesToDelete();

        void startUploadFileAction();

        void chooseFileActivity(Intent intent);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void clickToDownloadActivity();
        void clickToDeleteActivity();
        void clickToUploadFile();

        void chooseFile();
        void uploadFile(Uri uri);
    }
}
