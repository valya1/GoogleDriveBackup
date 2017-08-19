package com.example.mihail.googledrive.business.download.interactor;

import com.example.mihail.googledrive.data.repository.IDriveRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class DownloadInteractorTest {

    @Mock
    private IDriveRepository iDriveRepository;

    @InjectMocks
    private DownloadInteractor downloadInteractor;

    public DownloadInteractorTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void downloadFileSuccess() {

        TestObserver<File> testObserver = TestObserver.create();
        when(iDriveRepository.downloadFile("test")).thenReturn(Single.fromCallable(() ->
                new File("Downloads/test")));

        downloadInteractor.downloadFile("test").subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors().assertComplete();
        assertThat(testObserver.values().get(0).getAbsolutePath(), is("D:\\Downloads\\Telegram Desktop\\GoogleDriveBackup\\GoogleDriveBackup\\Downloads\\test"));
    }


    @Test
    public void getListSusccess() {

        TestObserver<List<String>> testObserver = TestObserver.create();
        when(iDriveRepository.getFilesList()).thenReturn(Single.fromCallable(
                () -> Arrays.asList("file1.txt", "file2.doc")));

        downloadInteractor.getFilesList().subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors().assertComplete();
        List res = testObserver.getEvents().get(0);
        assertThat(res.get(0), is(Arrays.asList("file1.txt", "file2.doc")));
    }

    @Test
    public void getListFail() {
        TestObserver<List<String>> testObserver = TestObserver.create();

        when(iDriveRepository.getFilesList()).thenReturn(Single.error(new RuntimeException("TEST_FAIL")));
        downloadInteractor.getFilesList().subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertError(RuntimeException.class);
    }
}