package com.msbshamim60.wallshare.bottom_nav_ui.offline;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.msbshamim60.wallshare.Adapter.OfflineRecyclerAdapter;
import com.msbshamim60.wallshare.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OfflineFragment extends Fragment implements OfflineRecyclerAdapter.OnOfflineImageClick {

    private static final String TAG = "TAG";
    private OfflineViewModel mViewModel;
    private RecyclerView recyclerView;
    private OfflineRecyclerAdapter adapter;
    private static final String IMAGE_URL="IMAGE_URL";
    private static final String IMAGE_TITLE="IMAGE_TITLE";
    private List<File> imageList;
    private Integer position;

    public static OfflineFragment newInstance() {
        return new OfflineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.offline_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.new_post_recyclerView);

        mViewModel = new ViewModelProvider(this).get(OfflineViewModel.class);
        mViewModel.getListLiveData().observe(getViewLifecycleOwner(),uris -> {
            if (uris!=null){
                this.imageList=uris;
                setRecyclerView(imageList);
            }
        });
        mViewModel.getDeleteSuccessful().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean){
                if (position!=null) {
                    imageList.remove(position);
                     adapter.itemRemoved(position);
                     position = null;
                }
            }else {
                position=null;
            }
        });
    }



    private void setRecyclerView(List<File> uris) {
        adapter=new OfflineRecyclerAdapter(uris,this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onImageCLick(String path) {
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_URL, path);
        Navigation.findNavController(getView()).navigate(R.id.action_offline_fragment_to_offline_show_imgae_fragment,bundle);
    }

    @Override
    public void onImageLongCLick(int position) {
        this.position=position;
        mViewModel.deleteImage(imageList.get(position));
    }
}