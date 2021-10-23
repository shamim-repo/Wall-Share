package com.msbshamim60.wallshare.dataModel.converters;

import com.google.firebase.auth.FirebaseUser;
import com.msbshamim60.wallshare.dataModel.User;

public class FirebaseUserToUser {
    private User user;

    public FirebaseUserToUser() {
        this.user = new User();
    }
    public  User getUser(FirebaseUser firebaseUser){
        if( firebaseUser!= null ) {
            this.user.setFullName(firebaseUser.getDisplayName());
            this.user.setUid(firebaseUser.getUid());
            if (firebaseUser.getPhotoUrl()!=null) {
                this.user.setProfileImage(firebaseUser.getPhotoUrl().toString());
            }
        }
        return user;
    }
}
