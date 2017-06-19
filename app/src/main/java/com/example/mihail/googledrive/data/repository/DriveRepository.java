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
import io.reactivex.Single;
import io.reactivex.annotations.Nullable;

public class DriveRepository implements IDriveRepository {

    private GoogleDriveManager googleDriveManager;

    public DriveRepository(GoogleDriveManager googleDriveManager){
        this.googleDriveManager = googleDriveManager;
    }

    @Override
    @Nullable
    public Single<Boolean> uploadFile(FileToUpload fileToUpload) {
        return Single.fromCallable(() ->
        {
            InputStream inputStream = fileToUpload.getFileInputStream();
            OutputStream outputStream = googleDriveManager.getOutputStream();
            buffer(inputStream,outputStream);
            return googleDriveManager.saveToDrive(fileToUpload.getTitle());
        });
    }

    @Override
    @Nullable
    public Single<File> downloadFile(String fileName) {
        return Single.fromCallable(() -> {
            //download root
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/" + fileName);
            OutputStream outputStream = new FileOutputStream(file);
            InputStream inputStream = googleDriveManager.getInputStream(fileName);
            if(inputStream != null) {
                buffer(inputStream, outputStream);
                return file;
            }
            outputStream.close();
            return null;
        });
    }

    @Override
    @Nullable
    public Single<Boolean> deleteFile(String fileName) {
        return Single.fromCallable(() -> googleDriveManager.deleteFile(fileName));
    }

    @Override
    @Nullable
    public Single<List<String>> getFilesList() {
        return Single.fromCallable(() ->  googleDriveManager.getFilesList());
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

