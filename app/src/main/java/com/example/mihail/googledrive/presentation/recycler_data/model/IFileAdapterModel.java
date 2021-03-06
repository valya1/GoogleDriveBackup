package com.example.mihail.googledrive.presentation.recycler_data.model;

import java.util.List;


public interface IFileAdapterModel {

    void add(String fileName);
    void update(List<String> fileNames);
    String remove(int position);
    String getFileName(int position);
    int getSize();
}
