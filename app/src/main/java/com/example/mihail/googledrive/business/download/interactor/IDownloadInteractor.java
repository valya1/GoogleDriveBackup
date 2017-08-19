package com.example.mihail.googledrive.business.download.interactor;


import java.io.File;
import java.util.List;

import io.reactivex.Single;


public interface IDownloadInteractor {

    Single<List<String>> getFilesList();

    Single<File> downloadFile(String fileName);
}
