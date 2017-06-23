package com.example.mihail.googledrive.presentation.download.presenter;

import com.example.mihail.googledrive.business.download.interactor.DownloadInteractor;
import com.example.mihail.googledrive.business.download.interactor.IDownloadInteractor;
import com.example.mihail.googledrive.presentation.download.DownloadContract;
import com.example.mihail.googledrive.presentation.recycler_data.model.IFileAdapterModel;

import java.io.File;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;

public class DownloadPresenter implements DownloadContract.Presenter {

    private DownloadContract.View mDownloadView;
    private IDownloadInteractor mDownloadInteractor;
    private IFileAdapterModel mFileAdapterModel;

    public DownloadPresenter(DownloadInteractor iDownloadInteractor, IFileAdapterModel iFileAdapterModel)
    {
        this.mDownloadInteractor = iDownloadInteractor;
        this.mFileAdapterModel = iFileAdapterModel;
    }

    @Override
    public void bindView(DownloadContract.View iDownloadView) {
        this.mDownloadView = iDownloadView;
    }

    @Override
    public void unbindView() {
        mDownloadView = null;
    }

    @Override
    public void provideData() {
        mDownloadInteractor.getFilesList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(List<String> list) {
                        if(mFileAdapterModel !=null)
                            mFileAdapterModel.update(list);
                        if(mDownloadView!=null)
                            mDownloadView.refreshFiles();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(mDownloadView!=null)
                            mDownloadView.showErrorMessage(e.getMessage());
                    }
                });
    }

    @Override
    public void downloadFile(int position) {
        mDownloadInteractor
                .downloadFile(mFileAdapterModel.getFileName(position))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<File>() {
                    @Override
                    public void onSuccess(File file) {
                        if(mDownloadView!=null) mDownloadView.showSuccessMessage(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(mDownloadView!=null) mDownloadView.showErrorMessage(e.getMessage());
                    }
                });
    }
}
