package com.example.mihail.googledrive.presentation.main.presenter;


import com.example.mihail.googledrive.presentation.main.view.IMainView;

/**
 * Created by mihail on 06.06.2017.
 */

public interface IMainPresenter {


    void bindView(IMainView iMainView);
    void unbindView();

    void clickToDownloadActivity();
    void clickToDeleteActivity();
    void clickToUploadFile();

    void chooseFile();
    void uploadFile();
}
