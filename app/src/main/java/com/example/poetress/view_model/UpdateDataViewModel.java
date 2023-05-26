package com.example.poetress.view_model;

import android.net.Uri;
import android.text.BoringLayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.poetress.data.types.UserMainData;
import com.example.poetress.data.repositories.UserMainDataInteraction;


public class UpdateDataViewModel extends ViewModel {
    private UserMainDataInteraction repository;
    private MutableLiveData<UserMainData> data = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSended = new MutableLiveData<>();

    public UpdateDataViewModel() {
        repository = new UserMainDataInteraction();
        isSended = repository.getIsSended();
    }

    public void loadData() {
        isLoading.setValue(true);
        repository.getDataFromFirebase(new UserMainDataInteraction.OnFirestoreDataCallback<UserMainData>() {
            @Override
            public void onCallback(UserMainData data) {
                isLoading.setValue(false);
                UpdateDataViewModel.this.data.setValue(data);
            }

            @Override
            public void onError(Exception e) {
                isLoading.setValue(false);
                error.setValue(e.getMessage());
            }
        });
    }

    public MutableLiveData<Boolean> getIsSended(){
        return isSended;
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

    public void sendData(Uri Image, String name, String surname, String Interests){
        UserMainData userMainData = new UserMainData(Image,name,surname,Interests);
        repository.putDataToFirebase(userMainData);
    }

    public boolean checkData(UserMainData userMainData){
        if (userMainData.getName().isEmpty()){
            return false;
        }
        if (userMainData.getSurname().isEmpty()){
            return false;
        }
        return true;
    }
}
