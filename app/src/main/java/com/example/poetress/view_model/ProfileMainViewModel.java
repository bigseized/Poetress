package com.example.poetress.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.poetress.data.types.AdditionVerseInfo;
import com.example.poetress.data.types.UserMainData;
import com.example.poetress.data.repositories.UserMainDataInteraction;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;


public class ProfileMainViewModel extends ViewModel {
    private UserMainDataInteraction repository = new UserMainDataInteraction();
    private MutableLiveData<UserMainData> data = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleted = new MutableLiveData<>();
    private MutableLiveData<List<AdditionVerseInfo>> additionVersesInfoData = new MutableLiveData<>();

    public ProfileMainViewModel() {
        repository.loadAdditionInfo();
        additionVersesInfoData = repository.getAdditionVerseLD();
    }

    public void loadData() {
        isLoading.setValue(true);
        repository.getDataFromFirebase(new UserMainDataInteraction.OnFirestoreDataCallback<UserMainData>() {
            @Override
            public void onCallback(UserMainData data) {
                isLoading.setValue(false);
                ProfileMainViewModel.this.data.setValue(data);
            }

            @Override
            public void onError(Exception e) {
                isLoading.setValue(false);
                error.setValue(e.getMessage());
            }
        });
    }

    public MutableLiveData<List<AdditionVerseInfo>> getAdditionVersesInfoData() {
        return additionVersesInfoData;
    }

    public void deleteVerse(String documentId, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        repository.deleteVerse(documentId, onSuccessListener, onFailureListener);
    }

    public LiveData<Boolean> getStatus(){
        return deleted;
    }

    public LiveData<UserMainData> getData() {
        return data;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

}
