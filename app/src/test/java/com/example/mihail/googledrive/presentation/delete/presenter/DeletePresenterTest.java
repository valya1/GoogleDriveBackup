package com.example.mihail.googledrive.presentation.delete.presenter;

import com.example.mihail.googledrive.business.delete.interactor.IDeleteInteractor;
import com.example.mihail.googledrive.presentation.delete.DeleteContract;
import com.example.mihail.googledrive.presentation.recycler_data.model.IFileAdapterModel;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class DeletePresenterTest {

    @InjectMocks
    private DeletePresenter deletePresenter;

    @Mock
    private DeleteContract.View iDeleteView;

    @Mock
    private IDeleteInteractor iDeleteInteractor;

    @Mock
    private IFileAdapterModel iFileAdapterModel;

    private List<String> testList;

    public DeletePresenterTest() {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
        this.testList = Arrays.asList("file1.txt", "file2.doc");
    }

    @Test
    public void loadFileList_Success() {

        when(iDeleteInteractor.getFilesList()).thenReturn(Single.fromCallable(() -> testList));

        deletePresenter.bindView(iDeleteView);
        deletePresenter.provideData();

        InOrder inOrder = inOrder(iFileAdapterModel, iDeleteView);

        inOrder.verify(iFileAdapterModel).update(testList);
        inOrder.verify(iDeleteView).refreshFileList();

        verify(iDeleteView, never()).showErrorMessage(anyString());

    }

    @Test
    public void loadFileList_Error() {

        RuntimeException exception = new RuntimeException("Test error");

        when(iDeleteInteractor.getFilesList()).thenReturn(Single.error(exception));

        deletePresenter.bindView(iDeleteView);
        deletePresenter.provideData();

        verify(iDeleteInteractor, atLeastOnce()).getFilesList();

        verify(iFileAdapterModel, never()).update(testList);
        verify(iDeleteView, never()).refreshFileList();

        verify(iDeleteView).showErrorMessage(exception.getMessage());

    }

    @Test
    public void deleteFile_Success() {

        when(iDeleteInteractor.getFilesList()).thenReturn(Single.fromCallable(() -> testList));
        when(iDeleteInteractor.deleteFile("file1.txt")).thenReturn(Completable.fromAction(() -> {
        }));
        when(iFileAdapterModel.getFileName(0)).thenReturn("file1.txt");

        deletePresenter.bindView(iDeleteView);
        deletePresenter.deleteFile(0);

        verify(iDeleteInteractor).deleteFile("file1.txt");

        InOrder inOrder = inOrder(iFileAdapterModel, iDeleteView);
        inOrder.verify(iFileAdapterModel).remove(0);
        inOrder.verify(iDeleteView).showSuccessMessage("File was successfully deleted");
        inOrder.verify(iDeleteView).refreshFileList();

        verify(iDeleteView, never()).showErrorMessage(anyString());
    }

    @Test
    public void deleteFile_Error() {

        RuntimeException runtimeException = new RuntimeException("Test Error");
        when(iDeleteInteractor.getFilesList()).thenReturn(Single.fromCallable(() -> testList));
        when(iDeleteInteractor.deleteFile("file1.txt")).thenReturn(Completable.error(runtimeException));
        when(iFileAdapterModel.getFileName(0)).thenReturn("file1.txt");

        deletePresenter.bindView(iDeleteView);
        deletePresenter.deleteFile(0);

        verify(iDeleteInteractor).deleteFile("file1.txt");
        verify(iFileAdapterModel, never()).remove(anyInt());
        verify(iDeleteView, never()).showSuccessMessage(anyString());
        verify(iDeleteView, never()).refreshFileList();

        verify(iDeleteView).showErrorMessage(runtimeException.getMessage());
    }

}