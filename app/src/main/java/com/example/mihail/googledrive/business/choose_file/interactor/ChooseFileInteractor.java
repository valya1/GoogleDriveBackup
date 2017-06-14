package com.example.mihail.googledrive.business.choose_file.interactor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import static com.example.mihail.googledrive.presentation.main.view.StartActivity.REQUEST_CODE_CHOOSE_FILE;


public class ChooseFileInteractor implements IChooseFileInteractor {

    private Context context;


    public ChooseFileInteractor(Context context) {
        this.context = context;
    }

    public void chooseFile() {
        ((Activity)context).startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT)
                .setType("*/*")
                .addCategory(Intent.CATEGORY_OPENABLE), REQUEST_CODE_CHOOSE_FILE);
    }

}
