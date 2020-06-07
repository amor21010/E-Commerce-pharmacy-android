package com.aboesmail.omar.pharma.Repository.user;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aboesmail.omar.pharma.Api.BaseApiUrl;

import com.aboesmail.omar.pharma.Api.User.User;
import com.aboesmail.omar.pharma.Api.User.UserInterface;
import com.aboesmail.omar.pharma.Database.Roomdb;
import com.aboesmail.omar.pharma.Database.UserDB.LocalUser;
import com.aboesmail.omar.pharma.Database.UserDB.UserDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRepository {
    UserDAO userDAO;
    BaseApiUrl baseApiUrl = new BaseApiUrl();
    private LocalUser localUser;
    private User user;
    private String BaseUrl = baseApiUrl.getBaseUrl();
    private LiveData<LocalUser> userList;


    UserRepository(Application application) {
        Roomdb DataBase = Roomdb.getInstance(application);
        userDAO = DataBase.userDAO();
        userList = userDAO.find();


    }

    User getUserInfo(final String id) {

        final MutableLiveData<User> data = new MutableLiveData<>();
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();
        UserInterface userInterface = retrofit.create(UserInterface.class);
        return getOnlineinfo(userInterface, id);
    }


    void updateOnlineUser(final String userID, final User user) {


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient).build();


        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<User> call = userInterface.update(userID, user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                LocalUser localUser1 = new LocalUser(user.getUserID(), user.getFullName(), user.getEmail(),
                        user.getAddress(), user.getAddress(), user.getPhone(), user.getGender(), user.getAge(),
                        user.getPhoto(), true);
                try {
                    updateUser(localUser1);
                } catch (Exception e) {
                    insertUser(localUser1);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {


            }
        });


    }


    public void Image(String id, Drawable file) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //  RequestBody body=RequestBody.create(MediaType.parse("*/Image"));
//        MultipartBody.Part part=MultipartBody.Part.createFormData("photo",file.getName(),body);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient).build();


        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<User> call = userInterface.patchImage(id, file);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("ussss", response.body().getUserID());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

 /*   void Register(User user) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();

        UserInterface userJSONPlaceHolder = retrofit.create(UserInterface.class);
        Call<User> call = userJSONPlaceHolder.Register(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.d("res", "successs " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("res", "faildd " + t.getMessage());
            }
        });
    }
*/
/*    User Login(final String email, final String pass) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();
         final User user = null;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();

        UserInterface userJSONPlaceHolder = retrofit.create(UserInterface.class);
        Call<User> call =userJSONPlaceHolder.Loggin(email,pass);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body()!=null){
                    user.setEmail(email);
                    user.setPassword(pass);
                    user.setFullName(response.body().getFullName());
                    user.setPhone(response.body().getPhone());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        return user;
    }
*/


    void insertUser(LocalUser localUser) {
        new insertUserAsync(userDAO).execute(localUser);
    }

    User getOnlineinfo(UserInterface userInterface, String id) {
        try {
            return new getOnlineIDAsynk(userInterface).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    LocalUser getByID(String userName) {

        try {
            return new getByIDAsynk(userDAO).execute(userName).get();
        } catch (ExecutionException e) {

            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    void updateUser(LocalUser localUser) {
        new updateUserAsync(userDAO).execute(localUser);
    }


    void logUser(LocalUser localUser) {
        new deleteUserAsync(userDAO).execute(localUser);
    }


    LiveData<LocalUser> find() {
        return userList;
    }


    class insertUserAsync extends AsyncTask<LocalUser, Void, Void> {
        private UserDAO userDAO;

        insertUserAsync(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(LocalUser... localUsers) {
            userDAO.insert(localUsers[0]);
            return null;
        }
    }

    class getByIDAsynk extends AsyncTask<String, Void, LocalUser> {
        private UserDAO userDAO;

        getByIDAsynk(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected LocalUser doInBackground(String... params) {
            localUser = userDAO.getByID(params[0]);
            return localUser;
        }
    }

    class getOnlineIDAsynk extends AsyncTask<String, Void, User> {
        private UserInterface userInterface;

        getOnlineIDAsynk(UserInterface userInterface) {
            this.userInterface = userInterface;
        }

        @Override
        protected User doInBackground(String... params) {
            try {
                user = userInterface.getUser(params[0]).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return user;
        }
    }

    class updateUserAsync extends AsyncTask<LocalUser, Void, Void> {
        private UserDAO userDAO;

        updateUserAsync(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(LocalUser... localUsers) {
            userDAO.update(localUsers[0]);
            return null;
        }
    }

    class deleteUserAsync extends AsyncTask<LocalUser, Void, Void> {
        private UserDAO userDAO;

        deleteUserAsync(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(LocalUser... localUsers) {
            userDAO.logeout(localUsers[0]);
            return null;
        }
    }


}

