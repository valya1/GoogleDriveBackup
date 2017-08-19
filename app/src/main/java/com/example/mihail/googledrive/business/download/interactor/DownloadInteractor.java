package com.example.mihail.googledrive.business.download.interactor;

import com.example.mihail.googledrive.data.repository.IDriveRepository;

import java.io.File;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class DownloadInteractor implements IDownloadInteractor {

    private IDriveRepository mDriveRepository;

    public DownloadInteractor(IDriveRepository driveRepository) {
        mDriveRepository = driveRepository;
    }

    @Override
    public Single<List<String>> getFilesList() {
        return mDriveRepository.getFilesList()
                .onErrorResumeNext(Single::error)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<File> downloadFile(String fileName) {
        return mDriveRepository.downloadFile(fileName)
                .onErrorResumeNext(Single::error)
                .subscribeOn(Schedulers.io());
    }
}
