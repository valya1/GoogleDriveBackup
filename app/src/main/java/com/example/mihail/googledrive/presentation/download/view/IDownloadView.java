package com.example.mihail.googledrive.presentation.download.view;


public interface IDownloadView {


    void refreshFiles();

    void showSuccessMessage(String result);
    void showErrorMessage();
}
