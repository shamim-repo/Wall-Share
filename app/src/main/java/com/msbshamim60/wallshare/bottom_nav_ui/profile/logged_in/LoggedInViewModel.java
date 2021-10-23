package com.msbshamim60.wallshare.bottom_nav_ui.profile.logged_in;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.msbshamim60.wallshare.repository.auth_repository.AuthRepository;
import com.msbshamim60.wallshare.repository.post_repository.PostRepository;

public class LoggedInViewModel extends AndroidViewModel {

    private AuthRepository authRepository;
    private PostRepository postRepository;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<Boolean> deleteSuccessfulLiveData;

    public LoggedInViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        postRepository=new PostRepository(application);
        userLiveData = authRepository.getUserLiveData();
        loggedOutLiveData = authRepository.getLoggedOutLiveData();
        deleteSuccessfulLiveData=postRepository.getDeleteSuccessfulLiveData();
    }

    public void logOut() {
        authRepository.logOut();
    }
    public void deletePost(String pid) {
        postRepository.deletePost(pid);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
    public MutableLiveData<Boolean> getDeleteSuccessfulLiveData() {
        return deleteSuccessfulLiveData;
    }
}