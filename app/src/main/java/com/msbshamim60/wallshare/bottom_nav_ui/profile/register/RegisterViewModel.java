package com.msbshamim60.wallshare.bottom_nav_ui.profile.register;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.msbshamim60.wallshare.repository.user_repository.UserRepository;

public class RegisterViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private MutableLiveData<Boolean> profileUpdateLiveData;
    private MutableLiveData<String> profilePictureUrlLiveData;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> userDeleteSuccessful;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        profileUpdateLiveData = userRepository.getProfileUpdateLiveData();
        profilePictureUrlLiveData = userRepository.getProfilePictureUrlLiveData();
        userLiveData = userRepository.getUserLiveData();
        userDeleteSuccessful = userRepository.getDeleteUserSuccessful();
    }
    public void editUserData(String displayName,String photoUri){
        userRepository.editUserData(displayName,photoUri);
    }
    public void uploadProfilePicture(Bitmap bitmap){
        userRepository.uploadProfilePicture(bitmap);
    }
    public void deleteUser(){
        userRepository.deleteUser();
    }
    public MutableLiveData<Boolean> getProfileUpdateLiveData(){
        return profileUpdateLiveData;
    }
    public MutableLiveData<String> getProfilePictureUrlLiveData(){
        return profilePictureUrlLiveData;
    }
    public MutableLiveData<FirebaseUser> getUserLiveData(){
        return userLiveData;
    }
    public MutableLiveData<Boolean> getDeleteSuccessful(){
        return userDeleteSuccessful;
    }
}