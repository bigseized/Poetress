package com.example.poetress.view_model.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.poetress.data.data_sources.CategoryFeedMain;
import com.example.poetress.data.repositories.FeedVersesGetData;
import com.example.poetress.data.types.ProfileVerse;

import java.util.ArrayList;
import java.util.List;

public class FeedMainViewModel extends ViewModel {
    public ArrayList getCategoryData(){
        return new CategoryFeedMain().getCategory();
    }
    private FeedVersesGetData repository;
    private MutableLiveData<List<ProfileVerse>> userVersesLiveData;


    public FeedMainViewModel() {
        repository = new FeedVersesGetData();
        userVersesLiveData = repository.getUserVersesLiveData();
    }

    public MutableLiveData<List<ProfileVerse>> getUserVersesLiveData() {
        return userVersesLiveData;
    }

    public List<String> getUserIds(){
        return repository.getUserId();
    }

    public void refreshUserVerses() {
        repository.pageStart();
        repository.loadUserVerses();
    }
    public void clearData() {
        repository = new FeedVersesGetData();
        userVersesLiveData = repository.getUserVersesLiveData();
    }

    public void loadUserVerses() {
        repository.loadUserVerses();
    }

    public boolean isLoading() {
        return repository.isLoading();
    }

    public boolean isLastPage() {
        return repository.isLastPage();
    }
}
