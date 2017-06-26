package com.example.mihail.googledrive.presentation.delete;

import com.example.mihail.googledrive.BaseContract;

public interface DeleteContract {

    interface View extends BaseContract.View {

        void refreshFileList();
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void provideData();

        void deleteFile(int position);
    }
}
