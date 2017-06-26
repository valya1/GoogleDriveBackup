package com.example.mihail.googledrive.business.upload.interactor;

import android.net.Uri;

import io.reactivex.Completable;


public interface IUploadInteractor {

     Completable uploadFile(Uri fileUri);
}
