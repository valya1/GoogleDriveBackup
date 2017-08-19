package com.example.mihail.googledrive.presentation.download.presenter;


import com.example.mihail.googledrive.business.download.interactor.IDownloadInteractor;
import com.example.mihail.googledrive.presentation.download.DownloadContract;
import com.example.mihail.googledrive.presentation.recycler_data.model.IFileAdapterModel;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DownloadPresenterTest {


    @InjectMocks
    private DownloadPresenter downloadPresenter;

    @Mock
    private DownloadContract.View iDownloadView;

    @Mock
    private IDownloadInteractor iDownloadInteractor;

    @Mock
    private IFileAdapterModel iFileAdapterModel;

    private List<String> testList;


    public DownloadPresenterTest() {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
        this.testList = Arrays.asList("file1.txt", "file2.doc");
    }


    @Test
    public void downloadFile_Success() {

        File testFile = spy(new File("test"));
        when(iDownloadInteractor.downloadFile("test.txt")).thenReturn(Single.fromCallable(
                () -> testFile));

        when(testFile.getAbsolutePath()).thenReturn("test");
        when(iFileAdapterModel.getFileName(0)).thenReturn("test.txt");

        downloadPresenter.bindView(iDownloadView);
        downloadPresenter.downloadFile(0);

        verify(iDownloadView, only()).showSuccessMessage(testFile.getAbsolutePath());
        verify(iDownloadView, never()).showErrorMessage(anyString());
    }

    @Test
    public void downloadFile_Error() {

        RuntimeException testRuntimeException = new RuntimeException("Test");
        when(iDownloadInteractor.downloadFile("test.txt")).thenReturn(Single.error(testRuntimeException));

        when(iFileAdapterModel.getFileName(0)).thenReturn("test.txt");

        downloadPresenter.bindView(iDownloadView);
        downloadPresenter.downloadFile(0);

        verify(iDownloadView, never()).showSuccessMessage(anyString());
        verify(iDownloadView, only()).showErrorMessage(testRuntimeException.getMessage());
    }


    @Test
    public void loadFileList_Success() {

        when(iDownloadInteractor.getFilesList()).thenReturn(Single.fromCallable(() -> testList));

        downloadPresenter.bindView(iDownloadView);
        downloadPresenter.provideData();

        verify(iDownloadInteractor, atLeastOnce()).getFilesList();

        InOrder inOrder = inOrder(iFileAdapterModel, iDownloadView);

        inOrder.verify(iFileAdapterModel).update(testList);
        inOrder.verify(iDownloadView).refreshFiles();

        verify(iDownloadView, never()).showErrorMessage(anyString());

    }

    @Test
    public void loadFileList_Error() {

        RuntimeException exception = new RuntimeException("Test error");

        when(iDownloadInteractor.getFilesList()).thenReturn(Single.error(exception));

        downloadPresenter.bindView(iDownloadView);
        downloadPresenter.provideData();

        verify(iDownloadInteractor, atLeastOnce()).getFilesList();

        verify(iFileAdapterModel, never()).update(testList);
        verify(iDownloadView, never()).refreshFiles();

        verify(iDownloadView).showErrorMessage(exception.getMessage());

    }
}