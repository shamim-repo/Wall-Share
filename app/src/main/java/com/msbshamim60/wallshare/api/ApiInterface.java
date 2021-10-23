package com.msbshamim60.wallshare.api;

import com.msbshamim60.wallshare.dataModel.ImageData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("upload")
    Call<ImageData> uploadImage(@Field("key")String key, @Field("image") String image);
}
