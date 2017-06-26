package com.example.mihail.googledrive.business.upload.interactor;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import com.example.mihail.googledrive.data.entities.FileToUpload;
import com.example.mihail.googledrive.data.repository.IDriveRepository;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class UploadInteractor implements IUploadInteractor {

    private IDriveRepository mDriveRepository;
    private ContentResolver mContentResolver;

    public UploadInteractor(IDriveRepository driveRepository, ContentResolver contentResolver)
    {
        this.mDriveRepository = driveRepository;
        this.mContentResolver = contentResolver;
    }
    @Override
    public Completable uploadFile(Uri fileUri) {
        return createFileToUpload(fileUri)
                .flatMapCompletable(mDriveRepository::uploadFile)
                .onErrorResumeNext(throwable -> Completable.error(new RuntimeException("Unable to upload file")))
                .subscribeOn(Schedulers.io());
    }

    private Single<FileToUpload> createFileToUpload(Uri fileUri) {
        return Single.fromCallable(() -> new FileToUpload(getTitleFromUri(fileUri), mContentResolver.openInputStream(fileUri)));
    }

    private String getTitleFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = mContentResolver.query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
