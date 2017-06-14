package com.example.mihail.googledrive.presentation.download.presenter;

import com.example.mihail.googledrive.presentation.download.view.IDownloadView;


public interface IDownloadPresenter {

    void bindView(IDownloadView iDownloadView);
    void unbindView();

    void provideData();
    void downloadFile(int position);

}
