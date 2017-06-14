package com.example.mihail.googledrive.data.repository;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.example.mihail.googledrive.data.models.FileToUpload;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.Nullable;

public class DriveRepository implements IDriveRepository {

    private GoogleApiClient mGoogleApiClient;

    public DriveRepository(GoogleApiClient mGoogleApiClient){
        this.mGoogleApiClient = mGoogleApiClient;
        if(mGoogleApiClient.isConnected())
            Drive.DriveApi.requestSync(mGoogleApiClient).await();
    }
@Override
@Nullable
public Single<Boolean> uploadFile(FileToUpload fileToUpload) {
    return Single.fromCallable(() ->
    {
        DriveApi.DriveContentsResult contentsResult = Drive.DriveApi
                .newDriveContents(mGoogleApiClient)
                .await();

        if (contentsResult.getStatus().isSuccess()) {

            InputStream inputStream = fileToUpload.getFileInputStream();

            DriveContents driveContents = contentsResult.getDriveContents();
            OutputStream outputStream = driveContents.getOutputStream();

            buffer(inputStream,outputStream);

            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle(fileToUpload.getTitle())
                    .build();
            return Drive.DriveApi
                    .getAppFolder(mGoogleApiClient)
                    .createFile(mGoogleApiClient,changeSet,driveContents)
                    .await().getStatus().isSuccess();
        }
        return null;
    });
}

    @Override
    @Nullable
    public Single<File> downloadFile(String fileName) {
        return Single.fromCallable(() -> {
            DriveApi.MetadataBufferResult result = getMetadataBufferResult(fileName);

            if(result.getStatus().isSuccess()) {
                Metadata metadata = result.getMetadataBuffer().get(0);
                DriveFile driveFile = metadata.getDriveId().asDriveFile();

                DriveApi.DriveContentsResult contentsResult = driveFile
                        .open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null)
                        .await();

                if (contentsResult.getStatus().isSuccess()) {
                    //download root
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            + "/" + metadata.getTitle());
                    DriveContents contents = contentsResult.getDriveContents();
                    OutputStream outputStream = new FileOutputStream(file);
                    InputStream inputStream = contents.getInputStream();

                    buffer(inputStream,outputStream);

                    return file;
                }
            }
            return null;
        });
    }

    @Override
    @Nullable
    public Single<Boolean> deleteFile(String fileName) {
        return Single.fromCallable(() -> {

            DriveApi.MetadataBufferResult result = getMetadataBufferResult(fileName);

            if (result.getStatus().isSuccess()) {
                return result.getMetadataBuffer()
                        .get(0)
                        .getDriveId()
                        .asDriveFile()
                        .delete(mGoogleApiClient)
                        .await()
                        .isSuccess();
            }
            return null;
        });
    }

    @Override
    @Nullable
    public Single<List<String>> getFilesList() {
        return Single.fromCallable(() -> {
                List<String> list;
                DriveApi.MetadataBufferResult result = Drive.DriveApi
                        .getAppFolder(mGoogleApiClient)
                        .listChildren(mGoogleApiClient)
                        .await();

                if (result.getStatus().isSuccess()) {
                    list = new ArrayList<>();
                    MetadataBuffer buffer = result.getMetadataBuffer();

                    for (Metadata m : buffer) {
                        list.add(m.getTitle());}
                    buffer.release();
                    return  list;
                }
                    return null;
        });
    }

    @NonNull
    private DriveApi.MetadataBufferResult getMetadataBufferResult(String fileName){

        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, fileName))
                .build();
        return Drive.DriveApi
                .query(mGoogleApiClient, query)
                .await();
    }

    private void buffer(InputStream inputStream, OutputStream outputStream) throws IOException{
        int bufferSize, maxBufferSize = 1024;
        byte[] buffer;
        bufferSize = Math.min(maxBufferSize, inputStream.available());
        buffer = new byte[bufferSize];
            while (inputStream.read(buffer,0,bufferSize) > 0){
                outputStream.write(buffer,0,bufferSize);
                bufferSize = Math.min(inputStream.available(), maxBufferSize);
            }
            outputStream.close();
    }
}
