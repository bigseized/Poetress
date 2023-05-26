package com.example.poetress.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.poetress.data.types.UserMainData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class SomeOneProfileDataInteraction {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String userID;
    MutableLiveData<Integer> friends = new MutableLiveData<>();
    MutableLiveData<Integer> verses = new MutableLiveData<>();

    public SomeOneProfileDataInteraction(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void getNumbers(){
        firebaseFirestore.collection("User_Data").document(userID)
                .collection("User_Verses").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        verses.postValue(queryDocumentSnapshots.size());
                    }
                });
        firebaseFirestore.collection("User_Data").document(userID)
                .collection("Friends").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        friends.postValue(queryDocumentSnapshots.size());
                    }
                });
    }

    public MutableLiveData<Integer> getFriends() {
        return friends;
    }

    public MutableLiveData<Integer> getVerses() {
        return verses;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void putFriendToFirebase(String ID, String Name, String Surname) {
        HashMap<String, String> data = new HashMap<>();
        data.put("ID", ID);
        data.put("Name", Name);
        data.put("Surname", Surname);

        if (!userID.equals(firebaseAuth.getUid())) {
            firebaseFirestore.collection("User_Data").document(firebaseAuth.getUid())
                    .collection("Friends").document(ID).set(data);
        }
        else{
            Log.d("firestore", "putFriendToFirebase: Failed add themself");
        }
    }

    public void getDataFromFirebase(final SomeOneProfileDataInteraction.OnFirestoreDataCallback<UserMainData> callback) {
        firebaseFirestore.collection("User_Data")
                .document(userID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            UserMainData data = new UserMainData(
                                    document.getString("Image_Profile"),
                                    document.getString("Name"),
                                    document.getString("Surname"),
                                    document.getString("Interests")
                            );
                            callback.onCallback(data);
                        } else {
                            callback.onError(new Exception("Document does not exist"));
                        }
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public interface OnFirestoreDataCallback<T> {
        void onCallback(T data);

        void onError(Exception e);
    }
}
