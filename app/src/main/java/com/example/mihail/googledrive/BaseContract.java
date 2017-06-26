package com.example.mihail.googledrive;

public interface BaseContract {

    interface BasePresenter<V> {

        void bindView(V view);
        void unbindView();
    }

    interface BaseView {

        void showSuccessMessage(String message);

        void showErrorMessage(String message);
    }
}
