package com.example.mihail.googledrive.business.upload.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;

import com.example.mihail.googledrive.data.models.FileToUpload;
import com.example.mihail.googledrive.data.repository.DriveRepository;
import com.example.mihail.googledrive.data.repository.IDriveRepository;
import com.google.android.gms.common.api.GoogleApiClient;


import java.io.IOException;

import io.reactivex.Single;


public class UploadInteractor implements IUploadInteractor {

    private IDriveRepository driveRepository;
    private Context context;

    public UploadInteractor(GoogleApiClient googleApiClient, Context context)
    {
        driveRepository = new DriveRepository(googleApiClient);
        this.context = context;
    }
    @Override
    public Single<Boolean> uploadFile() {
        return createFileToUpload()
                .flatMap(driveRepository::uploadFile)
                .onErrorResumeNext(throwable -> Single.error(new RuntimeException("Unable to upload file")));
    }

    private Single<FileToUpload> createFileToUpload()
    {
        return Single.fromCallable(() -> {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if(preferences.contains("Uri")){
                Uri uri = Uri.parse(preferences.getString("Uri",null));
                try {
                    return new FileToUpload(getTitleFromUri(uri), context.getContentResolver().openInputStream(uri));
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            return new FileToUpload();
        });
    }

    private String getTitleFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
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
