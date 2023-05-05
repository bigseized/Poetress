package com.example.poetress.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.poetress.data.types.SimpleUserData;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatMainGetData {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public LiveData<List<SimpleUserData>> getChats() {
        MutableLiveData<List<SimpleUserData>> chatsLiveData = new MutableLiveData<>();

        firestore.collection("User_Data").document(firebaseAuth.getUid()).collection("Chats")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Task<DocumentSnapshot>> documentSnapshotTasks = new ArrayList<>();
                        List<SimpleUserData> chats = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            SimpleUserData chat = new SimpleUserData();
                            chat.setID(document.getId());
                            chat.setName(document.getString("Name"));
                            chat.setSurname(document.getString("Surname"));

                            Task<DocumentSnapshot> documentTask = firestore.collection("User_Data").document(chat.getID()).get();
                            documentSnapshotTasks.add(documentTask);
                            documentTask.addOnCompleteListener(documentSnapshot -> {
                                if (documentSnapshot.isSuccessful()) {
                                    chat.setImage_Profile(documentSnapshot.getResult().getString("Image_Profile"));
                                }
                            });


                            chats.add(chat);
                        }
                        Tasks.whenAllComplete(documentSnapshotTasks)
                                .addOnCompleteListener(task1 -> {
                                    chatsLiveData.setValue(chats);
                                });
                    } else {
                        chatsLiveData.setValue(null);
                    }
                });
        return chatsLiveData;
    }
}
