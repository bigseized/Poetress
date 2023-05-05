package com.example.poetress.data.repositories;

import android.util.Log;

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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ChatSelectNewDataInteraction {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String Image;

    public LiveData<List<SimpleUserData>> getFriends() {
        MutableLiveData<List<SimpleUserData>> friendsLiveData = new MutableLiveData<>();

        firestore.collection("User_Data").document(firebaseAuth.getUid()).collection("Friends")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Task<DocumentSnapshot>> documentSnapshotTasks = new ArrayList<>();
                        List<SimpleUserData> friends = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            SimpleUserData friend = new SimpleUserData();
                            friend.setID(document.getString("ID"));
                            friend.setName(document.getString("Name"));
                            friend.setSurname(document.getString("Surname"));

                            Task<DocumentSnapshot> documentTask = firestore.collection("User_Data").document(friend.getID()).get();
                            documentSnapshotTasks.add(documentTask);
                            documentTask.addOnCompleteListener(documentSnapshot -> {
                                if (documentSnapshot.isSuccessful()) {
                                    friend.setImage_Profile(documentSnapshot.getResult().getString("Image_Profile"));
                                }
                            });

                            friends.add(friend);
                        }
                        Tasks.whenAllComplete(documentSnapshotTasks)
                                .addOnCompleteListener(task1 -> {
                                    friendsLiveData.setValue(friends);
                                });
                    } else {
                        friendsLiveData.setValue(null);
                    }
                });
        return friendsLiveData;
    }

    public void createNewChat(SimpleUserData simpleUserData){
        HashMap<String, String> data = new HashMap<>();
        data.put("Name", simpleUserData.getName());
        data.put("Surname", simpleUserData.getSurname());

        SimpleUserData currentUserData;

        if (!Objects.equals(simpleUserData.getID(), firebaseAuth.getUid())) { // create for this user
            firestore.collection("User_Data").document(firebaseAuth.getUid())
                    .collection("Chats").document(simpleUserData.getID()).set(data);
        }
        else{
            Log.d("firestore", "putFriendToFirebase: Failed add themself");
        }

        if (!Objects.equals(simpleUserData.getID(), firebaseAuth.getUid())) { // create for this user
            setChatToAnotherUser(simpleUserData.getID());
        }

    }

    private void setChatToAnotherUser(String ID){
        SimpleUserData simpleUserData = new SimpleUserData();
        List<Task<DocumentSnapshot>> list = new ArrayList<>();

        Task<DocumentSnapshot> task = firestore.collection("User_Data").document(firebaseAuth.getUid()).get();
        task.addOnCompleteListener(documentSnapshot -> {
            if (documentSnapshot.isSuccessful()) {
                simpleUserData.setName(documentSnapshot.getResult().getString("Name"));
                simpleUserData.setSurname(documentSnapshot.getResult().getString("Surname"));            }
        });


        list.add(task);
        Tasks.whenAllComplete(list).addOnCompleteListener(task1 -> {
            HashMap<String, String> dataCurrent = new HashMap<>();
            dataCurrent.put("Name", simpleUserData.getName());
            dataCurrent.put("Surname", simpleUserData.getSurname());
            if (simpleUserData.getID() != firebaseAuth.getUid()) { // create for another user
                firestore.collection("User_Data").document(ID)
                        .collection("Chats").document(firebaseAuth.getUid()).set(dataCurrent);
            }
            else{
                Log.d("firestore", "putFriendToFirebase: Failed add themself");
            }
        });
    }
}
