package com.msbshamim60.wallshare.bottom_nav_ui.all.popularTab;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.msbshamim60.wallshare.Adapter.RecyclerAdapter;
import com.msbshamim60.wallshare.R;
import com.msbshamim60.wallshare.click_interface.OnClickHandler;
import com.msbshamim60.wallshare.dataModel.Post;

public class PopularTabFragment extends Fragment implements OnClickHandler {
    private static final String USER_ID="USER_ID";

    private static final String USER_NAME="USER_NAME";
    private static final String USER_PROFILE_PIC="USER_PROFILE_PIC";
    private static final String IMAGE_URL="IMAGE_URL";
    private static final String IMAGE_TITLE="IMAGE_TITLE";
    private static final String POST_ID = "POST_ID";
    private static final String TAG = "TAG";
    private FirebaseFirestore db;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    FirestorePagingOptions<Post> options;


    public static PopularTabFragment newInstance() {
        return new PopularTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= view.findViewById(R.id.new_post_recyclerView);
        if (checkConnection()) {
            setupFirestore();
            setupRecyclerView();
        }
    }

    private boolean checkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void setupRecyclerView() {
        adapter=new RecyclerAdapter(options,getContext(),this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void setupFirestore() {
        db= FirebaseFirestore.getInstance();
        CollectionReference postReference=db.collection("Posts");
        Query query=postReference.orderBy("download",Query.Direction.DESCENDING);
        PagingConfig pagingConfig=new PagingConfig(20,10,true);

        options=new FirestorePagingOptions.Builder<Post>()
                .setLifecycleOwner(this)
                .setQuery(query,pagingConfig,Post.class)
                .build();

    }

    @Override
    public void onClickRecyclerItemPost(Post post) {
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, post.getUser().getUid());
        bundle.putString(USER_PROFILE_PIC, post.getUser().getProfileImage());
        bundle.putString(USER_NAME, post.getUser().getFullName());
        bundle.putString(IMAGE_URL, post.getUrl());
        bundle.putString(IMAGE_TITLE, post.getTitle());
        bundle.putString(POST_ID, post.getPid());
        Navigation.findNavController(getView()).navigate(R.id.action_bottom_navigation_all_to_show_post_fragment,bundle);
    }

}