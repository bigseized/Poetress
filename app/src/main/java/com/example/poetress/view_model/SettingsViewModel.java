package com.example.poetress.view_model;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsViewModel extends ViewModel {
    FirebaseAuth firebaseAuth;

    public SettingsViewModel() {
    }

    public void SignOut(){
        firebaseAuth.signOut();
    }
}
