package com.example.mihail.googledrive.presentation.download;

import com.example.mihail.googledrive.BasePresenter;
import com.example.mihail.googledrive.BaseView;

public interface DownloadContract {

    interface View extends BaseView{

        void refreshFiles();

    }

    interface Presenter extends BasePresenter<DownloadContract.View>{

        void provideData();
        void downloadFile(int position);
    }
}