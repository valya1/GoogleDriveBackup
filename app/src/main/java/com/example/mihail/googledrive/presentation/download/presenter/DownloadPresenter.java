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
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class DownloadPresenter implements DownloadContract.Presenter {

    private DownloadContract.View mDownloadView;
    private IDownloadInteractor mDownloadInteractor;
    private IFileAdapterModel mFileAdapterModel;
    private CompositeDisposable mCompositeDisposable;

    public DownloadPresenter(DownloadInteractor iDownloadInteractor, IFileAdapterModel iFileAdapterModel)
    {
        this.mDownloadInteractor = iDownloadInteractor;
        this.mFileAdapterModel = iFileAdapterModel;
        this.mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void bindView(DownloadContract.View iDownloadView) {
        this.mDownloadView = iDownloadView;
    }

    @Override
    public void unbindView() {
        mDownloadView = null;
        mCompositeDisposable.clear();
    }

    @Override
    public void provideData() {
        mCompositeDisposable.add(mDownloadInteractor.getFilesList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(List<String> list) {
                        mFileAdapterModel.update(list);
                        mDownloadView.refreshFiles();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mDownloadView.showErrorMessage(e.getMessage());
                    }
                }));
    }

    @Override
    public void downloadFile(int position) {
        mCompositeDisposable.add(mDownloadInteractor
                .downloadFile(mFileAdapterModel.getFileName(position))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<File>() {
                    @Override
                    public void onSuccess(File file) {
                        if(mDownloadView!=null) mDownloadView.showSuccessMessage(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(mDownloadView!=null) mDownloadView.showErrorMessage(e.getMessage());
                    }
                })
        );
    }
}
