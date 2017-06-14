package com.example.mihail.googledrive.business.delete.interactor;

import com.example.mihail.googledrive.data.repository.DriveRepository;
import com.example.mihail.googledrive.data.repository.IDriveRepository;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;


public class DeleteInteractor implements IDeleteInteractor {

    private IDriveRepository iDriveRepository;

    public DeleteInteractor(GoogleApiClient googleApiClient)
    {
        this.iDriveRepository = new DriveRepository(googleApiClient);
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
