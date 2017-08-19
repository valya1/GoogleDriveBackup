package com.example.mihail.googledrive.business.delete.interactor;

import com.example.mihail.googledrive.data.repository.IDriveRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class DeleteInteractor implements IDeleteInteractor {

    private IDriveRepository mDriveRepository;

    public DeleteInteractor(IDriveRepository driveRepository)
    {
        this.mDriveRepository = driveRepository;
    }

    @Override
    public Single<List<String>> getFilesList() {
        return mDriveRepository.getFilesList()
                .onErrorResumeNext(Single::error)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable deleteFile(String fileName) {
        return mDriveRepository.deleteFile(fileName)
                .onErrorResumeNext(Completable::error)
                .subscribeOn(Schedulers.io());
    }
}
