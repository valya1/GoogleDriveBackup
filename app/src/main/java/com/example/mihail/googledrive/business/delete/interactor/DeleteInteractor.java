package com.example.mihail.googledrive.business.delete.interactor;

import com.example.mihail.googledrive.data.repository.IDriveRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;


public class DeleteInteractor implements IDeleteInteractor {

    private IDriveRepository iDriveRepository;

    public DeleteInteractor(IDriveRepository iDriveRepository)
    {
        this.iDriveRepository = iDriveRepository;
    }
    @Override
    public Single<List<String>> getFilesList() {
        return iDriveRepository.getFilesList()
                .onErrorReturn(throwable -> new ArrayList<>());
    }

    @Override
    public Single<Boolean> deleteFile(String fileName) {
        return iDriveRepository.deleteFile(fileName)
                .onErrorReturn(throwable -> false);
    }
}
