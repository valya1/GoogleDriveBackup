package com.example.mihail.googledrive.presentation.delete.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.mihail.googledrive.BaseActivity;
import com.example.mihail.googledrive.R;
import com.example.mihail.googledrive.business.delete.interactor.DeleteInteractor;
import com.example.mihail.googledrive.data.models.GoogleDriveManager;
import com.example.mihail.googledrive.data.repository.DriveRepository;
import com.example.mihail.googledrive.presentation.delete.DeleteContract;
import com.example.mihail.googledrive.presentation.delete.presenter.DeletePresenter;
import com.example.mihail.googledrive.presentation.recycler_data.FilesAdapter;
import com.example.mihail.googledrive.presentation.recycler_data.view.IFileAdapterView;


public class DeleteActivity extends BaseActivity implements DeleteContract.View{

    private IFileAdapterView mFileAdapterView;

    private DeleteContract.Presenter mDeletePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_view);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FilesAdapter filesAdapter = new FilesAdapter();
        mFileAdapterView = filesAdapter;

        mDeletePresenter = new DeletePresenter(new DeleteInteractor(new DriveRepository(new GoogleDriveManager(getApiGoogleClient()))), filesAdapter);

        filesAdapter.setOnClickFileListener((adapter, position) -> mDeletePresenter.deleteFile(position));

        recyclerView.setAdapter(filesAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mDeletePresenter.unbindView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDeletePresenter.bindView(this);
    }


    @Override
    public void refreshFileList() {
        mFileAdapterView.refresh();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);
        mDeletePresenter.provideData();
    }
}
