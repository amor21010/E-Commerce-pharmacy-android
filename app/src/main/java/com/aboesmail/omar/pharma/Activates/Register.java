package com.aboesmail.omar.pharma.Activates;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[أ-يa-zA-Z])" +      //any letter

//                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[أ-يa-zA-Z])" +      //any letter
//                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
//                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                    "(?=\\S+$)" +           //no white spaces
                    ".{11,}" +               //at least 8 characters
                    "$");
    TextView signIn;
    TextInputLayout name;
    TextInputLayout email;
    TextInputLayout phone;
    TextInputLayout address;
    TextInputLayout password;
    TextInputLayout conPassword;
    NumberPicker agePicker;
    RadioButton male;
    RadioButton female;


    BaseApiUrl baseApiUrl=new BaseApiUrl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        password = findViewById(R.id.password);
        conPassword = findViewById(R.id.confirm_pass);
        Button regBtn = findViewById(R.id.registerbtn);
        signIn = findViewById(R.id.signintext);
        agePicker = findViewById(R.id.age);
        male = findViewById(R.id.maleRadio);
        female = findViewById(R.id.femaleRadio);

        final UserViewModel model = ViewModelProviders.of(Register.this).get(UserViewModel.class);

        agePicker.setMinValue(18);
        agePicker.setMaxValue(100);

        final EditText mName = name.getEditText();
        if (mName != null) {
            mName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    validateName(String.valueOf(mName.getText()).trim());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validateName(String.valueOf(mName.getText()).trim());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    validateName(String.valueOf(mName.getText()).trim());
                }
            });
        }

        final EditText mEmail = email.getEditText();
        if (mEmail != null) {
            mEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    validateEmail(String.valueOf(mEmail.getText()).trim());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validateEmail(String.valueOf(mEmail.getText()).trim());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    validateEmail(String.valueOf(mEmail.getText()).trim());
                }
            });
        }


        final EditText mAddress = address.getEditText();
        if (mAddress != null) {
            mAddress.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    validateAddress(String.valueOf(mAddress.getText()).trim());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validateAddress(String.valueOf(mAddress.getText()).trim());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    validateAddress(String.valueOf(mAddress.getText()).trim());
                }
            });
        }

        final EditText mPhone = phone.getEditText();
        if (mPhone != null) {
            mPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    validatePhone(String.valueOf(mPhone.getText()).trim());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validatePhone(String.valueOf(mPhone.getText()).trim());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    validatePhone(String.valueOf(mPhone.getText()).trim());
                }
            });
        }
        final EditText mpass = password.getEditText();
        if (mpass != null) {
            mpass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    validatePassword(String.valueOf(mpass.getText()));
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validatePassword(String.valueOf(mpass.getText()));
                }

                @Override
                public void afterTextChanged(Editable s) {
                    validatePassword(String.valueOf(mpass.getText()));
                }
            });
        }
        final EditText mconfirm = conPassword.getEditText();
        if (mconfirm != null) {
            mconfirm.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (mpass != null) {
                        validateConPass(String.valueOf(mconfirm.getText()), String.valueOf(mpass.getText()));
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mpass != null) {
                        validateConPass(String.valueOf(mconfirm.getText()), String.valueOf(mpass.getText()));
                    }

                }


                @Override
                public void afterTextChanged(Editable s) {
                    if (mpass != null) {
                        validateConPass(String.valueOf(mconfirm.getText()), String.valueOf(mpass.getText()));
                    }
                }
            });
        }


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Sign_In_Activity.class);
                startActivity(intent);
            }
        });
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = String.valueOf(name.getEditText().getText()).trim();
                String userPhone = String.valueOf(phone.getEditText().getText()).trim();
                String userEmail = String.valueOf(email.getEditText().getText()).trim();
                String userPassword = String.valueOf(password.getEditText().getText());
                String confirmPassword = String.valueOf(conPassword.getEditText().getText());
                String userAddress = String.valueOf(address.getEditText().getText());
                int useAge = agePicker.getValue();
                String gender = null;
                if (female.isChecked()) gender = String.valueOf(female.getText());
                if (male.isChecked()) gender = String.valueOf(male.getText());

                if (validation(userName, userEmail, userPhone, userPassword, userAddress, confirmPassword, male.isChecked(), female.isChecked())) {
                    User user = new User(userName, userPhone, userEmail, gender, null, useAge, userAddress, userPassword);
                    RegisterConnection(user);
                    if (model.getAllLocalUsers() == null)
                        model.insertUser(new LocalUser(user.getUserID(), userName, userEmail,
                                user.getAddress(), user.getAddress(), userPhone, gender, useAge, user.getPhoto(), true));

                }
            }
        });

    }

    private boolean validateName(String userName) {

        if (userName == null || userName.isEmpty()) {
            name.setError("please enter your name");
            return false;
        } else if (!NAME_PATTERN.matcher(userName).matches()) {
            name.setError("user name must be at least 8 characters");
            return false;
        } else {
            name.setError(null);
            return true;
        }

    }

    private boolean validatePassword(@NotNull String userPassword) {

        if (userPassword.isEmpty()) {
            password.setError("please enter your password");
            return false;
        } else if (userPassword.length() < 8) {
            password.setError("password must be at least 8 characters");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(userPassword).matches()) {
            password.setError("user password must has at least 1 letter");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    private boolean validateEmail(String userEmail) {

        if (userEmail == null || userEmail.isEmpty()) {
            email.setError("please enter your email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError("invalid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validateAddress(String userAddress) {

        if (userAddress == null || userAddress.isEmpty()) {
            address.setError("please enter your Address");
            return false;
        } else if (userAddress.length() < 8) {

            address.setError("please enter correct address");
            return false;
        } else {
            address.setError(null);
            return true;
        }
    }

    private boolean validateConPass(String userConPass, String userPassword) {

        if (userConPass == null || userConPass.isEmpty()) {
            conPassword.setError("please confirm your password");
            return false;
        } else if (userPassword.isEmpty()) {
            password.setError("you must enter password");
            return false;
        } else if (!userConPass.equals(userPassword)) {
            conPassword.setError("password doesn't match");


            return false;
        } else {
            conPassword.setError(null);
            return true;
        }
    }

    boolean validateGender(boolean isMale, boolean isFemale) {
        if (!isFemale && !isMale) {
            male.setError("required");
            female.setError("required");
            return false;
        } else {
            male.setError(null);
            female.setError(null);
            return true;
        }

    }

    private boolean validatePhone(String userPhone) {

        if (userPhone == null || userPhone.isEmpty()) {
            phone.setError("please enter your phone");
            return false;
        } else if (!PHONE_PATTERN.matcher(userPhone).matches()) {
            phone.setError("please enter a valid phone number");
            return false;
        } else if (userPhone.trim().length() > 15) {
            phone.setError("please enter a valid phone number");
            return false;
        } else {
            phone.setError(null);
            return true;
        }
    }

    private boolean validation(String userName, String userEmail,
                               String userPhone, String userPass, String userAddress,
                               String userconPss, boolean isMale, boolean isFemale) {
        return !(!validateName(userName) |
                !validateEmail(userEmail) |
                !validatePhone(userPhone) | !validateAddress(userAddress) |
                !validatePassword(userPass) |
                !validateConPass(userPass, userconPss) |
                !validateGender(isMale, isFemale));
    }

    void RegisterConnection(final User user) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        String baseUrl =baseApiUrl.getBaseUrl();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient).build();

        UserInterface userInterface = retrofit.create(UserInterface.class);
        Call<User> call = userInterface.Register(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    Toast.makeText(Register.this, "check your email for verification code", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Register.this, HomeActivity.class);
                    intent.putExtra("USERID", user.getUserID());
                    startActivity(intent);
                }
                if (response.code() == (409)) {
                    email.setError("email already exists");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("resss", "faildd " + t.getMessage());
                if (t.getMessage().equals("email already exists")) {
                    email.setError(t.getMessage());
                } else
                    Toast.makeText(Register.this, "Failed to connect with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
