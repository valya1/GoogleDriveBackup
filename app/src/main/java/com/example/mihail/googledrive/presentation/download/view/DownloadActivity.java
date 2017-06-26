package com.example.mihail.googledrive.presentation.download.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.mihail.googledrive.BaseActivity;
import com.example.mihail.googledrive.R;

import com.example.mihail.googledrive.business.download.interactor.DownloadInteractor;
import com.example.mihail.googledrive.data.models.GoogleDriveManager;
import com.example.mihail.googledrive.data.repository.DriveRepository;
import com.example.mihail.googledrive.presentation.download.DownloadContract;
import com.example.mihail.googledrive.presentation.download.presenter.DownloadPresenter;
import com.example.mihail.googledrive.presentation.recycler_data.FilesAdapter;
import com.example.mihail.googledrive.presentation.recycler_data.view.IFileAdapterView;

public class DownloadActivity extends BaseActivity implements DownloadContract.View
{
    private IFileAdapterView mFileAdapterView;

    private  DownloadContract.Presenter mDownloadPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_view);

        FilesAdapter filesAdapter = new FilesAdapter();

        mFileAdapterView = filesAdapter;

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDownloadPresenter = new DownloadPresenter(new DownloadInteractor(new DriveRepository(new GoogleDriveManager(getApiGoogleClient()))), filesAdapter);

        filesAdapter.setOnClickFileListener( (adapter, position) -> mDownloadPresenter.downloadFile(position) );

        recyclerView.setAdapter(filesAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mDownloadPresenter.unbindView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDownloadPresenter.bindView(this);
    }

    @Override
    public void refreshFiles()
    {
        mFileAdapterView.refresh();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);
        mDownloadPresenter.provideData();
    }
}
