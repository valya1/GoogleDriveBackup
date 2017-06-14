package com.example.mihail.googledrive.presentation.main.presenter;

import android.content.Context;

import com.example.mihail.googledrive.business.choose_file.interactor.ChooseFileInteractor;
import com.example.mihail.googledrive.business.choose_file.interactor.IChooseFileInteractor;
import com.example.mihail.googledrive.business.upload.interactor.IUploadInteractor;
import com.example.mihail.googledrive.business.upload.interactor.UploadInteractor;
import com.example.mihail.googledrive.presentation.main.view.IMainView;
import com.google.android.gms.common.api.GoogleApiClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MainPresenter implements IMainPresenter
{
    private IMainView iMainView;
    private IUploadInteractor iUploadInteractor;
    private IChooseFileInteractor iChooseFileInteractor;
    private CompositeDisposable compositeDisposable;

    public MainPresenter(GoogleApiClient googleApiClient, Context context)
    {
        this.iUploadInteractor = new UploadInteractor(googleApiClient, context);
        this.iChooseFileInteractor = new ChooseFileInteractor(context);
        this.compositeDisposable = new CompositeDisposable();
    }


    @Override
    public void bindView(IMainView iMainView)
    {
        this.iMainView = iMainView;
    }

    @Override
    public void unbindView()
    {
        iMainView = null;
        compositeDisposable.clear();
    }

    @Override
    public void clickToDownloadActivity() {

        iMainView.showFilesToDownload();
    }

    @Override
    public void clickToDeleteActivity() {

        iMainView.showFilesToDelete();
    }

    @Override
    public void clickToUploadFile() {

        iMainView.startUploadFileAction();
    }

    @Override
    public void chooseFile()
    {
        iChooseFileInteractor.chooseFile();
    }

    @Override
    public void uploadFile() {
        compositeDisposable.add(iUploadInteractor.uploadFile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean result) {
                        if (result)
                            iMainView.showSuccessMessage();
                        else
                            iMainView.showErrorMessage();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }
}
