package com.msbshamim60.wallshare.bottom_nav_ui.showUser;

import static android.graphics.Color.GREEN;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.PorterDuff;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.msbshamim60.wallshare.Adapter.RecyclerAdapter;
import com.msbshamim60.wallshare.R;
import com.msbshamim60.wallshare.click_interface.OnClickHandler;
import com.msbshamim60.wallshare.dataModel.Post;

public class ShowUserFragment extends Fragment implements OnClickHandler  {
    private static final String TAG = "TAG";
    private static final String USER_ID="USER_ID";
    private static final String USER_NAME="USER_NAME";
    private static final String USER_PROFILE_PIC="USER_PROFILE_PIC";
    private static final String IMAGE_URL="IMAGE_URL";
    private static final String IMAGE_TITLE="IMAGE_TITLE";
    private TextView fullNameTextView;
    private ImageView profilePicImageView;
    private FirebaseFirestore db;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private FirestorePagingOptions<Post> options;
    private String userID;

    private ShowUserViewModel mViewModel;
    public static ShowUserFragment newInstance() {
        return new ShowUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.show_user_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ShowUserViewModel.class);
        fullNameTextView=view.findViewById(R.id.user_name_textView);
        profilePicImageView=view.findViewById(R.id.profile_image_view);
        recyclerView=view.findViewById(R.id.new_post_recyclerView);
        setDataToViews();
        setupFirestore();
        setupRecyclerView();
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
        Query query=postReference.whereEqualTo("user.uid",userID).orderBy("time",Query.Direction.DESCENDING);
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
        Navigation.findNavController(getView()).navigate(R.id.action_show_user_fragment_to_show_post_fragment,bundle);
    }

    private void setDataToViews() {
        if (getArguments().get(USER_NAME)!=null) {
            fullNameTextView.setText(getArguments().get(USER_NAME).toString());
        }if (getArguments().get(USER_ID)!=null) {
            userID=getArguments().get(USER_ID).toString();
        }if (getArguments().get(USER_PROFILE_PIC)!=null) {
            showImageWithGlide(getArguments().get(USER_PROFILE_PIC).toString(),profilePicImageView);
        }
    }

    private void showImageWithGlide(String photoUrl, ImageView imageView) {
        Glide.with(getContext())
                .load(photoUrl)
                .circleCrop()
                .placeholder(getCircularProgressBar())
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(imageView);
    }

    private CircularProgressDrawable getCircularProgressBar() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setColorFilter(GREEN, PorterDuff.Mode.SRC_IN);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(100f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }

}