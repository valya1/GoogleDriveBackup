package com.example.mihail.googledrive.business.choose_file.interactor;

import android.content.Intent;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ChooseFileInteractor implements IChooseFileInteractor {

    public ChooseFileInteractor() {

    }

    public Single<Intent> chooseFile() {
        return Single.just(new Intent(Intent.ACTION_GET_CONTENT)
                .setType("*/*")
                .addCategory(Intent.CATEGORY_OPENABLE))
                .onErrorResumeNext(throwable -> Single.error(new RuntimeException("Choose file error")))
                .subscribeOn(Schedulers.io());
    }

}
