package com.msbshamim60.wallshare.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){

        if(retrofit!=null)
            return retrofit;
        retrofit= new Retrofit.Builder()
                .baseUrl("https://api.imgbb.com/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
