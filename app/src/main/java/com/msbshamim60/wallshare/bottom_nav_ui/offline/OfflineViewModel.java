package com.msbshamim60.wallshare.bottom_nav_ui.offline;

import android.app.Application;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OfflineViewModel extends AndroidViewModel {
    private static final String TAG = "TAG";
    private MutableLiveData<List<File>> listLiveData;
    private MutableLiveData<Boolean> deleteSuccessfulLiveData;
    private Application application;
    public OfflineViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
        listLiveData=new MutableLiveData<>();
        deleteSuccessfulLiveData=new MutableLiveData<>();
        getFiles();
    }
    public void getFiles() {
        File dir=new File(Environment.getExternalStorageDirectory() + "/WallShare");
        File listFile[] = dir.listFiles();
        List<File> fileList = new ArrayList<>();
        if (listFile != null && listFile.length > 0) {
            for (File file : listFile) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")) {
                    if (!fileList.contains(file.toString()))
                        fileList.add(file);
                }
            }
        }

        listLiveData.postValue(fileList);
    }
    public void deleteImage(File file){
        deleteSuccessfulLiveData.postValue(file.delete());
    }
    public MutableLiveData<List<File>> getListLiveData(){
        return listLiveData;
    }
    public MutableLiveData<Boolean> getDeleteSuccessful(){
        return deleteSuccessfulLiveData;
    }

}