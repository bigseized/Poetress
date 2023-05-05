package com.example.poetress.view_model;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.poetress.data.repositories.UserMainDataInteraction;
import com.example.poetress.data.types.UserMainData;

public class LoginViewModel extends ViewModel {
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
                LoginViewModel.this.data.setValue(data);
            }

            @Override
            public void onError(Exception e) {
                isLoading.setValue(false);
                error.postValue("Document does not exist");
            }
        });
    }

    public LiveData<UserMainData> getData() {
        return data;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(Boolean status){
        isLoading.setValue(status);
    }

    public LiveData<String> getError() {
        return error;
    }

    public void sendData(Uri Image, String name, String surname, String Interests){
        UserMainData userMainData = new UserMainData(Image,name,surname,Interests);
        new UserMainDataInteraction().putDataToFirebase(userMainData);
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
