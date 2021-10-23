package com.msbshamim60.wallshare.bottom_nav_ui.showImage;

import static android.graphics.Color.GREEN;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.msbshamim60.wallshare.R;
import com.ravikoradiya.zoomableimageview.ZoomableImageView;

import java.io.File;

public class OfflineShowImageFragment extends Fragment {
    private static final String IMAGE_URL="IMAGE_URL";
    private static final String IMAGE_TITLE="IMAGE_TITLE";
    private String imageURL;
    private String imageTitle;
    private ZoomableImageView zoomableImageView;
    private ImageView setWallpaperImageView;
    private ImageView shareImageView;
    private TextView titleTextView;
    private View parentLayout;

    public static OfflineShowImageFragment newInstance() {
        return new OfflineShowImageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.offline_show_image_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentLayout=view;
        titleTextView=view.findViewById(R.id.image_title_textView_show_image);
        zoomableImageView=view.findViewById(R.id.zoom_imageView_show_image);
        setWallpaperImageView=view.findViewById(R.id.set_wallpaper_button);
        shareImageView=view.findViewById(R.id.offline_share_button);
        setDataToViews();
    }

    View.OnClickListener clickListener = v -> {
        switch (v.getId()) {
            case R.id.set_wallpaper_button:
                setWallpaperIntent(getPhotoUri(imageURL));
                break;
            case R.id.offline_share_button:
                startShare(getPhotoUri(imageURL));
                break;
            default: return;
        }
    };

    private void setDataToViews() {
       if (getArguments().get(IMAGE_URL)!=null) {
            imageURL=getArguments().get(IMAGE_URL).toString();
            showImageWithGlide(imageURL);
        }
    }
    private Uri getPhotoUri(String imageURL){
        return FileProvider.getUriForFile(getContext(),
                getContext().getApplicationContext().getPackageName() + ".provider",
                new File(imageURL));
    }
    public void setWallpaperIntent(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("mimeType", "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getActivity().startActivity(Intent.createChooser(intent, "Set as:"));
    }
    private void showImageWithGlide(String photoUrl) {
        Glide.with(getContext())
                .load(getPhotoUri(photoUrl))
                .placeholder(getCircularProgressBar())
                .error(R.drawable.ic_baseline_error_outline_24)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        setWallpaperImageView.setOnClickListener(null);
                        shareImageView.setOnClickListener(null);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        setWallpaperImageView.setOnClickListener(clickListener);
                        shareImageView.setOnClickListener(clickListener);
                        return false;
                    }
                })
                .into(zoomableImageView);
    }
    private void startShare(Uri uri) {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        startActivity(Intent.createChooser(intent,"Share Image"));
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