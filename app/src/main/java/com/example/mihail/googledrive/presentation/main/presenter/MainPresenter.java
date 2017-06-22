package com.example.mihail.googledrive.presentation.main.presenter;


import android.content.Intent;
import android.net.Uri;

import com.example.mihail.googledrive.business.choose_file.interactor.IChooseFileInteractor;
import com.example.mihail.googledrive.business.upload.interactor.IUploadInteractor;
import com.example.mihail.googledrive.presentation.main.MainContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;


public class MainPresenter implements MainContract.Presenter
{
    private MainContract.View mMainView;
    private IUploadInteractor mUploadInteractor;
    private IChooseFileInteractor mChooseFileInteractor;

    public MainPresenter(IUploadInteractor iUploadInteractor, IChooseFileInteractor iChooseFileInteractor)
    {
        this.mUploadInteractor = iUploadInteractor;
        this.mChooseFileInteractor = iChooseFileInteractor;
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
        mChooseFileInteractor.chooseFile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        if(mMainView!=null) mMainView.chooseFileActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mMainView!=null) mMainView.showErrorMessage(e.getMessage());
                    }
                });
    }

    @Override
    public void uploadFile(Uri uri) {
                mUploadInteractor.uploadFile(uri)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        if(mMainView!=null) {
                            if (result)
                                mMainView.showSuccessMessage("File was successfully uploaded");
                            else
                                mMainView.showErrorMessage("Error while file uploading");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                });
    }
}

