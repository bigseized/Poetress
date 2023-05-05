package com.example.poetress.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.poetress.data.types.ProfileVerse;
import com.example.poetress.view_model.feed.UserVersesAdapter;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class FeedVersesGetData {
    private FirebaseFirestore db;
    private MutableLiveData<List<ProfileVerse>> userVersesLiveData = new MutableLiveData<>();
    private DocumentSnapshot lastVisible;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    List<Task<?>> tasks = new ArrayList<>();
    List<String> userId = new ArrayList<>();

    public FeedVersesGetData() {
        db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<List<ProfileVerse>> getUserVersesLiveData() {
        return userVersesLiveData;
    }

    public void loadUserVerses() {
        if (!isLoading && !isLastPage) {
            isLoading = true;
            Query query;
            if (lastVisible == null) {
                query = db.collectionGroup("User_Verses")
                        .orderBy("Date_Verse", Query.Direction.DESCENDING)
                        .limit(20);
            } else {
                query = db.collectionGroup("User_Verses")
                        .orderBy("Date_Verse", Query.Direction.DESCENDING)
                        .startAfter(lastVisible)
                        .limit(20);
            }
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<ProfileVerse> userVersesList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ProfileVerse userVerse = document.toObject(ProfileVerse.class);
                        DocumentReference profileRef = document.getReference().getParent().getParent();
                        userId.add(profileRef.getId());
                        Task<DocumentSnapshot> profileTask = profileRef.get();
                        tasks.add(profileTask);
                        profileTask.addOnSuccessListener(profileSnapshot -> {
                            String profileImageUrl = profileSnapshot.getString("Image_Profile");
                            userVerse.setUri(profileImageUrl);
                        }).addOnFailureListener(command -> {
                            Log.d("firestore", "Error getting user profile: ", command);
                        });
                        userVersesList.add(userVerse);
                    }
                    Tasks.whenAllSuccess(tasks).addOnSuccessListener(results -> {
                        if (userVersesList.size() > 0) {
                            lastVisible = task.getResult().getDocuments().get(userVersesList.size() - 1);
                            userVersesLiveData.postValue(userVersesList);
                            isLoading = false;
                        } else {
                            isLastPage = true;
                            isLoading = false;
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

    public List<String> getUserId(){
        return userId;
    }

    public void pageStart(){
        lastVisible = null;
        isLastPage = false;
        isLoading = false;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isLastPage() {
        return isLastPage;
    }
}
