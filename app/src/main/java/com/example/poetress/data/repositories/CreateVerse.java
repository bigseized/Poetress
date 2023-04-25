package com.example.poetress.data.repositories;

import android.util.Log;

import com.example.poetress.data.data_sources.UserMainData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateVerse {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    public CreateVerse(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }


    public void sendData(String ganre, String title, String text){

        firebaseFirestore.collection("User_Data")
                .document(firebaseAuth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String Author = document.getString("Name") + " " + document.getString("Surname");
                            Map<String, Object> data = new HashMap<>();
                            data.put("Author", Author);
                            data.put("Date_Verse", com.google.firebase.firestore.FieldValue.serverTimestamp());
                            data.put("Ganre_Verse", ganre);
                            data.put("Name_Verse",title);
                            data.put("Text_Verse", text);
                            String User_UID = firebaseAuth.getCurrentUser().getUid();
                            firebaseFirestore.collection("User_Data").document(User_UID).collection("User_Verses").add(data);
                        } else {
                            Log.e("firestore", "getAuthor: Document doesn't exist");
                        }
                    }
                });
    }
}
