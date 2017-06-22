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

    public Boolean deleteFile(String fileName){
        DriveApi.MetadataBufferResult result = getMetadataBufferResult(fileName);

        if (result.getStatus().isSuccess()) {
            return result.getMetadataBuffer()
                    .get(0)
                    .getDriveId()
                    .asDriveFile()
                    .delete(mGoogleApiClient)
                    .await()
                    .getStatus()
                    .isSuccess();
        }
        return null;
    }

    public OutputStream getOutputStream(){

        DriveApi.DriveContentsResult contentsResult = Drive.DriveApi
                .newDriveContents(mGoogleApiClient)
                .await();
        if (contentsResult.getStatus().isSuccess()) {
            mDriveContents = contentsResult.getDriveContents();
            return contentsResult.getDriveContents().getOutputStream();
        }
        return null;
    }

    public Boolean saveToDrive(String fileName){

        if(mDriveContents == null)
            return false;

        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(fileName)
                .build();
        return Drive.DriveApi
                .getAppFolder(mGoogleApiClient)
                .createFile(mGoogleApiClient,changeSet, mDriveContents)
                .await().getStatus().isSuccess();
    }

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
        return null;
    }


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
        return null;

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

