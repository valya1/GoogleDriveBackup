package com.example.mihail.googledrive.presentation.main.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.widget.Toast;

import com.example.mihail.googledrive.BaseActivity;
import com.example.mihail.googledrive.R;
import com.example.mihail.googledrive.presentation.delete.view.DeleteActivity;
import com.example.mihail.googledrive.presentation.download.view.DownloadActivity;
import com.example.mihail.googledrive.presentation.main.presenter.IMainPresenter;
import com.example.mihail.googledrive.presentation.main.presenter.MainPresenter;

public class StartActivity extends BaseActivity implements IMainView {

    public static final int REQUEST_CODE_CHOOSE_FILE = 100;
    private IMainPresenter iMainPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatButton chooseFileToDelete;
        AppCompatButton chooseFileToDownload;
        AppCompatButton uploadFile;

        setContentView(R.layout.activity_main);

        iMainPresenter = new MainPresenter(getApiGoogleClient(),this);

        chooseFileToDelete = (AppCompatButton) findViewById(R.id.btnToDeleteList);
        chooseFileToDownload = (AppCompatButton) findViewById(R.id.btnToDownloadList);
        uploadFile = (AppCompatButton) findViewById(R.id.btnToUpload);

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
    protected void onResume() {
        super.onResume();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putString("Uri", uri.toString()).apply();
                    iMainPresenter.uploadFile();
                }
                break;
                default: break;
        }
    }
}