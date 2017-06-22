package com.example.mihail.googledrive.presentation.main;

import android.content.Intent;
import android.net.Uri;

import com.example.mihail.googledrive.BasePresenter;
import com.example.mihail.googledrive.BaseView;

public interface MainContract {

    interface View extends BaseView{
        void showFilesToDownload();

        void showFilesToDelete();

        void startUploadFileAction();

        void chooseFileActivity(Intent intent);
    }

    interface Presenter extends BasePresenter<MainContract.View>{

        void clickToDownloadActivity();
        void clickToDeleteActivity();
        void clickToUploadFile();

        void chooseFile();
        void uploadFile(Uri uri);
    }
}
