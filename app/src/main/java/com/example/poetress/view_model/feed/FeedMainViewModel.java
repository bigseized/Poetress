package com.example.poetress.view_model.feed;

import androidx.lifecycle.ViewModel;

import com.example.poetress.data.data_sources.CategoryFeedMain;

import java.util.ArrayList;

public class FeedMainViewModel extends ViewModel {
    public ArrayList getCategoryData(){
        return new CategoryFeedMain().getCategory();
    }
}
