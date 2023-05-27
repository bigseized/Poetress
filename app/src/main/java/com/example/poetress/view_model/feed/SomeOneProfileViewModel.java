package com.example.poetress.view_model.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.poetress.data.repositories.SomeOneProfileDataInteraction;
import com.example.poetress.data.types.ProfileVerse;
import com.example.poetress.data.types.UserMainData;
import com.example.poetress.view_model.adapters.versesRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SomeOneProfileViewModel extends ViewModel {
    private SomeOneProfileDataInteraction repository = new SomeOneProfileDataInteraction();
    private MutableLiveData<UserMainData> data = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Integer> friends;
    private MutableLiveData<Integer> verses;
    private FirestoreRecyclerOptions<ProfileVerse> options;
    private versesRecyclerAdapter adapter;

    public SomeOneProfileViewModel(){
        friends = repository.getFriends();
        verses = repository.getVerses();

    }

    public MutableLiveData<Integer> getFriends() {
        return friends;
    }

    public MutableLiveData<Integer> getVerses() {
        return verses;
    }

    public void setParams(String UserId){
        Query query = FirebaseFirestore.getInstance().collection("User_Data").document(UserId).collection("User_Verses").orderBy("Date_Verse", Query.Direction.DESCENDING);
        options = new FirestoreRecyclerOptions.Builder<ProfileVerse>()
                .setQuery(query, ProfileVerse.class)
                .build();
        adapter = new versesRecyclerAdapter(options);
    }

    public versesRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void addFriend(String ID, String Name, String Surname){
        repository.putFriendToFirebase(ID,Name,Surname);
    }

    public void loadData() {
        isLoading.setValue(true);
        repository.getDataFromFirebase(new SomeOneProfileDataInteraction.OnFirestoreDataCallback<UserMainData>() {
            @Override
            public void onCallback(UserMainData data) {
                isLoading.setValue(false);
                SomeOneProfileViewModel.this.data.setValue(data);
            }

            @Override
            public void onError(Exception e) {
                isLoading.setValue(false);
                error.setValue(e.getMessage());
            }
        });
    }

    public void setUserIdRepository(String userId){
        repository.setUserID(userId);
        repository.getNumbers();
    }

    public LiveData<UserMainData> getData() {
        return data;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }
}
