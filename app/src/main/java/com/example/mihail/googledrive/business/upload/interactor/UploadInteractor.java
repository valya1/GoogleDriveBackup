package com.example.mihail.googledrive.business.upload.interactor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import com.example.mihail.googledrive.data.models.FileToUpload;
import com.example.mihail.googledrive.data.repository.IDriveRepository;

import java.io.IOException;

import io.reactivex.Single;


public class UploadInteractor implements IUploadInteractor {

    private IDriveRepository driveRepository;
    //TODO Интерактор это изнеслогикак какой Контекст??
    private Context context;

    public UploadInteractor(IDriveRepository iDriveRepository, Context context)
    {
        this.driveRepository = iDriveRepository;
        this.context = context;
    }
    @Override
    public Single<Boolean> uploadFile(Uri fileUri) {
        return createFileToUpload(fileUri)
                .flatMap(driveRepository::uploadFile)
                .onErrorResumeNext(throwable -> Single.error(new RuntimeException("Unable to upload file")));
    }

    private Single<FileToUpload> createFileToUpload(Uri fileUri)
    {
        return Single.fromCallable(() -> {
            try {
                return new FileToUpload(getTitleFromUri(fileUri), context.getContentResolver().openInputStream(fileUri));
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return new FileToUpload();
        });
    }

    private String getTitleFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
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
