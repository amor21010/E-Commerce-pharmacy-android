package com.aboesmail.omar.pharma.Activates;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.aboesmail.omar.pharma.Api.BaseApiUrl;

import com.aboesmail.omar.pharma.Api.User.User;
import com.aboesmail.omar.pharma.Api.User.UserInterface;
import com.aboesmail.omar.pharma.Database.UserDB.LocalUser;
import com.aboesmail.omar.pharma.R;
import com.aboesmail.omar.pharma.Repository.user.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Sign_In_Activity extends AppCompatActivity {
    TextInputLayout emailText;
    TextInputLayout passText;
    UserViewModel viewModel;
    String userID;
    BaseApiUrl baseApiUrl=new BaseApiUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_);
        TextView register = findViewById(R.id.registerText);
        emailText = findViewById(R.id.sing_email);
        passText = findViewById(R.id.sing_pass);
        viewModel = ViewModelProviders
                .of(Sign_In_Activity.this)
                .get(UserViewModel.class);

        Button singin_btn = findViewById(R.id.singin_btn);
        singin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(emailText.getEditText().getText());
                String pass = String.valueOf(passText.getEditText().getText());

                //TODO validate
                if (Validate(email.trim(), pass)) {
                    Login(email.trim(), pass);
                }

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate(Register.class);
            }
        });
    }


    void Login(final String email, final String pass) {
        String baseUrl=baseApiUrl.getBaseUrl();


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();

        UserInterface userInterface = retrofit.create(UserInterface.class);
        Call<User> call = userInterface.Loggin(email, pass);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.body() != null)
                    if (response.body().getEmail() != null) {

                        User user = response.body();
                        Log.d("ussserrrr", user.getUserID());
                        LocalUser localUser = new LocalUser(user.getUserID(), user.getFullName(), user.getEmail(),
                                "sers", "sers", user.getPhone(), user.getGender(), user.getAge(), user.getPhoto(), true);
                        Log.d("ussserrrr", localUser.getId());
                        viewModel.insertUser(localUser);


                        Log.d("categorys", response.body().getEmail());
                        userID = user.getUserID();
                        navigate(HomeActivity.class);
                    } else {
                        emailText.setError("email or password doesn't seam correct");
                    }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("okh", t.getMessage());
                Toast.makeText(Sign_In_Activity.this, "Cannot connect to the server please call us " +
                        "on 01279509735", Toast.LENGTH_LONG).show();
            }
        });


    }

    public boolean Validate(String email, String pass) {
        if (!passValidate(pass) | !emailValidate(email)) {
            return false;
        } else {

            emailText.setError(null);
            return true;
        }
    }

    boolean passValidate(String pass) {
        if (pass == null || pass.isEmpty()) {
            passText.setError("please enter your Password");
            return false;
        } else {
            passText.setError(null);
            return true;
        }
    }

    boolean emailValidate(String email) {
        if (email.trim() == null || email.isEmpty()) {
            emailText.setError("please enter your email");
            return false;
        } else {
            emailText.setError(null);
            return true;
        }
    }

    private void navigate(Class toActivity) {
        Intent intent = new Intent(Sign_In_Activity.this, toActivity);
        intent.putExtra("USERID", userID);

        startActivity(intent);
    }

}