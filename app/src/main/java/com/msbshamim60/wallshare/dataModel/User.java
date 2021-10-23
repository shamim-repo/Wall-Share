package com.msbshamim60.wallshare.dataModel;

import java.util.Objects;

public class User {
    private String uid;
    private String fullName;
    private String profileImage;

    public User() {
    }

    public User(String uid, String fullName, String profileImage) {
        this.uid = uid;
        this.fullName = fullName;
        this.profileImage = profileImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUid().equals(user.getUid()) && Objects.equals(getFullName(), user.getFullName()) && Objects.equals(getProfileImage(), user.getProfileImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid(), getFullName(), getProfileImage());
    }
}
