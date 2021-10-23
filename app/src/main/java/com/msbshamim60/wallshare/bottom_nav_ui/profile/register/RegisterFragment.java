package com.msbshamim60.wallshare.bottom_nav_ui.profile.register;



import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.msbshamim60.wallshare.R;

import java.io.IOException;


public class RegisterFragment extends Fragment {

    private View parentLayout;
    private ImageView profileImageView;
    private Uri photoUri;
    private String photoUrl;
    private RegisterViewModel mViewModel;
    private TextInputEditText fullNameEditText;
    private MaterialButton registerButton;
    private MaterialButton deleteButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentLayout=view;
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        registerButton=view.findViewById(R.id.register_button);
        deleteButton=view.findViewById(R.id.delete_user_button);
        ExtendedFloatingActionButton uploadPhotoButton=view.findViewById(R.id.floating_button_upload);
        fullNameEditText=view.findViewById(R.id.textInputEditText);
        profileImageView=view.findViewById(R.id.register_image_view);

        mViewModel.getProfileUpdateLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
         if (aBoolean){
             Navigation.findNavController(getView()).navigate(R.id.action_register_fragment_to_logged_in_Fragment);
         } else {
             showSeekbar(parentLayout,getString(R.string.Profile_Couldnt_Updated));
         }
        });

        mViewModel.getProfilePictureUrlLiveData().observe(getViewLifecycleOwner(), s -> {
            if(!s.isEmpty() && s!=""){
                registerButton.setEnabled(true);
                registerButton.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_baseline_add_24, getContext().getTheme()),null,null,null);
                photoUrl=s;
                profileImageView.setImageURI(photoUri);
                showSeekbar(parentLayout,getString(R.string.Profile_Picture_Upload_Successful));
            }else {
                profileImageView.setImageResource(R.drawable.ic_baseline_error_outline_24);
                showSeekbar(parentLayout, getString(R.string.Profile_Picture_Upload_Unsuccessful));
            }
        });

        mViewModel.getDeleteSuccessful().observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                Navigation.findNavController(getView()).navigate(R.id.action_register_fragment_to_logged_in_Fragment);
            }
        });

        registerButton.setOnClickListener(view1 -> {
            String fullName=fullNameEditText.getText().toString();
                if (fullName.trim().isEmpty() || fullName==""){
                    fullNameEditText.setError(getString(R.string.Full_Name_cannot_be_empty));
                    fullNameEditText.requestFocus();
                }else{
                    registerButton.setEnabled(false);
                    registerButton.setCompoundDrawables(getCircularProgressBar(),null,null,null);
                    mViewModel.editUserData(fullName,photoUrl);
                }
        });
        deleteButton.setOnClickListener(v -> {
                mViewModel.deleteUser();
        });

        uploadPhotoButton.setOnClickListener(view1 -> {
            if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
                    !=PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            }else
                pickImage();
        });

        mViewModel.getUserLiveData().observe(getViewLifecycleOwner(),firebaseUser -> {
            if (firebaseUser!=null) {
                setUserData(firebaseUser);
            }
        });
    }

    private CircularProgressDrawable getCircularProgressBar() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(40f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }

    private void setUserData(FirebaseUser firebaseUser) {
        if (!firebaseUser.getDisplayName().trim().isEmpty() &&  firebaseUser.getDisplayName().trim() != "")
            fullNameEditText.setText(firebaseUser.getDisplayName());
        if (firebaseUser.getPhotoUrl() != null && firebaseUser.getPhotoUrl().toString().trim()!="") {
            showImageWithGlide(firebaseUser.getPhotoUrl());
        }
    }

    private void showImageWithGlide(Uri photoUrl) {
        Glide.with(this)
                .load(photoUrl)
                .centerCrop()
                .placeholder(getCircularProgressBar())
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(profileImageView);
    }

    private void showSeekbar(View parentLayout, String message){
        final Snackbar snackBar=Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);
        snackBar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .setAction("Dismiss", v -> snackBar.dismiss())
                .show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            photoUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profileImageView.setImageDrawable(getCircularProgressBar());
            registerButton.setEnabled(false);
            registerButton.setCompoundDrawables(getCircularProgressBar(),null,null,null);
            mViewModel.uploadProfilePicture(bitmap);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            profileImageView.setImageResource(R.drawable.ic_baseline_error_outline_24);
            showSeekbar(parentLayout,ImagePicker.getError(data));
        } else {
            showSeekbar(parentLayout,getString(R.string.image_picker_canceled));
        }
    }



    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),result -> {
                if (result.get(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    pickImage();
                }else
                    showSeekbar(parentLayout,getString(R.string.storage_permission_needed));
            });
    private void pickImage(){
        ImagePicker.with(this)
                .cropSquare()
                .galleryOnly()
                .compress(500)
                .galleryMimeTypes(new String[]{
                        "image/png",
                        "image/jpg",
                        "image/jpeg"})
                .maxResultSize(300,300)
                .start();
    }
}