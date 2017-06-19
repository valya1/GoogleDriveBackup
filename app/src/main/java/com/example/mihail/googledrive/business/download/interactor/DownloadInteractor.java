package com.example.mihail.googledrive.business.download.interactor;

import com.example.mihail.googledrive.data.repository.IDriveRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;



public class DownloadInteractor implements IDownloadInteractor {

    private IDriveRepository iDriveRepository;

    public DownloadInteractor(IDriveRepository iDriveRepository)
    {
        this.iDriveRepository = iDriveRepository;
    }

    @Override
    public Single<List<String>> getFilesList() {
        return iDriveRepository.getFilesList()
                .onErrorReturn(throwable -> new ArrayList<>());
    }

    @Override
    public Single<File> downloadFile(String fileName) {
        return iDriveRepository.downloadFile(fileName)
                .onErrorResumeNext(throwable -> Single.error(new RuntimeException("Error while downloading file")));
    }
}
