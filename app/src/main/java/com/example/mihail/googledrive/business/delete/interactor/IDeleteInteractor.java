package com.example.mihail.googledrive.business.delete.interactor;

import java.util.List;

import io.reactivex.Single;


public interface IDeleteInteractor {

    Single<List<String>> getFilesList();

    Single<Boolean> deleteFile(String fileName);
}
