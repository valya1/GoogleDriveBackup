package com.example.mihail.googledrive.business.upload.interactor;

import android.net.Uri;

import io.reactivex.Single;



public interface IUploadInteractor {

     Single<Boolean> uploadFile(Uri fileUri);
}
