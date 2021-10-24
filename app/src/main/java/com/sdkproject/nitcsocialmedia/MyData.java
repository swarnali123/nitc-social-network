package com.sdkproject.nitcsocialmedia;

public class MyData {
    String DoB;
    String Email;
    String Name;
    String ProfilePhoto;
    String Followers;
    String Following;

    public MyData() {
    }

    public MyData(String doB, String email, String name, String profilePhoto, String followers, String following) {
        DoB = doB;
        Email = email;
        Name = name;
        ProfilePhoto = profilePhoto;
        Followers = followers;
        Following = following;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String doB) {
        DoB = doB;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        ProfilePhoto = profilePhoto;
    }

    public String getFollowers() {
        return Followers;
    }

    public void setFollowers(String followers) {
        Followers = followers;
    }

    public String getFollowing() {
        return Following;
    }

    public void setFollowing(String following) {
        Following = following;
    }
}
