package com.example.poetress.data.repositories;

import androidx.navigation.fragment.NavHostFragment;

import com.example.poetress.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthentificationInteraction {
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;

    public AuthentificationInteraction() {
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public boolean check(){
        return firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified();
    }

    public void createUserWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }

    public void sendEmailVerification(OnCompleteListener<Void> onCompleteListener) {
        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(onCompleteListener);
    }

    public void signInWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }

}
