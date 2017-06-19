package com.example.mihail.googledrive.presentation.main.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.Toast;

import com.example.mihail.googledrive.BaseActivity;
import com.example.mihail.googledrive.R;
import com.example.mihail.googledrive.business.choose_file.interactor.ChooseFileInteractor;
import com.example.mihail.googledrive.business.upload.interactor.UploadInteractor;
import com.example.mihail.googledrive.data.models.GoogleDriveManager;
import com.example.mihail.googledrive.data.repository.DriveRepository;
import com.example.mihail.googledrive.presentation.delete.view.DeleteActivity;
import com.example.mihail.googledrive.presentation.download.view.DownloadActivity;
import com.example.mihail.googledrive.presentation.main.MainContract;
import com.example.mihail.googledrive.presentation.main.presenter.MainPresenter;

public class StartActivity extends BaseActivity implements MainContract.View {

    public static final int REQUEST_CODE_CHOOSE_FILE = 100;

    private MainContract.Presenter iMainPresenter;

    private Uri fileUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button chooseFileToDelete;
        Button chooseFileToDownload;
        Button uploadFile;

        setContentView(R.layout.activity_main);

        iMainPresenter = new MainPresenter(new UploadInteractor(new DriveRepository(new GoogleDriveManager(getApiGoogleClient())),this),
                new ChooseFileInteractor(this));

        chooseFileToDelete = (Button) findViewById(R.id.btnToDeleteList);
        chooseFileToDownload = (Button) findViewById(R.id.btnToDownloadList);
        uploadFile = (Button) findViewById(R.id.btnToUpload);

        chooseFileToDelete.setOnClickListener(v -> iMainPresenter.clickToDeleteActivity());
        chooseFileToDownload.setOnClickListener(v -> iMainPresenter.clickToDownloadActivity());
        uploadFile.setOnClickListener(v -> iMainPresenter.clickToUploadFile());

    }

    @Override
    protected void onStop() {
        super.onStop();
        iMainPresenter.unbindView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        iMainPresenter.bindView(this);
    }

    @Override
    public void showFilesToDownload() {
        startActivity(new Intent(this, DownloadActivity.class));
    }

    @Override
    public void showFilesToDelete() {
        startActivity(new Intent(this, DeleteActivity.class));
    }

    @Override
    public void startUploadFileAction() {
        iMainPresenter.chooseFile();
    }

    @Override
    public void showSuccessMessage() {
        Toast.makeText(this, "File upload succeeded!!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this, "FILE UPLOAD FAILED", Toast.LENGTH_LONG).show();
    }

    @Override
    public Uri getFileUri() {
        return fileUri;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    iMainPresenter.bindView(this);
                    fileUri = data.getData();
                    iMainPresenter.uploadFile();
                }
                break;
            default:
                break;
        }
    }
}