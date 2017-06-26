package com.example.mihail.googledrive.data.repository;

import android.os.Environment;
import com.example.mihail.googledrive.data.entities.FileToUpload;
import com.example.mihail.googledrive.data.models.GoogleDriveManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class DriveRepository implements IDriveRepository {

    private GoogleDriveManager mGoogleDriveManager;

    public DriveRepository(GoogleDriveManager googleDriveManager){
        this.mGoogleDriveManager = googleDriveManager;
    }

    @Override
    @NonNull
    public Completable uploadFile(FileToUpload fileToUpload) {
        return Completable.fromAction(() ->
        {
            InputStream inputStream = fileToUpload.getFileInputStream();
            OutputStream outputStream = mGoogleDriveManager.getOutputStream();
            buffer(inputStream,outputStream);
            outputStream.close();
            mGoogleDriveManager.saveToDrive(fileToUpload.getTitle());
        });
    }

    @Override
    @NonNull
    public Single<File> downloadFile(String fileName) {
        return Single.fromCallable(() -> {
            //download root
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/" + fileName);
            OutputStream outputStream = new FileOutputStream(file);
            InputStream inputStream = mGoogleDriveManager.getInputStream(fileName);
            buffer(inputStream, outputStream);
            outputStream.close();
            return file;
        });
    }

    @Override
    @NonNull
    public Completable deleteFile(String fileName) {
        return Completable.fromAction(() -> mGoogleDriveManager.deleteFile(fileName));
    }

    @Override
    @NonNull
    public Single<List<String>> getFilesList() {
        return Single.fromCallable(() -> mGoogleDriveManager.getFilesList());
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
    }
}

