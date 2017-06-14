package com.example.mihail.googledrive.presentation.delete.presenter;


import com.example.mihail.googledrive.presentation.delete.view.IDeleteView;

public interface IDeletePresenter {

    void bindView(IDeleteView iDeleteView);
    void unbindView();

    void provideData();

    void deleteFile(int position);
}
