package com.msbshamim60.wallshare.bottom_nav_ui.profile.new_post;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.msbshamim60.wallshare.dataModel.ImageData;
import com.msbshamim60.wallshare.dataModel.Post;
import com.msbshamim60.wallshare.repository.image_repository.ImageRepository;
import com.msbshamim60.wallshare.repository.post_repository.PostRepository;

public class NewPostViewModel extends AndroidViewModel {
    private PostRepository postRepository;
    private ImageRepository imageRepository;
    private MutableLiveData<ImageData> imageDataMutableLiveData;
    private MutableLiveData<Boolean> uploadSuccessfulLiveData;
    public NewPostViewModel(@NonNull Application application) {
        super(application);
        postRepository=new PostRepository(application);
        imageRepository=new ImageRepository(application);
        imageDataMutableLiveData=imageRepository.getImageMutableLiveData();
        uploadSuccessfulLiveData=postRepository.getUploadSuccessfulLiveData();
    }
    public void newPost(Post post){
        postRepository.newPost(post);
    }
    public void uploadImage(String key, Bitmap image){
        imageRepository.uploadImage(key, image);
    }
    public MutableLiveData<ImageData> getImageDataMutableLiveData(){
        return imageDataMutableLiveData;
    }
    public MutableLiveData<Boolean> getUploadSuccessfulLiveData(){
        return uploadSuccessfulLiveData;
    }
}