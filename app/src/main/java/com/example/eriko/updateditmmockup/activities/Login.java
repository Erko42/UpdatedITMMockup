package com.example.eriko.updateditmmockup.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eriko.updateditmmockup.R;
import com.example.eriko.updateditmmockup.classes.User;
import com.example.eriko.updateditmmockup.helpers.DatabaseHelper;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    DisplayMetrics metrics;

    DatabaseHelper db;
    Cursor res;
    ArrayList<User> users;

    EditText emailView;
    EditText passwordView;

    TextView notAValidEmailAddress;
    TextView notAValidPassword;

    String email;
    String password;

    boolean notAValidEmailAddressIsSlided = false;
    boolean notAValidPasswordIsSlided = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        res = db.getAllData(DatabaseHelper.USER_TABLE);
        users = new ArrayList<>();

        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        notAValidEmailAddress = findViewById(R.id.notAValidEmailAddress);
        notAValidPassword = findViewById(R.id.notAValidPassword);

        while (res.moveToNext()) {
            User user = new User(res.getString(1), res.getString(2));
            users.add(user);
        }
    }

    public void login(View view) {
        email = emailView.getText().toString();
        password = passwordView.getText().toString();
        notAValidPassword.post(new Runnable() {
            @Override
            public void run() {
                metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int Start;
                int Destination;

                for (User user : users) {
                    if (email.equals(user.getEmail()) && password.equals(user.getPassword())) {
                        Intent intent = new Intent(Login.this, Ticket.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", user);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    if (!email.equals(user.getEmail()) && !notAValidEmailAddressIsSlided) {
                        Start = metrics.widthPixels;
                        Destination = metrics.widthPixels / 2 - notAValidEmailAddress.getMeasuredWidth() / 2;
                        ObjectAnimator slider = ObjectAnimator.ofFloat(notAValidEmailAddress, "x", Start, Destination);
                        slider.setDuration(500);
                        slider.start();
                        notAValidEmailAddressIsSlided = true;
                        notAValidEmailAddress.setVisibility(View.VISIBLE);
                    } else if (email.equals(user.getEmail()) && notAValidEmailAddressIsSlided) {
                        Start = metrics.widthPixels / 2 - notAValidEmailAddress.getMeasuredWidth() / 2;
                        Destination = -metrics.widthPixels;
                        ObjectAnimator slider = ObjectAnimator.ofFloat(notAValidEmailAddress, "x", Start, Destination);
                        slider.setDuration(500);
                        slider.start();
                        notAValidEmailAddressIsSlided = false;
                    }
                    if (!password.equals(user.getPassword()) && !notAValidPasswordIsSlided) {
                        Start = metrics.widthPixels;
                        Destination = metrics.widthPixels / 2 - notAValidPassword.getMeasuredWidth() / 2;
                        ObjectAnimator slider = ObjectAnimator.ofFloat(notAValidPassword, "x", Start, Destination);
                        slider.setDuration(500);
                        slider.start();
                        notAValidPasswordIsSlided = true;
                        notAValidPassword.setVisibility(View.VISIBLE);
                    } else if (password.equals(user.getPassword()) && notAValidPasswordIsSlided) {
                        Start = metrics.widthPixels / 2 - notAValidPassword.getMeasuredWidth() / 2;
                        Destination = -metrics.widthPixels;
                        ObjectAnimator slider = ObjectAnimator.ofFloat(notAValidPassword, "x", Start, Destination);
                        slider.setDuration(500);
                        slider.start();
                        notAValidPasswordIsSlided = false;
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Login.this, RegisterAndLogin.class);
        startActivity(intent);
    }
}