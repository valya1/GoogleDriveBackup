package com.example.mihail.googledrive;


public interface BasePresenter<V> {

    void bindView(V view);
    void unbindView();
}
