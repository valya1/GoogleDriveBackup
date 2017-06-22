package com.example.mihail.googledrive.data.entities;

import java.io.InputStream;

public class FileToUpload
{
    private String mTitle;
    private InputStream mFileInputStream;

    public FileToUpload() {
    }

    public FileToUpload(String title, InputStream fileInputStream) {
        this.mTitle = title;
        this.mFileInputStream = fileInputStream;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public InputStream getFileInputStream() {
        return mFileInputStream;
    }

    public void setFileInputStream(InputStream mFileInputStream) {
        this.mFileInputStream = mFileInputStream;
    }
}
