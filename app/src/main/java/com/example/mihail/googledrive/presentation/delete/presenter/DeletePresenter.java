package com.example.mihail.googledrive.presentation.delete.presenter;

import com.example.mihail.googledrive.business.delete.interactor.DeleteInteractor;
import com.example.mihail.googledrive.business.delete.interactor.IDeleteInteractor;
import com.example.mihail.googledrive.presentation.delete.view.IDeleteView;
import com.example.mihail.googledrive.presentation.recycler_data.model.IFileAdapterModel;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;



public class DeletePresenter implements IDeletePresenter {

   private IDeleteInteractor iDeleteInteractor;
   private IFileAdapterModel iFileAdapterModel;
   private IDeleteView iDeleteView;

   private CompositeDisposable compositeDisposable;

    public DeletePresenter(GoogleApiClient googleApiClient, IFileAdapterModel iFileAdapterModel)
    {
        this.iDeleteInteractor = new DeleteInteractor(googleApiClient);
        this.iFileAdapterModel = iFileAdapterModel;
        compositeDisposable = new CompositeDisposable();
    }
    @Override
    public void bindView(IDeleteView iDeleteView) {
        this.iDeleteView = iDeleteView;
    }

    @Override
    public void unbindView() {
        iDeleteView = null;
        compositeDisposable.clear();
    }

    @Override
    public void provideData() {
        compositeDisposable.add(iDeleteInteractor.getFilesList()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<String>>() {
                    @Override
                    public void onNext(@NonNull List<String> list) {
                        iFileAdapterModel.update(list);
                        iDeleteView.refreshFileList();
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        iDeleteView.showErrorMessage();
                    }
                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void deleteFile(int position) {
        compositeDisposable.add(iDeleteInteractor
                .deleteFile(iFileAdapterModel.getFileName(position))
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean result) {
                        if(result) {
                            iFileAdapterModel.remove(position);
                            iDeleteView.showSuccessMessage();
                            iDeleteView.refreshFileList();
                        }
                        else
                            iDeleteView.showErrorMessage();
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }
}
