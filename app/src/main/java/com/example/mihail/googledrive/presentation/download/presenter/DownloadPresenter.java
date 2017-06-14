package com.example.mihail.googledrive.presentation.download.presenter;

import com.example.mihail.googledrive.business.download.interactor.DownloadInteractor;
import com.example.mihail.googledrive.business.download.interactor.IDownloadInteractor;
import com.example.mihail.googledrive.presentation.download.view.IDownloadView;
import com.example.mihail.googledrive.presentation.recycler_data.model.IFileAdapterModel;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DownloadPresenter implements IDownloadPresenter {

    private IDownloadView iDownloadView;
    private IDownloadInteractor iDownloadInteractor;
    private IFileAdapterModel iFileAdapterModel;
    private CompositeDisposable compositeDisposable;


    public DownloadPresenter(GoogleApiClient googleApiClient, IFileAdapterModel iFileAdapterModel)
    {
        this.iDownloadInteractor = new DownloadInteractor(googleApiClient);
        this.iFileAdapterModel = iFileAdapterModel;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void bindView(IDownloadView iDownloadView) {
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
