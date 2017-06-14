package com.example.mihail.googledrive.business.delete.interactor;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by mihail on 10.06.2017.
 */

public interface IDeleteInteractor {

    Single<List<String>> getFilesList();

    Single<Boolean> deleteFile(String fileName);
}
