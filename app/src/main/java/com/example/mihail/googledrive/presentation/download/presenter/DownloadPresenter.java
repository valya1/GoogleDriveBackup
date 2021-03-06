package com.example.mihail.googledrive.presentation.download.presenter;

import com.example.mihail.googledrive.business.download.interactor.IDownloadInteractor;
import com.example.mihail.googledrive.presentation.download.DownloadContract;
import com.example.mihail.googledrive.presentation.recycler_data.model.IFileAdapterModel;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class DownloadPresenter implements DownloadContract.Presenter {

    private DownloadContract.View mDownloadView;
    private IDownloadInteractor mDownloadInteractor;
    private IFileAdapterModel mFileAdapterModel;

    public DownloadPresenter(IDownloadInteractor downloadInteractor, IFileAdapterModel fileAdapterModel)
    {
        this.mDownloadInteractor = downloadInteractor;
        this.mFileAdapterModel = fileAdapterModel;
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
                .subscribe(list -> {
                            if (mFileAdapterModel != null) mFileAdapterModel.update(list);
                            if (mDownloadView != null) mDownloadView.refreshFiles();
                        }
                , throwable -> {
                            if (mDownloadView != null) mDownloadView.showErrorMessage(throwable.getMessage());
                        }
                );
    }

    @Override
    public void downloadFile(int position) {
        mDownloadInteractor.downloadFile(mFileAdapterModel.getFileName(position))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                            if (mDownloadView != null) mDownloadView.showSuccessMessage(file.getAbsolutePath());
                        }
                    ,throwable -> {
                            if (mDownloadView != null) mDownloadView.showErrorMessage(throwable.getMessage());
                        });
    }
}
