package com.msbshamim60.wallshare.bottom_nav_ui.profile.new_post;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.msbshamim60.wallshare.R;
import com.msbshamim60.wallshare.dataModel.Post;
import com.msbshamim60.wallshare.dataModel.converters.FirebaseUserToUser;
import com.msbshamim60.wallshare.dataModel.converters.ImageDataToPost;

import java.io.IOException;

public class NewPostFragment extends Fragment {

    private NewPostViewModel mViewModel;
    private View parentLayout;
    private ExtendedFloatingActionButton uploadButton;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView locationTextView;
    private Spinner spinner;
    private Uri photoUri;
    private Post post;
    private Button postButton;

    public static NewPostFragment newInstance() {
        return new NewPostFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_post_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentLayout=view;
        post=new Post();
        uploadButton=view.findViewById(R.id.floating_button_upload);
        imageView=view.findViewById(R.id.new_post_image_view);
        titleTextView=view.findViewById(R.id.post_title_editText);
        spinner=view.findViewById(R.id.spinner_view);
        locationTextView=view.findViewById(R.id.post_location_editText);
        postButton=view.findViewById(R.id.post_button);
        post=null;

        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(getContext(),
                R.array.category_array, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        uploadButton.setOnClickListener(view1 -> {
            if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            }else
                pickImage();
        });
        postButton.setOnClickListener(view1 -> {
            checkPostData();
        });

        mViewModel = new ViewModelProvider(this).get(NewPostViewModel.class);

        mViewModel.getImageDataMutableLiveData().observe(getViewLifecycleOwner(), imageData -> {
            if (imageData.getSuccess()){
                showImageWithGlide(photoUri);
                post=new ImageDataToPost().convert(imageData);
            }else{
                post=null;
            }
        });

        mViewModel.getUploadSuccessfulLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean){
                Navigation.findNavController(getView()).navigate(R.id.action_new_post_fragment_to_logged_in_Fragment);
            }else{
                showSeekbar(getString(R.string.New_Post_Creation_Unsuccessful));
                postButton.setEnabled(true);
                postButton.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_baseline_post_add_24),null,null,null);
            }
        });

    }

    private void checkPostData() {
        String title=titleTextView.getText().toString();
        String category=spinner.getSelectedItem().toString();
        String location=locationTextView.getText().toString();
        if(post==null){
            showSeekbar(getString(R.string.No_image_to_post));
            imageView.requestFocus();
            return;
        }if (title.length()<3){
            showSeekbar("Title must be at least 3 character long");
            titleTextView.requestFocus();
            return;
        }if (category.equalsIgnoreCase("Category")){
            showSeekbar("Need select a category");
            spinner.requestFocus();
            return;
        }
            postButton.setEnabled(false);
            CircularProgressDrawable circularProgressDrawable=getCircularProgressBar();
            circularProgressDrawable.setCenterRadius(25f);
            postButton.setCompoundDrawables(circularProgressDrawable,null,null,null);
            post.setUser(new FirebaseUserToUser().getUser(FirebaseAuth.getInstance().getCurrentUser()));
            post.setTitle(title);
            post.setCategory(category);
            post.setLocation(location);
            mViewModel.newPost(post);
    }

    private void showSeekbar(String message){
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }
    private CircularProgressDrawable getCircularProgressBar() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            photoUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageDrawable(getCircularProgressBar());
            mViewModel.uploadImage(getString(R.string.img_bb_api_key),bitmap);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            imageView.setImageResource(R.drawable.ic_baseline_error_outline_24);
            showSeekbar(ImagePicker.getError(data));
        } else {
            showSeekbar(getString(R.string.image_picker_canceled));
        }
    }

    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                if (result.get(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    pickImage();
                }else
                    showSeekbar(getString(R.string.storage_permission_needed));
            });
    private void pickImage(){
        ImagePicker.with(this)
                .maxResultSize(4000,4000)
                .crop()
                .galleryOnly()
                .galleryMimeTypes(new String[]{
                        "image/png",
                        "image/jpg",
                        "image/jpeg"})
                .start();
    }

    private void showImageWithGlide(Uri photoUrl) {
        Glide.with(this)
                .load(photoUrl)
                .centerCrop()
                .placeholder(getCircularProgressBar())
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(imageView);
    }
}