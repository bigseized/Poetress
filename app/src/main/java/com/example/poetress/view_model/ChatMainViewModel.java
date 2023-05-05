package com.example.poetress.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.poetress.data.repositories.ChatMainGetData;
import com.example.poetress.data.repositories.ChatSelectNewDataInteraction;
import com.example.poetress.data.types.SimpleUserData;

import java.util.List;

public class ChatMainViewModel extends ViewModel {
    private ChatMainGetData repository;
    private LiveData<List<SimpleUserData>> chatsLiveData;

    ChatMainViewModel(){
        repository = new ChatMainGetData();
        chatsLiveData = repository.getChats();
    }

    public void refreshData(){
        chatsLiveData = repository.getChats();
    }

    public SimpleUserData getData(int position){
        return chatsLiveData.getValue().get(position);
    }

    public LiveData<List<SimpleUserData>> getChats() {
        return chatsLiveData;
    }
}
