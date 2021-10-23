package com.msbshamim60.wallshare.bottom_nav_ui.profile.login;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.msbshamim60.wallshare.R;

import java.util.Arrays;

public class LoginFragment extends Fragment {
    private View parentLayout;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentLayout=view;
        Button signInButton=view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(view1 -> startSignIn());

    }

    private ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            (result) -> {
                onSignInResult(result);
            });


    private void startSignIn() {

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false,true)
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build()))
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (response.isNewUser()){
                gotoRegisterFragment();
            }
            else gotoLoggedInFragment();
        } else {
            if (response == null) {
                showSeekbar(parentLayout,R.string.sign_in_cancelled);
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSeekbar(parentLayout,R.string.no_internet_connection);
                return;
            }
            showSeekbar(parentLayout,R.string.unknown_error);
        }
    }



    private void showSeekbar(View parentLayout, int message){
        Snackbar.make(parentLayout, getString(message), Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }
    private void gotoLoggedInFragment(){
        Navigation.findNavController(getView()).navigate(R.id.action_login_fragment_to_logged_in_Fragment);
    }
    private void gotoRegisterFragment() {
        Navigation.findNavController(getView()).navigate(R.id.action_login_fragment_to_register_fragment);
    }
}