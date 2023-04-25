package com.example.poetress.view_model;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.poetress.data.data_sources.UserMainData;
import com.example.poetress.data.repositories.UserMainDataInteraction;


public class ProfileMainViewModel extends ViewModel {
    private UserMainDataInteraction repository = new UserMainDataInteraction();
    private MutableLiveData<UserMainData> data = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

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
