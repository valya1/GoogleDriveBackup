package com.example.mihail.googledrive.presentation.delete.presenter;

import com.example.mihail.googledrive.business.delete.interactor.IDeleteInteractor;
import com.example.mihail.googledrive.presentation.delete.DeleteContract;
import com.example.mihail.googledrive.presentation.recycler_data.model.IFileAdapterModel;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class DeletePresenter implements DeleteContract.Presenter {

    private IDeleteInteractor mDeleteInteractor;
    private IFileAdapterModel mFileAdapterModel;
    private DeleteContract.View mDeleteView;

    public DeletePresenter(IDeleteInteractor iDeleteInteractor, IFileAdapterModel iFileAdapterModel)
    {
        this.mDeleteInteractor = iDeleteInteractor;
        this.mFileAdapterModel = iFileAdapterModel;
    }
    @Override
    public void bindView(DeleteContract.View iDeleteView) {
        this.mDeleteView = iDeleteView;
    }

    @Override
    public void unbindView() {
        mDeleteView = null;
    }

    @Override
    public void provideData() {
        mDeleteInteractor.getFilesList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if(mFileAdapterModel!=null) mFileAdapterModel.update(list);
                    if(mDeleteView!=null) mDeleteView.refreshFileList();
                    }, throwable -> {
                    if(mDeleteView!=null) mDeleteView.showErrorMessage(throwable.getMessage());
                });
    }

    @Override
    public void deleteFile(int position) {
            mDeleteInteractor
                    .deleteFile(mFileAdapterModel.getFileName(position))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(()-> {
                if(mDeleteView!=null){
                    mFileAdapterModel.remove(position);
                    mDeleteView.showSuccessMessage("File was successfully deleted");
                    mDeleteView.refreshFileList();
                }
                    },throwable -> {
                if(mDeleteView!=null) mDeleteView.showErrorMessage(throwable.getMessage());
            });
    }
}