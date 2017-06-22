package com.example.mihail.googledrive.presentation.delete.presenter;

import com.example.mihail.googledrive.business.delete.interactor.IDeleteInteractor;
import com.example.mihail.googledrive.presentation.delete.DeleteContract;
import com.example.mihail.googledrive.presentation.recycler_data.model.IFileAdapterModel;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;


public class DeletePresenter implements DeleteContract.Presenter {

    private IDeleteInteractor mDeleteInteractor;
    private IFileAdapterModel mFileAdapterModel;
    private DeleteContract.View mDeleteView;

    private CompositeDisposable compositeDisposable;

    public DeletePresenter(IDeleteInteractor iDeleteInteractor, IFileAdapterModel iFileAdapterModel)
    {
        this.mDeleteInteractor = iDeleteInteractor;
        this.mFileAdapterModel = iFileAdapterModel;
        compositeDisposable = new CompositeDisposable();
    }
    @Override
    public void bindView(DeleteContract.View iDeleteView) {
        this.mDeleteView = iDeleteView;
    }

    @Override
    public void unbindView() {
        mDeleteView = null;
        compositeDisposable.clear();
    }

    @Override
    public void provideData() {
        compositeDisposable.add(mDeleteInteractor.getFilesList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(List<String> list) {
                        if(mFileAdapterModel!=null)
                            mFileAdapterModel.update(list);
                        if(mDeleteView!=null)
                            mDeleteView.refreshFileList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(mDeleteView!=null)
                            mDeleteView.showErrorMessage(e.getMessage());
                    }
                }));
    }

    @Override
    public void deleteFile(int position) {
        compositeDisposable.add(mDeleteInteractor
                .deleteFile(mFileAdapterModel.getFileName(position))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Boolean>() {

                    @Override
                    public void onSuccess(Boolean result) {
                        if(mDeleteView !=null) {
                            if (result) {
                                mFileAdapterModel.remove(position);
                                mDeleteView.showSuccessMessage("File was successfully deleted");
                                mDeleteView.refreshFileList();
                            } else
                                mDeleteView.showErrorMessage("Error while file uploading");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                })
        );
    }
}