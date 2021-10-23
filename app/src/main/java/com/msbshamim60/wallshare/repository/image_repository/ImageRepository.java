package com.msbshamim60.wallshare.repository.image_repository;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.msbshamim60.wallshare.api.ApiClient;
import com.msbshamim60.wallshare.api.ApiInterface;
import com.msbshamim60.wallshare.dataModel.ImageData;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageRepository {
    private ApiInterface apiInterface;
    private Application application;
    private MutableLiveData<ImageData>  imageMutableLiveData;

    public ImageRepository(Application application) {
        this.application = application;
        this.imageMutableLiveData=new MutableLiveData<>();
        this.apiInterface=ApiClient.getClient().create(ApiInterface.class);

    }

    public void uploadImage(String key, Bitmap image){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Call<ImageData> call=
                apiInterface.uploadImage(key,encodedImage);
        call.enqueue(new Callback<ImageData>() {
            @Override
            public void onResponse(Call<ImageData> call, Response<ImageData> response) {
                if (response.isSuccessful()){
                    imageMutableLiveData.postValue(response.body());
                }else
                    Log.d("TAG","Upload Failed :"+response.message());
            }

            @Override
            public void onFailure(Call<ImageData> call, Throwable t) {
                Log.d("TAG","Upload Failed :");
                t.printStackTrace();
            }
        });
    }

    public MutableLiveData<ImageData> getImageMutableLiveData(){
        return imageMutableLiveData;
    }
}
