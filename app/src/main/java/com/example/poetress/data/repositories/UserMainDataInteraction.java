package com.example.poetress.data.repositories;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.poetress.data.types.UserMainData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;
import java.util.UUID;

public class UserMainDataInteraction {
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    public UserMainDataInteraction(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void getDataFromFirebase(final OnFirestoreDataCallback<UserMainData> callback) {
        firebaseFirestore.collection("User_Data")
                .document(firebaseAuth.getUid())
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

    public void putDataToFirebase(UserMainData userMainData){
        StorageReference reference = storageReference.child("/user/" + firebaseAuth.getUid() + "/profileImage/" + UUID.randomUUID().toString());
        reference.putFile(Uri.parse(userMainData.getImage_Profile())).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("firestore", "onSuccess: Image Uploaded");
                                Map<String, Object> data = userMainData.getHashMap(uri.toString());
                                firebaseFirestore.collection("User_Data").document(firebaseAuth.getUid()).set(data).addOnSuccessListener(
                                        new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("firestore", "onSuccess: Data uploaded");
                                            }
                                        }
                                );
                            }
                        });
                        Log.d("firestore", "onSuccess: Image Uploaded");
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("firestore", "onFailure: Upload Failed");
                    }
                }
        );

    }
}
