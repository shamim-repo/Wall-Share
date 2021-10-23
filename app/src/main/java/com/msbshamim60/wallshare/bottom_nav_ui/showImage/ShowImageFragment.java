package com.msbshamim60.wallshare.bottom_nav_ui.showImage;

import static android.graphics.Color.GREEN;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.msbshamim60.wallshare.R;
import com.ravikoradiya.zoomableimageview.ZoomableImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShowImageFragment extends Fragment {
    private static final String TAG = "TAG";
    private static final String USER_ID="USER_ID";
    private static final String POST_ID="POST_ID";
    private static final String USER_NAME="USER_NAME";
    private static final String USER_PROFILE_PIC="USER_PROFILE_PIC";
    private static final String IMAGE_URL="IMAGE_URL";
    private static final String IMAGE_TITLE="IMAGE_TITLE";

    private TextView fullName;
    private ImageView profilePicImageView;
    private ImageView downloadImageView;
    private ImageView setWallpaperImageView;
    private ImageView shareImageView;
    private ZoomableImageView zoomableImageView;
    private LinearLayout userLayout;
    private String userID;
    private String userNAme;
    private String userProfilePic;
    private String imageURL;
    private String imageTitle;
    private View parentLayout;

    private ShowImageViewModel mViewModel;
    private String postID;

    public static ShowImageFragment newInstance() {
        return new ShowImageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.show_image_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentLayout=view;
        mViewModel=new ViewModelProvider(this).get(ShowImageViewModel.class);
        fullName=view.findViewById(R.id.user_name_textView_show_image);
        profilePicImageView =view.findViewById(R.id.user_imageView);
        zoomableImageView=view.findViewById(R.id.zoom_imageView_show_image);
        downloadImageView=view.findViewById(R.id.download_button);
        setWallpaperImageView=view.findViewById(R.id.set_wallpaper_button);
        shareImageView=view.findViewById(R.id.share_button);
        userLayout=view.findViewById(R.id.top_user_layout);
        setDataToViews();

        userLayout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(USER_ID, userID);
            bundle.putString(USER_PROFILE_PIC, userProfilePic);
            bundle.putString(USER_NAME, userNAme);
            Navigation.findNavController(getView()).navigate(R.id.action_show_post_fragment_to_show_user_fragment,bundle);
        });

    }



    private void setDataToViews() {
        if (getArguments().get(USER_NAME)!=null) {
            userNAme=getArguments().get(USER_NAME).toString();
            fullName.setText(userNAme);
        } if (getArguments().get(POST_ID)!=null) {
            postID=getArguments().get(POST_ID).toString();
        }if (getArguments().get(USER_ID)!=null) {
            userID=getArguments().get(USER_ID).toString();
        }if (getArguments().get(USER_PROFILE_PIC)!=null) {
            userProfilePic=getArguments().get(USER_PROFILE_PIC).toString();
            showImageWithGlide(userProfilePic,profilePicImageView);
        }if (getArguments().get(IMAGE_URL)!=null) {
            imageURL=getArguments().get(IMAGE_URL).toString();
            showImageWithGlide(imageURL);
        }if (getArguments().get(IMAGE_TITLE)!=null) {
            imageTitle=getArguments().get(IMAGE_TITLE).toString();
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
    private void showImageWithGlide(String photoUrl) {
        Glide.with(getContext())
                .load(photoUrl)
                .placeholder(getCircularProgressBar())
                .error(R.drawable.ic_baseline_error_outline_24)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        downloadImageView.setOnClickListener(null);
                        setWallpaperImageView.setOnClickListener(null);
                        shareImageView.setOnClickListener(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        downloadImageView.setOnClickListener(clickListener);
                        setWallpaperImageView.setOnClickListener(clickListener);
                        shareImageView.setOnClickListener(clickListener);
                        return false;
                    }
                })
                .into(zoomableImageView);
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
    View.OnClickListener clickListener = v -> {
        BitmapDrawable drawable = (BitmapDrawable) zoomableImageView.getDrawable();
        switch (v.getId()) {
            case R.id.download_button:
                if (isStoragePermissionGranted())
                    saveToInternalStorage(drawable.getBitmap());
                break;
            case R.id.set_wallpaper_button:
                if (isStoragePermissionGranted()) {
                    Uri uri=saveToInternalStorage(drawable.getBitmap());
                    if(uri!=null)
                        setWallpaperIntent(uri);
                }
                break;
            case R.id.share_button:
                if (isStoragePermissionGranted()) {
                    startShare(saveToInternalStorage(drawable.getBitmap()));
                }

                break;
            default: return;
        }
    };

    private Uri saveToInternalStorage(Bitmap bitmapImage){
        File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/WallShare/");
        myDir.mkdirs();
        Uri photoURI=null;
        String fname = imageTitle +new SimpleDateFormat("ddMMyydd-HH:mm",
                Locale.getDefault()).format(new Date())
        +".jpg";
        File file = new File (myDir, fname);

        try {

            if (myDir.exists()) {
                FileOutputStream out = new FileOutputStream(file);
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                photoURI = FileProvider.getUriForFile(getContext(),
                        getContext().getApplicationContext().getPackageName() + ".provider",
                        file);
                showSeekbar("Image saved in " + file.toString());
                out.flush();
                out.close();
                if (postID != null)
                    mViewModel.downloadPost(postID);
            }else
                showSeekbar("Directory dose not exist " + file.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoURI;
    }
    private void deleteImage(Uri uri){
        File file=new File(uri.toString());
        file.delete();
    }


    public void setWallpaperIntent(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("mimeType", "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getActivity().startActivity(Intent.createChooser(intent, "Set as:"));
        deleteImage(uri);
    }
    private void startShare(Uri uri) {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        startActivity(Intent.createChooser(intent,"Share Image"));
        deleteImage(uri);
    }
    private void showSeekbar(String message){
        final Snackbar snackbar=Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .setAction("Dismiss", v -> snackbar.dismiss())
                .show();
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
        && grantResults[1] == PackageManager.PERMISSION_GRANTED){

        }else {
            showSeekbar("Storage Write Permission needed");
        }
    }
}