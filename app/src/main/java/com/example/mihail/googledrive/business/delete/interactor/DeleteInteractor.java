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
                .onErrorResumeNext(throwable -> Single.error(new RuntimeException("Unable to get files list")))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable deleteFile(String fileName) {
        return mDriveRepository.deleteFile(fileName)
                .onErrorResumeNext(throwable -> Completable.error(new RuntimeException("Error while deleting file")))
                .subscribeOn(Schedulers.io());
    }
}
