package com.msbshamim60.wallshare.repository.post_repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.msbshamim60.wallshare.dataModel.Post;


public class PostRepository {
    private static final String TAG = "TAG";
    private Application application;
    private FirebaseFirestore db;
    private MutableLiveData<Boolean> uploadSuccessfulLiveData;
    private MutableLiveData<Boolean> deleteSuccessfulLiveData;

    public PostRepository(Application application) {
        this.application = application;
        this.uploadSuccessfulLiveData = new MutableLiveData<>();
        this.deleteSuccessfulLiveData = new MutableLiveData<>();
        this.db=FirebaseFirestore.getInstance();
    }


    public void newPost(Post post){
        DocumentReference postReference=db.collection("Posts").document();
        post.setPid(postReference.getId());
        post.setDownload(0l);
        postReference.set(post)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        uploadSuccessfulLiveData.postValue(true);
                    }else
                        uploadSuccessfulLiveData.postValue(false);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }
    public void deletePost(String id){
        DocumentReference postReference=db.collection("Posts").document(id);
        postReference.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        deleteSuccessfulLiveData.postValue(true);
                    }else
                        deleteSuccessfulLiveData.postValue(false);
                })
                .addOnFailureListener(e -> {
                   e.printStackTrace();
                });
    }
    public MutableLiveData<Boolean> getUploadSuccessfulLiveData(){
        return uploadSuccessfulLiveData;
    }
    public MutableLiveData<Boolean> getDeleteSuccessfulLiveData(){
        return deleteSuccessfulLiveData;
    }
    public void downloadPost(String postId){
        DocumentReference postReference=db.collection("Posts").document(postId);
        postReference.get()
                .addOnCompleteListener(command -> {
                    if (command.isSuccessful()){
                        int download=Integer.parseInt(command.getResult().get("download").toString());
                        postReference.update("download",++download);
                    }else {
                        Log.d(TAG,command.getException().getMessage());
                    }
                });

    }
}
