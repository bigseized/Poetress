package com.example.poetress.view_model.create;

import androidx.lifecycle.ViewModel;

import com.example.poetress.data.repositories.CreateVerse;

public class CreateMainViewModel extends ViewModel {
    public void createVerse(String ganre, String title, String text){
        new CreateVerse().sendData(ganre,title,text);
    }
}
