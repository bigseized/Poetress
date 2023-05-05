package com.example.poetress.data.repositories;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.poetress.data.types.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ChatDialogDataInteraction {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void sendMessage(Message message){
        HashMap<String, Object> data = new HashMap<>();
        data.put("SenderId", message.getSenderID());
        data.put("ReceiverId", message.getReceiverId());
        data.put("MessageText", message.getMessageText());
        data.put("Date", com.google.firebase.firestore.FieldValue.serverTimestamp());
        firestore.collection("ChatData").add(data);
    }

    public String getID(){
        return firebaseAuth.getUid();
    }

    public Task<String> getImage(String ID){
        return firestore.collection("User_Data").document(ID).get().continueWith(task -> {
            if (task.isSuccessful()){
                String image = task.getResult().getString("Image_Profile");
                return image;
            } else {
                throw task.getException();
            }
        });
    }
    public void getUri(String ID, OnUriReadyCallback callback) {
        firestore.collection("User_Data").document(ID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                String uriString = document.getString("Image_Profile");
                                if (uriString != null) {
                                    Uri uri = Uri.parse(uriString);
                                    callback.onUriReady(uri);
                                } else {
                                    callback.onError(new Exception("URI field is null"));
                                }
                            } else {
                                callback.onError(new Exception("Document does not exist"));
                            }
                        } else {
                            callback.onError(task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError(e);
                    }
                });
    }

    public interface OnUriReadyCallback {
        void onUriReady(Uri uri);
        void onError(Exception e);
    }
}
