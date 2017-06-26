package com.example.mihail.googledrive.presentation.main.presenter;


import android.content.Intent;
import android.net.Uri;

import com.example.mihail.googledrive.business.upload.interactor.IUploadInteractor;
import com.example.mihail.googledrive.presentation.main.MainContract;

import io.reactivex.android.schedulers.AndroidSchedulers;



public class MainPresenter implements MainContract.Presenter
{
    private MainContract.View mMainView;
    private IUploadInteractor mUploadInteractor;

    public MainPresenter(IUploadInteractor iUploadInteractor)
    {
        this.mUploadInteractor = iUploadInteractor;
    }


    @Override
    public void bindView(MainContract.View iMainView)
    {
        this.mMainView = iMainView;
    }

    @Override
    public void unbindView()
    {
        mMainView = null;
    }

    @Override
    public void clickToDownloadActivity() {

        mMainView.showFilesToDownload();
    }

    @Override
    public void clickToDeleteActivity() {

        mMainView.showFilesToDelete();
    }

    @Override
    public void clickToUploadFile() {

        mMainView.startUploadFileAction();
    }

    @Override
    public void chooseFile()
    {
        mMainView.chooseFileActivity(new Intent(Intent.ACTION_GET_CONTENT)
                .setType("*/*")
                .addCategory(Intent.CATEGORY_OPENABLE));
    }

    @Override
    public void uploadFile(Uri uri) {
        if(uri==null) {
            mMainView.showErrorMessage("No data");
            return;
        }
        mUploadInteractor.uploadFile(uri)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
            if(mMainView !=null) mMainView.showSuccessMessage("File was successfully uploaded");
                }, throwable -> {
            if(mMainView!=null) mMainView.showErrorMessage(throwable.getMessage());
        });
    }
}

