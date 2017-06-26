package com.example.mihail.googledrive;

public interface BaseContract {

    interface Presenter<V> {

        void bindView(V view);
        void unbindView();
    }

    interface View {

        void showSuccessMessage(String message);

        void showErrorMessage(String message);
    }
}
