package com.example.poetress.view_model.profile;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsViewModel extends ViewModel {
    FirebaseAuth firebaseAuth;

    public SettingsViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void SignOut(){
        firebaseAuth.signOut();
    }
}
