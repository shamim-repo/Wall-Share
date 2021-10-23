package com.msbshamim60.wallshare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.msbshamim60.wallshare.R;
import com.msbshamim60.wallshare.click_interface.OnClickHandler;
import com.msbshamim60.wallshare.click_interface.OnMenuItemClickListenerDelete;
import com.msbshamim60.wallshare.click_interface.OnMenuItemClickListenerDeleteINT;
import com.msbshamim60.wallshare.click_interface.OnclickHandlerInt;
import com.msbshamim60.wallshare.dataModel.Post;

public class RecyclerAdapter extends FirestorePagingAdapter<Post, RecyclerViewHolder> implements OnclickHandlerInt,OnMenuItemClickListenerDeleteINT{
    private Context context;
    private OnClickHandler onClickHandlerParent;
    private OnMenuItemClickListenerDelete onMenuItemClickListenerDelete;

    public RecyclerAdapter(@NonNull FirestorePagingOptions<Post> options,
                           Context context,OnClickHandler onClickHandler) {
        super(options);
        this.context=context;
        this.onClickHandlerParent =onClickHandler;
        this.onMenuItemClickListenerDelete=null;
    }
    public RecyclerAdapter(@NonNull FirestorePagingOptions<Post> options,
                           Context context, OnClickHandler onClickHandler,
                           OnMenuItemClickListenerDelete onMenuItemClickListenerDelete) {
        super(options);
        this.context=context;
        this.onClickHandlerParent =onClickHandler;
        this.onMenuItemClickListenerDelete=onMenuItemClickListenerDelete;
    }
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (onMenuItemClickListenerDelete!=null)
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyler_view, parent,false),this ,this);
        else return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyler_view, parent,false),this );
    }


    @Override
    protected void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position, @NonNull Post model) {
        holder.userNameTextView.setText(model.getUser().getFullName());
        holder.downloadTextView.setText(""+model.getDownload());
        holder.sizeTextView.setText(String.format("%.1f MB",(model.getSize()/1048576.0)));
        if(model.getUser().getProfileImage() != null ){
            showImageWithGlideProfilePic(model.getUser().getProfileImage(),holder.profileImageView);
        }
        if(model.getThumbnail() != null ){
            showImageWithGlide(model.getThumbnail(),holder.imageView);
        }
    }

    private void showImageWithGlide(String photoUrl, ImageView imageView) {
        Glide.with(context)
                .load(photoUrl)
                .centerCrop()
                .placeholder(getCircularProgressBar())
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(imageView);
    }
    private void showImageWithGlideProfilePic(String photoUrl, ImageView imageView) {
        Glide.with(context)
                .load(photoUrl)
                .circleCrop()
                .placeholder(getCircularProgressBar())
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(imageView);
    }

    private CircularProgressDrawable getCircularProgressBar() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }
    @Override
    public void onClickRecyclerItem(int postNum) {
        onClickHandlerParent.onClickRecyclerItemPost(getItem(postNum).toObject(Post.class));
    }

    @Override
    public void onDeleteSelected(int postNum) {
        onMenuItemClickListenerDelete.onDeleteSelected(getItem(postNum).get("pid").toString(),postNum);
    }
}
