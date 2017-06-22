package com.example.mihail.googledrive.business.download.interactor;

import com.example.mihail.googledrive.data.repository.IDriveRepository;
import java.io.File;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class DownloadInteractor implements IDownloadInteractor {

    private IDriveRepository mDriveRepository;

    public DownloadInteractor(IDriveRepository driveRepository)
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
    public Single<File> downloadFile(String fileName) {
        return mDriveRepository.downloadFile(fileName)
                .onErrorResumeNext(throwable -> Single.error(new RuntimeException("Error while downloading file")))
                .subscribeOn(Schedulers.io());
    }
}
