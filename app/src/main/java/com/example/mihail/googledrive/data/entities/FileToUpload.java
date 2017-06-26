package com.example.mihail.googledrive.data.entities;

import java.io.InputStream;

public class FileToUpload
{
    private String mTitle;
    private InputStream mFileInputStream;

    public FileToUpload(String title, InputStream fileInputStream) {
        this.mTitle = title;
        this.mFileInputStream = fileInputStream;
    }

    public String getTitle() {
        return mTitle;
    }

    public InputStream getFileInputStream() {
        return mFileInputStream;
    }

}
