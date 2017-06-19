package com.example.mihail.googledrive.presentation.download.presenter;

import com.example.mihail.googledrive.business.download.interactor.DownloadInteractor;
import com.example.mihail.googledrive.business.download.interactor.IDownloadInteractor;
import com.example.mihail.googledrive.presentation.download.DownloadContract;
import com.example.mihail.googledrive.presentation.recycler_data.model.IFileAdapterModel;

import java.io.File;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DownloadPresenter implements DownloadContract.Presenter {

    private DownloadContract.View iDownloadView;
    private IDownloadInteractor iDownloadInteractor;
    private IFileAdapterModel iFileAdapterModel;
    private CompositeDisposable compositeDisposable;


    public DownloadPresenter(DownloadInteractor iDownloadInteractor, IFileAdapterModel iFileAdapterModel)
    {
        this.iDownloadInteractor = iDownloadInteractor;
        this.iFileAdapterModel = iFileAdapterModel;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void bindView(DownloadContract.View iDownloadView) {
        this.iDownloadView = iDownloadView;
    }

    @Override
    public void unbindView() {
        iDownloadView = null;
        compositeDisposable.clear();
    }

    @Override
    public void provideData() {
        compositeDisposable.add(iDownloadInteractor.getFilesList()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<String>>() {
                    @Override
                    public void onNext(@NonNull List<String> list) {
                        iFileAdapterModel.update(list);
                        iDownloadView.refreshFiles();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void downloadFile(int position) {
        compositeDisposable.add(iDownloadInteractor
                .downloadFile(iFileAdapterModel.getFileName(position))
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<File>() {
                    @Override
                    public void onNext(@NonNull File file) {
                        iDownloadView.showSuccessMessage(file.getAbsolutePath());
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        iDownloadView.showErrorMessage();
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }
}
