package com.example.mihail.googledrive.business.download.interactor;

import com.example.mihail.googledrive.data.repository.DriveRepository;
import com.example.mihail.googledrive.data.repository.IDriveRepository;
import com.google.android.gms.common.api.GoogleApiClient;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;



public class DownloadInteractor implements IDownloadInteractor {

    private IDriveRepository iDriveRepository;

    public DownloadInteractor(GoogleApiClient googleApiClient)
    {
        iDriveRepository = new DriveRepository(googleApiClient);
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
