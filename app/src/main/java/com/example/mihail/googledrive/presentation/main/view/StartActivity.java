package com.example.mihail.googledrive.presentation.main.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.mihail.googledrive.BaseActivity;
import com.example.mihail.googledrive.R;
import com.example.mihail.googledrive.business.upload.interactor.UploadInteractor;
import com.example.mihail.googledrive.data.models.GoogleDriveManager;
import com.example.mihail.googledrive.data.repository.DriveRepository;
import com.example.mihail.googledrive.presentation.delete.view.DeleteActivity;
import com.example.mihail.googledrive.presentation.download.view.DownloadActivity;
import com.example.mihail.googledrive.presentation.main.MainContract;
import com.example.mihail.googledrive.presentation.main.presenter.MainPresenter;


public class StartActivity extends BaseActivity implements MainContract.View {

    public static final int REQUEST_CODE_CHOOSE_FILE = 100;
    private MainContract.Presenter mMainPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View chooseFileToDelete;
        View chooseFileToDownload;
        View uploadFile;

        setContentView(R.layout.activity_main);

        mMainPresenter = new MainPresenter(new UploadInteractor(new DriveRepository(new GoogleDriveManager(getApiGoogleClient()))
                ,getContentResolver()));

        chooseFileToDelete = findViewById(R.id.btnToDeleteList);
        chooseFileToDownload = findViewById(R.id.btnToDownloadList);
        uploadFile = findViewById(R.id.btnToUpload);

        View.OnClickListener onClickListener = v -> {
                switch (v.getId()){
                    case R.id.btnToDeleteList:
                        mMainPresenter.clickToDeleteActivity();
                        break;
                    case R.id.btnToDownloadList:
                        mMainPresenter.clickToDownloadActivity();
                        break;
                    case R.id.btnToUpload:
                        mMainPresenter.clickToUploadFile();
                }
            };

        chooseFileToDelete.setOnClickListener(onClickListener);
        chooseFileToDownload.setOnClickListener(onClickListener);
        uploadFile.setOnClickListener(onClickListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mMainPresenter.unbindView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMainPresenter.bindView(this);
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
        mMainPresenter.chooseFile();
    }

    @Override
    public void chooseFileActivity(Intent intent){
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    mMainPresenter.uploadFile(data.getData());
                }
                break;
            default:
                break;
        }
    }
}