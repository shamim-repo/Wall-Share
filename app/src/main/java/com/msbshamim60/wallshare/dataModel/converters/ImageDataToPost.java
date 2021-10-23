package com.msbshamim60.wallshare.dataModel.converters;

import com.msbshamim60.wallshare.dataModel.ImageData;
import com.msbshamim60.wallshare.dataModel.Post;

public class ImageDataToPost {
    private Post post;
    public ImageDataToPost( ) {
        this.post = new Post();
    }

    public Post convert(ImageData imageData){
        post.setUrl(imageData.getData().getUrl());
        post.setThumbnail(imageData.getData().getDisplayUrl());
        post.setSize(imageData.getData().getSize());
        post.setTime(imageData.getData().getTime());
        return post;
    }
}
