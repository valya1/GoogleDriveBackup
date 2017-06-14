package com.example.mihail.googledrive.presentation.main.view;

/**
 * Created by mihail on 06.06.2017.
 */

public interface IMainView {

    void showFilesToDownload();
    void showFilesToDelete();
    void startUploadFileAction();
    void showSuccessMessage();
    void showErrorMessage();
}
