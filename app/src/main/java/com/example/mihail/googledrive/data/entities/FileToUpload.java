package com.example.mihail.googledrive.data.entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileToUpload
{
    private String title;

    public FileToUpload() {
    }

    private InputStream fileInputStream;

    public FileToUpload(String title, InputStream fileInputStream) {
        this.title = title;
        this.fileInputStream = fileInputStream;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(InputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }
}
