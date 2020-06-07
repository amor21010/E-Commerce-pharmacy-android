package com.aboesmail.omar.pharma.Activates;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.aboesmail.omar.pharma.Api.BaseApiUrl;

import com.aboesmail.omar.pharma.Api.User.User;
import com.aboesmail.omar.pharma.Api.User.UserInterface;
import com.aboesmail.omar.pharma.R;
import com.aboesmail.omar.pharma.Repository.user.UserViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    int PICK_IMAGE = 1;
    ImageView image;
    TextView empty;
    TextInputLayout name;
    TextInputLayout email;
    TextInputLayout phone;
    TextInputLayout password;
    NumberPicker agePicker;
    RadioButton male;
    RadioButton female;
    Button update;

    String userID;

    User userr;
    UserViewModel userViewModel;
    BaseApiUrl baseApiUrl = new BaseApiUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        userViewModel = ViewModelProviders.of(ProfileActivity.this).get(UserViewModel.class);


        image = findViewById(R.id.userPhotoProfile);
        empty = findViewById(R.id.empty);
        name = findViewById(R.id.UserName);
        email = findViewById(R.id.userEmail);
        phone = findViewById(R.id.UserPhone);
        password = findViewById(R.id.UserPass);

        agePicker = findViewById(R.id.age);
        male = findViewById(R.id.maleRadio);
        female = findViewById(R.id.femaleRadio);

        update = findViewById(R.id.update);

        agePicker.setMinValue(18);
        agePicker.setMaxValue(100);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialog();
            }
        });


        userr = getUserInfo();


        Glide.with(ProfileActivity.this).asBitmap()
                .load(userr.getPhoto()).into(image);

        name.getEditText().setText(userr.getFullName());
        phone.getEditText().setText(userr.getPhone());
        email.getEditText().setText(userr.getEmail());
        agePicker.setValue(userr.getAge());

        Log.e("ageeee", String.valueOf(agePicker.getValue()));
        Log.e("ageeee1", String.valueOf(userr.getAge()));
        if (userr.getGender().toUpperCase().equals("MALE")) {
            male.setChecked(true);
            female.setChecked(false);
        }
        if (userr.getGender().toUpperCase().equals("FEMALE")) {
            male.setChecked(false);
            female.setChecked(true);
        }

        password.getEditText().setText(null);
//validate on change
        liveValidation();
//patch user info
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//get values


                final String userName = String.valueOf(name.getEditText().getText()).trim();
                final String userPhone = String.valueOf(phone.getEditText().getText()).trim();
                final String userEmail = String.valueOf(email.getEditText().getText()).trim();
                final String userPassword = String.valueOf(password.getEditText().getText());
                final int useAge = agePicker.getValue();
                //todo patch user info
                String gender = null;
                if (female.isChecked()) gender = String.valueOf(female.getText());
                if (male.isChecked()) gender = String.valueOf(male.getText());

                if (validation(userName, userEmail, userPhone,
                        male.isChecked(), female.isChecked())) {


                    User newUser;
                    if (validatePassword(userPassword)) {

                        newUser = new User(userName, userPhone, userEmail,
                                gender, userr.getPhoto(), useAge,
                                userr.getAddress(), userPassword);
                        confirmDialog(newUser);

                    } else {
                        empty.setVisibility(View.VISIBLE);
                        newUser = new User(userName, userPhone, userEmail,
                                gender, userr.getPhoto(), useAge,
                                userr.getAddress(), null);
                        confirmDialog(newUser);
                    }


                }


            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fabProfile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout_profile);
        NavigationView navigationView = findViewById(R.id.nav_view_profile);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_profile);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            navigate(HomeActivity.class);

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            navigate(HomeActivity.class);
        } else if (id == R.id.nav_deals) {

        } else if (id == R.id.nav_Chekout) {
            navigate(CartActivity.class);

        } else if (id == R.id.nav_history) {
            navigate(HistoryActivity.class);

        } else if (id == R.id.nav_history) {
            navigate(HistoryActivity.class);
        } else if (id == R.id.Logout) {
            userViewModel.logOut(userViewModel.getLocalById(userID));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_profile);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private boolean validatePassword(String userPassword) {
        if (userPassword.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            password.setError(null);
            return true;
        } else if (!userPassword.isEmpty() && userPassword.length() < 8) {
            empty.setVisibility(View.GONE);
            password.setError("password must be at least 8 characters");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(userPassword).matches()) {
            empty.setVisibility(View.GONE);

            password.setError("user password must has at least 1 letter");
            return false;
        } else {
            empty.setVisibility(View.VISIBLE);
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
                               String userPhone,
                               boolean isMale, boolean isFemale) {

        if (!validateName(userName) |
                !validateEmail(userEmail) |
                !validatePhone(userPhone) |
                !validateGender(isMale, isFemale)) {
            return false;
        } else return true;
    }

    private void liveValidation() {

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


    }

    private User getUserInfo() {

        Intent intent = getIntent();
        userID = intent.getStringExtra("USERID");
        return userViewModel.getUserInfo(userID);

    }


    public void ImageDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater()
                .inflate(R.layout.imagedialoug, null);
//custom dialog var
        LinearLayout layout = view.findViewById(R.id.linerDialog);
        CardView cardView = view.findViewById(R.id.cardDialog);
        final ImageView imageView = view.findViewById(R.id.imageDialog);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        layout.setBackground(new ColorDrawable(Color.TRANSPARENT));
        cardView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        Glide.with(ProfileActivity.this).asBitmap().load(userr.getPhoto()).into(imageView);

        dialog.show();
    }

    public void confirmDialog(final User newUser) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater()
                .inflate(R.layout.confirmdialoug, null);
//custom dialog var
        Button confirm = view.findViewById(R.id.confirm);
        final TextInputLayout oldPass = view.findViewById(R.id.oldpass);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldPass.getEditText().getText() == null) {
                    oldPass.setError("please enter your old password");
                } else {
                    confirmAndUpdate(userr.getEmail(), oldPass.getEditText().getText().toString(), newUser);
                }
            }
        });
        dialog.show();
    }


    private void navigate(Class toActivity) {
        Intent intent = new Intent(ProfileActivity.this, toActivity);
        intent.putExtra("USERID", userID);
        //intent.putExtra("ProductID", productId);
        startActivity(intent);
    }


    void confirmAndUpdate(final String email, final String pass, final User newUser) {
        String baseUrl = baseApiUrl.getBaseUrl();


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

                if (response.body() != null) {

                    userViewModel.updateOnlinUser(userID, newUser);
                    navigate(HomeActivity.class);
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("okh", t.getCause().toString());
                Toast.makeText(ProfileActivity.this, "Cannot connect to the server please call us " +
                        "on 01279509735", Toast.LENGTH_LONG).show();
            }
        });


    }


   /* @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                grantUriPermission(getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Glide.with(ProfileActivity.this).asBitmap().load(uri).into(image);
                File file = new File(uri.getPath()).getAbsoluteFile();

                Source source = ImageDecoder.createSource(file);
                Drawable drawable = null;
                try {
                    drawable = ImageDecoder.decodeDrawable(source);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Log.e("fileee", file.getAbsolutePath());
                userViewModel.image(userID, drawable);

            }

        }

    }*/
}
