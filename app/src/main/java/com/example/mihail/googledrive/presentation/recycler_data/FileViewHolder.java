package com.example.mihail.googledrive.presentation.recycler_data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mihail.googledrive.R;


class FileViewHolder extends RecyclerView.ViewHolder {

    final TextView mFileName;

     FileViewHolder(View itemView) {
        super(itemView);
        mFileName = (TextView) itemView.findViewById(R.id.tvFileName);
    }
}
