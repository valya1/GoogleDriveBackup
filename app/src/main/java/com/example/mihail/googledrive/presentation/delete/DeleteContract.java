package com.example.mihail.googledrive.presentation.delete;

import com.example.mihail.googledrive.BasePresenter;
import com.example.mihail.googledrive.BaseView;

public interface DeleteContract {

    interface View extends BaseView{

        void refreshFileList();
    }

    interface Presenter extends BasePresenter<DeleteContract.View>{

        void provideData();

        void deleteFile(int position);
    }
}
