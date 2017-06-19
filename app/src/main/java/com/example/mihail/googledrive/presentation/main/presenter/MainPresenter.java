package com.example.mihail.googledrive.presentation.main.presenter;


import com.example.mihail.googledrive.business.choose_file.interactor.IChooseFileInteractor;
import com.example.mihail.googledrive.business.upload.interactor.IUploadInteractor;
import com.example.mihail.googledrive.presentation.main.MainContract;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MainPresenter implements MainContract.Presenter
{
    private MainContract.View iMainView;
    private IUploadInteractor iUploadInteractor;
    private IChooseFileInteractor iChooseFileInteractor;
    private CompositeDisposable compositeDisposable;


    public MainPresenter(IUploadInteractor iUploadInteractor, IChooseFileInteractor iChooseFileInteractor)
    {
        this.iUploadInteractor = iUploadInteractor;
        this.iChooseFileInteractor = iChooseFileInteractor;
        this.compositeDisposable = new CompositeDisposable();
    }


    @Override
    public void bindView(MainContract.View iMainView)
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
        compositeDisposable.add(iUploadInteractor.uploadFile(iMainView.getFileUri())
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

