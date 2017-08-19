package com.example.mihail.googledrive.presentation.main.presenter;


import android.net.Uri;

import com.example.mihail.googledrive.business.upload.interactor.IUploadInteractor;
import com.example.mihail.googledrive.presentation.main.MainContract;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

    @InjectMocks
    private MainPresenter mainPresenter;

    @Mock
    private IUploadInteractor iUploadInteractor;

    @Mock
    private MainContract.View iMainView;

    public MainPresenterTest() {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(NONE -> Schedulers.trampoline());
    }


    @Test
    public void uploadFile_Success() {

        when(iUploadInteractor.uploadFile(any(Uri.class))).thenReturn(Completable.fromAction(() -> {
        }));
        mainPresenter.bindView(iMainView);
        mainPresenter.uploadFile(mock(Uri.class));

        verify(iMainView, only()).showSuccessMessage("File backup success");
        verify(iMainView, never()).showErrorMessage(anyString());
    }


    @Test
    public void uploadFile_Error() {

        RuntimeException testRuntimeException = new RuntimeException("Test");
        when(iUploadInteractor.uploadFile(any(Uri.class))).thenReturn(Completable.error(testRuntimeException));
        mainPresenter.bindView(iMainView);
        mainPresenter.uploadFile(mock(Uri.class));

        verify(iMainView, never()).showSuccessMessage("File backup success");
        verify(iMainView, only()).showErrorMessage(anyString());
    }
}