package com.example.mihail.googledrive.data.models;

import android.support.annotation.NonNull;

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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class GoogleDriveManager {

    private GoogleApiClient mGoogleApiClient;

    private DriveContents mDriveContents;

    public GoogleDriveManager(GoogleApiClient googleApiClient) {
        this.mGoogleApiClient = googleApiClient;
        if(googleApiClient.isConnected())
            Drive.DriveApi.requestSync(googleApiClient).await();
    }

    public Void deleteFile(String fileName){
        DriveApi.MetadataBufferResult result = getMetadataBufferResult(fileName);

        if (result.getStatus().isSuccess()) {
            if(result.getMetadataBuffer()
                    .get(0)
                    .getDriveId()
                    .asDriveFile()
                    .delete(mGoogleApiClient)
                    .await()
                    .getStatus()
                    .isSuccess())
                return null;
        }
        throw new RuntimeException(result.getStatus().getStatusMessage());
    }

    public OutputStream getOutputStream(){

        DriveApi.DriveContentsResult contentsResult = Drive.DriveApi
                .newDriveContents(mGoogleApiClient)
                .await();
        if (contentsResult.getStatus().isSuccess()) {
            mDriveContents = contentsResult.getDriveContents();
            return contentsResult.getDriveContents().getOutputStream();
        }
        throw new RuntimeException("Unable to get a File from Drive");
    }

    public Void saveToDrive(String fileName){

        if(mDriveContents == null)
            throw new NullPointerException("Null file is trying to be uploaded to Drive");

        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(fileName)
                .build();

            if( !Drive.DriveApi
                .getAppFolder(mGoogleApiClient)
                .createFile(mGoogleApiClient,changeSet, mDriveContents)
                .await().getStatus().isSuccess())
                throw new RuntimeException("Unable to save file on Drive");
            return null;
    }

    @NonNull
    public InputStream getInputStream(String fileName) {
        DriveApi.MetadataBufferResult result = getMetadataBufferResult(fileName);

        if (result.getStatus().isSuccess()) {
            Metadata metadata = result.getMetadataBuffer().get(0);
            DriveFile driveFile = metadata.getDriveId().asDriveFile();

            DriveApi.DriveContentsResult contentsResult = driveFile
                    .open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null)
                    .await();
            if(contentsResult.getStatus().isSuccess())
                return contentsResult.getDriveContents().getInputStream();
        }
        throw new RuntimeException("Unable to create file in Drive");
    }

    @NonNull
    public List<String> getFilesList(){

        DriveApi.MetadataBufferResult result = Drive.DriveApi
                .getAppFolder(mGoogleApiClient)
                .listChildren(mGoogleApiClient)
                .await();

        if (result.getStatus().isSuccess()) {
            List<String> list = new ArrayList<>();
            MetadataBuffer buffer = result.getMetadataBuffer();

            for (Metadata m : buffer)
                list.add(m.getTitle());

            buffer.release();
            return  list;
        }
        throw new RuntimeException("Unable to get file list from Drive");

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

}

