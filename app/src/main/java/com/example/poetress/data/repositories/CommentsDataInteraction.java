package com.example.poetress.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.poetress.data.types.AdditionVerseInfo;
import com.example.poetress.data.types.Comment;
import com.example.poetress.data.types.ProfileVerse;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsDataInteraction {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    List<Task<?>> tasks = new ArrayList<>();
    MutableLiveData<List<Comment>> commentsLD = new MutableLiveData<>();
    MutableLiveData<Boolean> isSend = new MutableLiveData<>();
    Query query;

    public CommentsDataInteraction() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void loadComments(String user_Id, String verse_Id) {
        query = getQuery(user_Id,verse_Id);
        if (query != null) {
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Comment> commentList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Comment comment = document.toObject(Comment.class);
                        DocumentReference profileRef = firebaseFirestore.collection("User_Data")
                                .document(comment.getUserId());

                        Task<DocumentSnapshot> profileTask = profileRef.get();
                        tasks.add(profileTask);

                        profileTask.addOnSuccessListener(profileSnapshot -> {
                            String profileImageUrl = profileSnapshot.getString("Image_Profile");
                            comment.setUri(profileImageUrl);
                        }).addOnFailureListener(command -> {
                            Log.d("firestore", "Error getting user profile: ", command);
                        });
                        commentList.add(comment);
                    }
                    Tasks.whenAllSuccess(tasks).addOnSuccessListener(results -> {
                        if (commentList.size() > 0) {
                            commentsLD.postValue(commentList);
                        }
                    }).addOnFailureListener(command -> {
                        Log.d("firestore", "Error getting user profile: ", command);
                    });

                } else {
                    Log.d("firestore", "Error getting user verses: ", task.getException());
                }
            });
        }
    }

    public void newComment(String user_id, String verse_Id, String text){
        Map<String, Object> data = new HashMap<>();
        data.put("Date", Timestamp.now());
        data.put("Text", text);
        data.put("UserId", firebaseAuth.getUid());
        firebaseFirestore.collection("User_Data").document(user_id).collection("User_Verses").document(verse_Id)
                .collection("comments").add(data).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        isSend.postValue(true);
                    }else{
                        isSend.postValue(false);
                    }
                });
    }

    public MutableLiveData<List<Comment>> getCommentsLD() {
        return commentsLD;
    }

    public MutableLiveData<Boolean> getIsSend() {
        return isSend;
    }

    public Query getQuery(String user, String verse){
        try {
            Query query = firebaseFirestore.collection("User_Data").document(user).collection("User_Verses").document(verse)
                    .collection("comments").orderBy("Date", Query.Direction.ASCENDING);
            return query;
        }catch (Exception e){
            return null;
        }
    }
}
