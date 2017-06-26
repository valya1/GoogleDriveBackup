package com.example.mihail.googledrive.data.repository;


import com.example.mihail.googledrive.data.entities.FileToUpload;
import java.io.File;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface IDriveRepository {

    Completable uploadFile(FileToUpload fileToUpload);
    Single<File> downloadFile(String fileName);
    Completable deleteFile(String fileName);
    Single<List<String>> getFilesList();
}
