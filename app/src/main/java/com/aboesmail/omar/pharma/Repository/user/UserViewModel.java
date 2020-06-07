package com.aboesmail.omar.pharma.Repository.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aboesmail.omar.pharma.Api.User.User;
import com.aboesmail.omar.pharma.Database.UserDB.LocalUser;

public class UserViewModel extends AndroidViewModel {

    private LiveData<LocalUser> userList;

    private UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        userList = userRepository.find();

    }

    public void updateOnlinUser(String userId, User user) {
        userRepository.updateOnlineUser(userId, user);
    }

    public void insertUser(LocalUser localUser) {
        userRepository.insertUser(localUser);
    }

    public User getUserInfo(String id) {
        return userRepository.getUserInfo(id);
    }

    public LocalUser getLocalById(String userId) {
        return userRepository.getByID(userId);
    }


    void updateUser(LocalUser localUser) {
        userRepository.updateUser(localUser);
    }

    public void logOut(LocalUser localUser) {
        userRepository.logUser(localUser);
    }

    /* public void Register(User user) {
         userRepository.Register(user);
     }

     public void Login(String email, String password) {
         userRepository.Login(email, password);
     }*/


   /* public void image(String id, Drawable file){
        userRepository.Image(id,file);
    }*/

    public LiveData<LocalUser> getAllLocalUsers() {
        return userList;
    }
}
