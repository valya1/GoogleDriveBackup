package com.example.mihail.googledrive.presentation.download.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.mihail.googledrive.BaseActivity;
import com.example.mihail.googledrive.R;

import com.example.mihail.googledrive.presentation.download.presenter.DownloadPresenter;
import com.example.mihail.googledrive.presentation.download.presenter.IDownloadPresenter;
import com.example.mihail.googledrive.presentation.recycler_data.FilesAdapter;
import com.example.mihail.googledrive.presentation.recycler_data.view.IFileAdapterView;

public class DownloadActivity extends BaseActivity implements IDownloadView
{
    private FilesAdapter filesAdapter;
    private IFileAdapterView iFileAdapterView;

    private  IDownloadPresenter iDownloadPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_view);

        filesAdapter = new FilesAdapter();

        iFileAdapterView = filesAdapter;

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        iDownloadPresenter = new DownloadPresenter(getApiGoogleClient(), filesAdapter);

        filesAdapter.setOnClickFileListener(( (adapter, position) -> iDownloadPresenter.downloadFile(position) ));

        recyclerView.setAdapter(filesAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        iDownloadPresenter.unbindView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iDownloadPresenter.bindView(this);
    }

    @Override
    public void refreshFiles()
    {
       iFileAdapterView.refresh();
    }

    @Override
    public void showSuccessMessage(String result) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this, "Error while file downloading", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);
        iDownloadPresenter.provideData();
    }
}
