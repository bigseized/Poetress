package com.example.poetress.view_model.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.poetress.data.types.AdditionVerseInfo;
import com.example.poetress.data.types.ProfileVerse;
import com.example.poetress.data.types.UserMainData;
import com.example.poetress.data.repositories.UserMainDataInteraction;
import com.example.poetress.view_model.adapters.ProfileVersesAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;


public class ProfileMainViewModel extends ViewModel {
    private UserMainDataInteraction repository = new UserMainDataInteraction();
    private MutableLiveData<UserMainData> data = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleted = new MutableLiveData<>();
    public ProfileVersesAdapter adapter;
    FirestoreRecyclerOptions<ProfileVerse> options;
    private MutableLiveData<List<AdditionVerseInfo>> additionVersesInfoData = new MutableLiveData<>();

    public ProfileMainViewModel() {
        repository.loadAdditionInfo();
        additionVersesInfoData = repository.getAdditionVerseLD();
    }

    public void setRecyclerData(String User_UID){
        Query query = FirebaseFirestore.getInstance().collection("User_Data").document(User_UID).collection("User_Verses").orderBy("Date_Verse", Query.Direction.DESCENDING);
        options = new FirestoreRecyclerOptions.Builder<ProfileVerse>()
                .setQuery(query, ProfileVerse.class)
                .build();
        adapter = new ProfileVersesAdapter(options, this);
    }

    public ProfileVersesAdapter getAdapter(){
        return adapter;
    }

    public String getUID(){
        return repository.getUID();
    }


    public void loadData() {
        isLoading.setValue(true);
        repository.getDataFromFirebase(new UserMainDataInteraction.OnFirestoreDataCallback<UserMainData>() {
            @Override
            public void onCallback(UserMainData data) {
                isLoading.setValue(false);
                ProfileMainViewModel.this.data.setValue(data);
            }

            @Override
            public void onError(Exception e) {
                isLoading.setValue(false);
                error.setValue(e.getMessage());
            }
        });
    }

    public MutableLiveData<List<AdditionVerseInfo>> getAdditionVersesInfoData() {
        return additionVersesInfoData;
    }

    public void deleteVerse(String documentId, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        repository.deleteVerse(documentId, onSuccessListener, onFailureListener);
    }

    public LiveData<Boolean> getStatus(){
        return deleted;
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
