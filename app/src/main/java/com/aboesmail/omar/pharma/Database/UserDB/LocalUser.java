package com.aboesmail.omar.pharma.Database.UserDB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "user_table")
public class LocalUser {

    String gender;
    int age;
    @PrimaryKey
    @SerializedName("_id")
    @NonNull
    private String id;
    private String userName;
    private String userEmail;
    private String address;
    private String location;
    private String phone;
    private String userPhoto;
    private boolean isLogin;

    public LocalUser(@org.jetbrains.annotations.NotNull String id, String userName, String userEmail, String address, String location, String phone, String gender, int age, String userPhoto, boolean isLogin) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.address = address;
        this.location = location;
        this.gender = gender;
        this.age = age;

        this.phone = phone;
        this.userPhoto = userPhoto;
        this.isLogin = isLogin;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
