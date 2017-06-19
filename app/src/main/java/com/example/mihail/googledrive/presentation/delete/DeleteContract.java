package com.example.mihail.googledrive.presentation.delete;

import com.example.mihail.googledrive.BasePresenter;

public interface DeleteContract {

    interface View{

        void refreshFileList();
        void showSuccessMessage();
        void showErrorMessage();

    }

    interface Presenter extends BasePresenter<DeleteContract.View>{

        void provideData();

        void deleteFile(int position);
    }
}
