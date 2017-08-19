//package com.example.mihail.googledrive.business.upload.interactor;


//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import io.reactivex.Completable;
//import io.reactivex.Single;
//import io.reactivex.observers.TestObserver;
//
//import static org.powermock.api.mockito.PowerMockito.mock;
//import static org.powermock.api.mockito.PowerMockito.when;
//
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(Uri.class)
//
//public class UploadInteractorTest {
//
//    @InjectMocks
//    UploadInteractor uploadInteractor;
//
//    @Mock
//    IDriveRepository iDriveRepository;
//
//    @Mock
//    ContentResolver contentResolver;
//
//    public UploadInteractorTest(){
//        MockitoAnnotations.initMocks(this);
//    }
//
//
//    public void uploadFile_Success() throws Exception{
//
//
//        FileToUpload testFile = mock(FileToUpload.class);
//        Uri testUri = mock(Uri.class);
//        PowerMockito.doReturn(Single.fromCallable(()->testFile)).when(uploadInteractor,"createFileToUpload", testUri);
//        when(iDriveRepository.uploadFile(testFile)).thenReturn(Completable.fromAction(()->{}));
//
//        TestObserver testObserver = TestObserver.create();
//
//        uploadInteractor.uploadFile(testUri).subscribe(testObserver);
//        testObserver.awaitTerminalEvent();
//        testObserver.assertNoErrors().assertComplete().assertValueCount(1);
//
//
//    }
//
//}