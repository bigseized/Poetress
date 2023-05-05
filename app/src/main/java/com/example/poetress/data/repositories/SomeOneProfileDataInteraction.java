package com.example.poetress.data.repositories;

import android.util.Log;

import com.example.poetress.data.types.UserMainData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SomeOneProfileDataInteraction {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String userID;
    public SomeOneProfileDataInteraction(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
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
                    .collection("Friends").add(data);
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
