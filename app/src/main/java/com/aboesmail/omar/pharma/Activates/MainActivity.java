package com.aboesmail.omar.pharma.Activates;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aboesmail.omar.pharma.Database.UserDB.LocalUser;
import com.aboesmail.omar.pharma.R;
import com.aboesmail.omar.pharma.Repository.user.UserViewModel;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    String userID;

   // TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button register = findViewById(R.id.registerbtn);
        final Button sign_in = findViewById(R.id.logginbtn);
        //skip = findViewById(R.id.skip);

        final UserViewModel model = ViewModelProviders.of(MainActivity.this).get(UserViewModel.class);
        model.getAllLocalUsers().observe(MainActivity.this, new Observer<LocalUser>() {
            @Override
            public void onChanged(LocalUser localUser) {


                if (localUser == null) {
                    userID = null;
                    (register).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            navigate(Register.class);
                        }
                    });
                    sign_in.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            navigate(Sign_In_Activity.class);

                        }
                    });

                    /*skip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            navigate(HomeActivity.class);
                        }
                    });*/
                } else {
                    userID = localUser.getId();
                    navigate(HomeActivity.class);

                }
            }
        });


    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);

            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);


    }


    @Override
    protected void onResume() {
        super.onResume();

        final Button register = findViewById(R.id.registerbtn);
        final Button sign_in = findViewById(R.id.logginbtn);

        final UserViewModel model = ViewModelProviders.of(MainActivity.this).get(UserViewModel.class);
        model.getAllLocalUsers().observe(MainActivity.this, new Observer<LocalUser>() {
            @Override
            public void onChanged(LocalUser localUser) {


                if (localUser == null) {
                    userID = null;
                    (register).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            navigate(Register.class);
                        }
                    });
                    sign_in.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            navigate(Sign_In_Activity.class);

                        }
                    });
/*
                    skip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            navigate(HomeActivity.class);
                        }
                    });*/
                } else {
                    userID = localUser.getId();
                    navigate(HomeActivity.class);

                }
            }
        });


    }

    private void navigate(Class toActivity) {
        Intent intent = new Intent(MainActivity.this, toActivity);
        intent.putExtra("USERID", userID);
        startActivity(intent);
    }


}
