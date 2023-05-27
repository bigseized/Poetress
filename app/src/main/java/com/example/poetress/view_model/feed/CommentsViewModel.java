package com.example.poetress.view_model.feed;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.poetress.data.repositories.CommentsDataInteraction;
import com.example.poetress.data.types.Comment;
import com.google.firebase.firestore.Query;

import java.util.List;

public class CommentsViewModel extends ViewModel {

    CommentsDataInteraction repository;
    MutableLiveData<List<Comment>> commentsLD = new MutableLiveData<>();
    MutableLiveData<Boolean> isSend = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsSend() {
        return isSend;
    }

    public CommentsViewModel() {
        repository = new CommentsDataInteraction();
        commentsLD = repository.getCommentsLD();
        isSend = repository.getIsSend();
    }

    public void loadComments(String User_Id, String Verse_Id) {
        repository.loadComments(User_Id, Verse_Id);
    }

    public void newComment(String user_id, String verse_Id, String text) {
        repository.newComment(user_id, verse_Id, text);
    }

    public MutableLiveData<List<Comment>> getCommentsLD() {
        return commentsLD;
    }

    public Query getQuery(String user, String verse){
        return repository.getQuery(user, verse);
    }
}
