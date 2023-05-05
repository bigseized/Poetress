package com.example.poetress.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.poetress.data.repositories.ChatSelectNewDataInteraction;
import com.example.poetress.data.repositories.SomeOneProfileDataInteraction;
import com.example.poetress.data.types.SimpleUserData;

import java.util.List;

public class ChatSelectNewViewModel extends ViewModel {
    private ChatSelectNewDataInteraction friendRepository;
    private LiveData<List<SimpleUserData>> friendsLiveData;

    ChatSelectNewViewModel(){
        friendRepository = new ChatSelectNewDataInteraction();
        friendsLiveData = friendRepository.getFriends();
    }

    public SimpleUserData getData(int position){
        return friendsLiveData.getValue().get(position);
    }

    public LiveData<List<SimpleUserData>> getFriends() {
        return friendsLiveData;
    }

    public void createNewChat(SimpleUserData simpleUserData){
        friendRepository.createNewChat(simpleUserData);
    }
}
