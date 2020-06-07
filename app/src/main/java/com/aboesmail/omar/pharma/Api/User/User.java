package com.aboesmail.omar.pharma.Api.User;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("_id")
    private String userID;

    private String fullName;

    private String phone;

    private String email;

    private String gender;
    @SerializedName("age")
    private int Age;
    private String address;
    private String password;
    private String photo;
    private Boolean blocked;
    private Boolean verfied;

    public User(String fullName, String phone, String email, String gender, String photo, int age, String address, String password) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.photo = photo;
        this.Age = age;
        this.address = address;
        this.password = password;


    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserID() {
        return userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Boolean getVerfied() {
        return verfied;
    }

    public void setVerfied(Boolean verfied) {
        this.verfied = verfied;
    }
}
