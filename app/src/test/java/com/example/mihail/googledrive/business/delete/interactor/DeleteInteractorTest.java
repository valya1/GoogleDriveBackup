package com.example.mihail.googledrive.business.delete.interactor;

import com.example.mihail.googledrive.data.repository.IDriveRepository;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class DeleteInteractorTest {


    @Mock
    private IDriveRepository iDriveRepository;
    @InjectMocks
    private DeleteInteractor deleteInteractor;

    public DeleteInteractorTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void deleteFileSuccess() {
        TestObserver testObserver = TestObserver.create();

        when(iDriveRepository.deleteFile("test")).thenReturn(Completable.fromAction(() -> {
        }));
        deleteInteractor.deleteFile("test").subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors().assertComplete();
    }

    @Test
    public void deleteFileFail() {
        TestObserver testObserver = TestObserver.create();

        when(iDriveRepository.deleteFile("test")).thenReturn(Completable.error(new RuntimeException("Delete err")));
        deleteInteractor.deleteFile("test").subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertError(RuntimeException.class);
    }

    @Test
    public void getListSusccess() {
        when(iDriveRepository.getFilesList()).thenReturn(Single.fromCallable(
                () -> Arrays.asList("file1.txt", "file2.doc")));

        TestObserver<List<String>> testObserver = TestObserver.create();

        deleteInteractor.getFilesList().subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors().assertComplete();
        List res = testObserver.getEvents().get(0);

        assertThat(res.get(0), CoreMatchers.is(Arrays.asList("file1.txt", "file2.doc")));
    }

    @Test
    public void getListFail() {
        TestObserver<List<String>> testObserver = TestObserver.create();
        when(iDriveRepository.getFilesList()).thenReturn(Single.error(new RuntimeException("TEST_FAIL")));
        deleteInteractor.getFilesList().subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertError(RuntimeException.class);
    }
}