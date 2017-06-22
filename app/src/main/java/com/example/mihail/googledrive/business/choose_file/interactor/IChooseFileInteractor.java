package com.example.mihail.googledrive.business.choose_file.interactor;

import android.content.Intent;

import io.reactivex.Single;



public interface IChooseFileInteractor {

        Single<Intent> chooseFile();
}
