package com.example.mihail.googledrive.business.upload.interactor;

import io.reactivex.Single;



public interface IUploadInteractor {

     Single<Boolean> uploadFile();
}
