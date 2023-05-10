package com.example.poetress.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.poetress.data.types.AdditionVerseInfo;
import com.example.poetress.data.types.ProfileVerse;
import com.example.poetress.view_model.feed.UserVersesAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedVersesGetData {
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<List<ProfileVerse>> userVersesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<AdditionVerseInfo>> additionalVerseLiveData = new MutableLiveData<>();
    private DocumentSnapshot lastVisible;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    List<Task<?>> tasks = new ArrayList<>();
    List<String> userId = new ArrayList<>();
    List<String> verseId = new ArrayList<>();


    public FeedVersesGetData() {
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public MutableLiveData<List<ProfileVerse>> getUserVersesLiveData() {
        return userVersesLiveData;
    }

    public MutableLiveData<List<AdditionVerseInfo>> getAdditionalVerseLiveData() {
        return additionalVerseLiveData;
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
                    List<AdditionVerseInfo> additionVerseInfoList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        AdditionVerseInfo addInfo = new AdditionVerseInfo();
                        setAddInfo(addInfo, document.getReference());

                        ProfileVerse userVerse = document.toObject(ProfileVerse.class);
                        DocumentReference profileRef = document.getReference().getParent().getParent();

                        userId.add(profileRef.getId());
                        verseId.add(document.getId());
                        additionVerseInfoList.add(addInfo);

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
                        if (userVersesList.size() > 0 && userVersesList.size() == additionVerseInfoList.size()) {
                            lastVisible = task.getResult().getDocuments().get(userVersesList.size() - 1);
                            userVersesLiveData.postValue(userVersesList);
                            additionalVerseLiveData.postValue(additionVerseInfoList);
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

    public List<String> getVerseId(){
        return verseId;
    }

    public void pageStart(){
        lastVisible = null;
        isLastPage = false;
        isLoading = false;
    }

    public LiveData<Boolean> updateLikes(String verseID, String UserID) {
        MutableLiveData<Boolean> isLiked = new MutableLiveData<>();
        DocumentReference documentReference = db.collection("User_Data").document(UserID).collection("User_Verses")
                .document(verseID).collection("likes").document(firebaseAuth.getUid());
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    deleteLike(documentReference, isLiked);
                } else {
                    // Создание отсутствующего документа и коллекции
                    setLike(documentReference,isLiked);
                }
            } else {
                isLiked.postValue(false);
                Log.e("UserDocumentRepository", "Ошибка при чтении документа:", task.getException());
            }
        });

        return isLiked;
    }

    private void setLike(DocumentReference documentReference, MutableLiveData<Boolean> resultLiveData) {
        Map<String,Object> data = new HashMap<>();
        data.put("UserId",firebaseAuth.getUid());
        documentReference.set(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                resultLiveData.postValue(true);
            } else {
                resultLiveData.postValue(false);
                Log.e("UserDocumentRepository", "Ошибка при создании отсутствующего документа:", task.getException());
            }
        });
    }

    private void deleteLike(DocumentReference documentReference, MutableLiveData<Boolean> resultLiveData) {
        documentReference.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                resultLiveData.postValue(false);
            } else {
                resultLiveData.postValue(true);
                Log.e("UserDocumentRepository", "Ошибка при создании отсутствующего документа:", task.getException());
            }
        });
    }

    private void setAddInfo(AdditionVerseInfo addInfo, DocumentReference docRef){
        docRef.collection("likes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                addInfo.setNumOfLikes(task.getResult().size());
            }
        });
        docRef.collection("likes").document(firebaseAuth.getUid()).get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                if(task1.getResult().exists()){
                    addInfo.setLiked(true);
                }
            }
        });
        docRef.collection("comments").get().addOnCompleteListener(task2 ->{
            if (task2.isSuccessful()){
                addInfo.setNumOfComment(task2.getResult().size());
            }
        });
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isLastPage() {
        return isLastPage;
    }
}
