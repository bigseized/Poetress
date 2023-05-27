package com.example.poetress.view_model.chat;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.example.poetress.data.repositories.ChatDialogDataInteraction;
import com.example.poetress.data.types.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatMainDialogViewModel extends ViewModel {
    ChatDialogDataInteraction repository;

    public ChatMainDialogViewModel(){
        repository = new ChatDialogDataInteraction();
    }

    public void sendMessage(Message message){
        repository.sendMessage(message);
    }

    public void setListener(EventListener<QuerySnapshot> eventListener, String curUserID, String curPartnerID){
        repository.listenMessages(eventListener, curUserID, curPartnerID);
    }

    public String getID(){
        return repository.getID();
    }

    public Task<String> getImage(String ID){
        return repository.getImage(ID);
    }
    public void getUri(String ID, ChatDialogDataInteraction.OnUriReadyCallback callback) {
        repository.getUri(ID, callback);
    }

}
