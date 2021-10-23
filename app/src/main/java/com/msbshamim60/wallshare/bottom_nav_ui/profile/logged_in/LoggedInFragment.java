package com.msbshamim60.wallshare.bottom_nav_ui.profile.logged_in;
import androidx.lifecycle.ViewModelProvider;

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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.msbshamim60.wallshare.Adapter.RecyclerAdapter;
import com.msbshamim60.wallshare.R;
import com.msbshamim60.wallshare.click_interface.OnClickHandler;
import com.msbshamim60.wallshare.click_interface.OnMenuItemClickListenerDelete;
import com.msbshamim60.wallshare.dataModel.Post;

public class LoggedInFragment extends Fragment implements OnClickHandler, OnMenuItemClickListenerDelete {
    private static final String TAG = "TAG";
    private LoggedInViewModel mViewModel;
    private TextView userNameTextView;
    private ImageView userProfilePic;
    private static String USER_ID="USER_ID";
    private static String USER_NAME="USER_NAME";
    private static String USER_PROFILE_PIC="USER_PROFILE_PIC";
    private static String IMAGE_URL="IMAGE_URL";
    private static final String IMAGE_TITLE="IMAGE_TITLE";
    private FirebaseFirestore db;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private FirestorePagingOptions<Post> options;
    private String UID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.logged_in_fragment, container, false);
    }

    private boolean checkConnection() {ConnectivityManager cm =
            (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
       return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!checkConnection()){
            gotoOfflineFragment();
        }else {
            mViewModel = new ViewModelProvider(this).get(LoggedInViewModel.class);
            recyclerView = view.findViewById(R.id.new_post_recyclerView);
            userNameTextView = view.findViewById(R.id.user_name_textView);
            userProfilePic = view.findViewById(R.id.profile_image_view);
            Button signOutButton = view.findViewById(R.id.profile_logout_button);
            Button editButton = view.findViewById(R.id.edit_button);
            ExtendedFloatingActionButton newPostButton = view.findViewById(R.id.add_post_button);
            ExtendedFloatingActionButton downloadedImageButton = view.findViewById(R.id.downloaded_image_button);
            if (FirebaseAuth.getInstance().getCurrentUser() == null)
                gotoLoginFragment();
            newPostButton.setOnClickListener(view1 -> {
                Navigation.findNavController(getView()).navigate(R.id.action_logged_in_Fragment_to_new_post_fragment);
            });
            editButton.setOnClickListener(view12 -> {
                Navigation.findNavController(getView()).navigate(R.id.action_logged_in_Fragment_to_register_fragment);
            });
            signOutButton.setOnClickListener(view1 -> signOut());
            mViewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
                if (firebaseUser != null) {
                    setUserData(firebaseUser);
                    setupFirestore(firebaseUser.getUid());
                    setupRecyclerView();
                }
            });
            mViewModel.getLoggedOutLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
                if (aBoolean) {
                    gotoLoginFragment();
                }
            });
            mViewModel.getDeleteSuccessfulLiveData().observe(getViewLifecycleOwner(),aBoolean -> {
                if (aBoolean){
                    setupFirestore(UID);
                    setupRecyclerView();
                }else {
                    showSeekbar("Could not delete post");
                }
            });
            downloadedImageButton.setOnClickListener(v -> {
                gotoOfflineFragment();
            });
        }
    }

    private void gotoOfflineFragment() {
        Navigation.findNavController(getView()).navigate(R.id.action_logged_in_Fragment_to_offline_fragment);
    }

    private void setupRecyclerView() {
        adapter=new RecyclerAdapter(options,getContext(),this,this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    private void setupFirestore(String uid) {
        db=FirebaseFirestore.getInstance();
        CollectionReference postReference=db.collection("Posts");
        Query query=postReference.whereEqualTo("user.uid",uid).orderBy("time",Query.Direction.DESCENDING);
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
        Navigation.findNavController(getView()).navigate(R.id.action_logged_in_Fragment_to_show_post_fragment,bundle);
    }
    private void setUserData(FirebaseUser firebaseUser) {
        UID=firebaseUser.getUid();
        if (firebaseUser.getDisplayName()!=null)
            userNameTextView.setText(firebaseUser.getDisplayName());
        if (firebaseUser.getPhotoUrl() != null && firebaseUser.getPhotoUrl().toString().trim()!="") {
            Glide.with(this)
                    .load(firebaseUser.getPhotoUrl())
                    .placeholder(getCircularProgressBar())
                    .circleCrop()
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(userProfilePic);
        }
    }

    private void signOut() {
        mViewModel.logOut();
    }
    private void gotoLoginFragment(){
        Navigation.findNavController(getView()).navigate(R.id.action_logged_in_Fragment_to_login_fragment);
    }
    private CircularProgressDrawable getCircularProgressBar() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }

    @Override
    public void onDeleteSelected(String postID, int postNum) {
        mViewModel.deletePost(postID);

    }
    private void showSeekbar(String message){
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }
}