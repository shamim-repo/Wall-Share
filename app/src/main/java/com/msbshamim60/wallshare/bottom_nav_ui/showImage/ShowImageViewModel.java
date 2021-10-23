package com.msbshamim60.wallshare.bottom_nav_ui.showImage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.msbshamim60.wallshare.repository.post_repository.PostRepository;

public class ShowImageViewModel extends AndroidViewModel {
    private PostRepository postRepository;
    public ShowImageViewModel(@NonNull Application application) {
        super(application);
        postRepository=new PostRepository(application);
    }
    public void downloadPost(String postID){
        postRepository.downloadPost(postID);
    }
}
