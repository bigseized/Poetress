package com.example.poetress.view_model.auth;
import androidx.lifecycle.ViewModel;

import com.example.poetress.data.repositories.AuthentificationInteraction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

public class RegisterViewModel extends ViewModel {
    AuthentificationInteraction repository;
    public RegisterViewModel() {
        repository = new AuthentificationInteraction();
    }

    public boolean check(){
        return repository.check();
    }

    public void createUserWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        repository.createUserWithEmailAndPassword(email, password, onCompleteListener);
    }

    public void sendEmailVerification(OnCompleteListener<Void> onCompleteListener) {
        repository.sendEmailVerification(onCompleteListener);
    }

}
