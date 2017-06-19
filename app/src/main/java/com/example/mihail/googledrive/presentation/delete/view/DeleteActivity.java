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

    private IFileAdapterView iFileAdapterView;

    private DeleteContract.Presenter iDeletePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_view);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FilesAdapter filesAdapter = new FilesAdapter();
        iFileAdapterView = filesAdapter;

        iDeletePresenter = new DeletePresenter(new DeleteInteractor(new DriveRepository(new GoogleDriveManager(getApiGoogleClient()))), filesAdapter);

        filesAdapter.setOnClickFileListener((adapter, position) -> iDeletePresenter.deleteFile(position));

        recyclerView.setAdapter(filesAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        iDeletePresenter.unbindView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iDeletePresenter.bindView(this);
    }

    @Override
    public void showSuccessMessage() {
        Toast.makeText(this, "File was deleted successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public void refreshFileList() {
        iFileAdapterView.refresh();
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this,"Error while file deleting", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);
        iDeletePresenter.provideData();
    }
}
