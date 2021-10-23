package com.msbshamim60.wallshare.repository.user_repository;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

public class UserRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<Boolean> profileUpdateLiveData;
    private MutableLiveData<Boolean> deleteUserSuccessful;
    private MutableLiveData<String> profilePictureUrlLiveData;
    private MutableLiveData<FirebaseUser> userLiveData;
    private StorageReference storageRef;

    public UserRepository(Application application){
        this.application=application;
        this.firebaseAuth=FirebaseAuth.getInstance();
        this.storageRef= FirebaseStorage.getInstance().getReference();
        this.profileUpdateLiveData = new MutableLiveData<>();
        this.profilePictureUrlLiveData = new MutableLiveData<>();
        this.deleteUserSuccessful = new MutableLiveData<>();
        this.userLiveData = new MutableLiveData<>();
        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
        }
    }

    public void editUserData(String displayName, String photoUri){
        UserProfileChangeRequest changeRequest;
        if (photoUri == null) {
            changeRequest =new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();
        }else {
            changeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(photoUri))
                    .build();
        }
        firebaseAuth.getCurrentUser().updateProfile(changeRequest)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        profileUpdateLiveData.postValue(true);
                    }else
                        profileUpdateLiveData.postValue(false);
                });
    }
    public void uploadProfilePicture(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        StorageReference uploadImagesRef =
                storageRef.child("profilePic/"+firebaseAuth.getCurrentUser().getUid()+"/profilePicture.jpg");
        UploadTask uploadTask=uploadImagesRef.putBytes(byteArray);

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful())
                return null;
            return uploadImagesRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Uri url=task.getResult();
                profilePictureUrlLiveData.postValue(url.toString());
            }else
                profilePictureUrlLiveData.postValue("");
        });
    }
    public MutableLiveData<Boolean> getProfileUpdateLiveData(){
        return profileUpdateLiveData;
    }
    public MutableLiveData<String> getProfilePictureUrlLiveData(){
        return profilePictureUrlLiveData;
    }
    public void deleteUser(){
        firebaseAuth.getCurrentUser().delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                deleteUserSuccessful.postValue(true);
            }else
                deleteUserSuccessful.postValue(false);
        });
    }
    public MutableLiveData<FirebaseUser> getUserLiveData(){
        return userLiveData;
    }
    public MutableLiveData<Boolean> getDeleteUserSuccessful(){
        return deleteUserSuccessful;
    }
}
