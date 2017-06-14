package com.example.mihail.googledrive.data.repository;


import com.example.mihail.googledrive.data.models.FileToUpload;
import java.io.File;
import java.util.List;

import io.reactivex.Single;

public interface IDriveRepository {

    Single<Boolean> uploadFile(FileToUpload fileToUpload);
    Single<File> downloadFile(String fileName);
    Single<Boolean> deleteFile(String fileName);
    Single<List<String>> getFilesList();
}
