package com.example.mihail.googledrive.presentation.recycler_data;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mihail.googledrive.R;
import com.example.mihail.googledrive.presentation.recycler_data.model.IFileAdapterModel;
import com.example.mihail.googledrive.presentation.recycler_data.view.IFileAdapterView;

import java.util.ArrayList;
import java.util.List;


public class FilesAdapter extends RecyclerView.Adapter<FileViewHolder> implements IFileAdapterModel, IFileAdapterView {

    private List<String> filesList;
    private OnRecyclerItemClickListener onClickFileListener;


    public FilesAdapter() {
        super();
        filesList = new ArrayList<>();
    }


    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        String s = filesList.get(position);
        holder.fileName.setText(filesList.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (onClickFileListener != null)
                onClickFileListener.onItemClick(this, position);
        });
    }

    public void setOnClickFileListener(OnRecyclerItemClickListener onClickFileListener)
    {
        this.onClickFileListener = onClickFileListener;
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public void add(String fileName) {
        filesList.add(fileName);
    }

    @Override
    public void update(List<String> fileNames) {
        filesList.clear();
        filesList.addAll(fileNames);
    }

    @Override
    public String remove(int position) {
        return filesList.remove(position);
    }

    @Override
    public String getFileName(int position) {
        return filesList.get(position);
    }

    @Override
    public int getSize() {
        return getItemCount();
    }
}
