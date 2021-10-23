package com.msbshamim60.wallshare.bottom_nav_ui.category.category;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.paging.CombinedLoadStates;
import androidx.paging.LoadState;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class CategoryOFFragment extends Fragment implements OnClickHandler {
    private static final String POST_ID = "POST_ID";
    private static final String CATEGORY = "CATEGORY";
    private static String USER_ID="USER_ID";
    private static String USER_NAME="USER_NAME";
    private static String USER_PROFILE_PIC="USER_PROFILE_PIC";
    private static String IMAGE_URL="IMAGE_URL";
    private static final String IMAGE_TITLE="IMAGE_TITLE";
    private FirebaseFirestore db;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private FirestorePagingOptions<Post> options;
    private String category;

    public static CategoryOFFragment newInstance() {
        return new CategoryOFFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_o_f_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= view.findViewById(R.id.new_post_recyclerView);
        checkData();
        if (category!=null) {
            if(checkConnection()) {
                setupFirestore();
                setupRecyclerView();
            }
        }
    }


    private boolean checkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void checkData() {
        if (getArguments().get(CATEGORY)!=null){
            category=getArguments().get(CATEGORY).toString();
        }
    }

    private void setupRecyclerView() {
        adapter=new RecyclerAdapter(options,getContext(),this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    private void setupFirestore() {
        db=FirebaseFirestore.getInstance();
        CollectionReference postReference=db.collection("Posts");
        Query query=postReference.whereEqualTo("category",category).orderBy("time",Query.Direction.DESCENDING);
        PagingConfig pagingConfig=new PagingConfig(10,2,false);

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
        Navigation.findNavController(getView()).navigate(R.id.action_category_of_fragment_to_show_post_fragment,bundle);

    }
}