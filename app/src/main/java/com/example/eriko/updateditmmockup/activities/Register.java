package com.example.eriko.updateditmmockup.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eriko.updateditmmockup.R;
import com.example.eriko.updateditmmockup.helpers.DatabaseHelper;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    DatabaseHelper db;
    HashMap<String, String> hashMap;

    DisplayMetrics metrics;

    EditText emailView;
    EditText passwordView;
    EditText repeatPasswordView;

    TextView notAValidEmailAddress;
    TextView notAValidPassword;
    TextView notMatchingPassword;

    String email;
    String password;
    String repeatPassword;

    boolean notAValidEmailAddressIsSlided = false;
    boolean notAValidPasswordIsSlided = false;
    boolean notMatchingPasswordIsSlided = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        hashMap = new HashMap<>();

        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        repeatPasswordView = findViewById(R.id.repeatPassword);
        notAValidEmailAddress = findViewById(R.id.notAValidEmailAddress);
        notAValidPassword = findViewById(R.id.notAValidPassword);
        notMatchingPassword = findViewById(R.id.notMatchingPassword);
    }

    public void register(View view) {
        email = emailView.getText().toString();
        password = passwordView.getText().toString();
        repeatPassword = repeatPasswordView.getText().toString();

        if (email.contains("@") && email.contains(".") && email.length() > 3 &&
                password.length() > 3 &&
                password.equals(repeatPassword)) {
            hashMap.put("email", email);
            hashMap.put("password", password);
            db.insertData(DatabaseHelper.USER_TABLE, hashMap);
            Intent intent = new Intent(Register.this, RegisterAndLogin.class);
            startActivity(intent);
        }
        notAValidPassword.post(new Runnable() {
            @Override
            public void run() {
                metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int Start;
                int Destination;

                if ((!email.contains("@") || !email.contains(".") || email.length() < 5) && !notAValidEmailAddressIsSlided) {
                    Start = metrics.widthPixels;
                    Destination = metrics.widthPixels / 2 - notAValidEmailAddress.getMeasuredWidth() / 2;
                    ObjectAnimator slider = ObjectAnimator.ofFloat(notAValidEmailAddress, "x", Start, Destination);
                    slider.setDuration(500);
                    slider.start();
                    notAValidEmailAddressIsSlided = true;
                    notAValidEmailAddress.setVisibility(View.VISIBLE);
                } else if (email.contains("@") && email.contains(".") && email.length() > 3 && notAValidEmailAddressIsSlided) {
                    Start = metrics.widthPixels / 2 - notAValidEmailAddress.getMeasuredWidth() / 2;
                    Destination = -metrics.widthPixels;
                    ObjectAnimator slider = ObjectAnimator.ofFloat(notAValidEmailAddress, "x", Start, Destination);
                    slider.setDuration(500);
                    slider.start();
                    notAValidEmailAddressIsSlided = false;
                }
                if (password.length() < 4 && !notAValidPasswordIsSlided) {
                    Start = metrics.widthPixels;
                    Destination = metrics.widthPixels / 2 - notAValidPassword.getMeasuredWidth() / 2;
                    ObjectAnimator slider = ObjectAnimator.ofFloat(notAValidPassword, "x", Start, Destination);
                    slider.setDuration(500);
                    slider.start();
                    notAValidPasswordIsSlided = true;
                    notAValidPassword.setVisibility(View.VISIBLE);
                } else if (password.length() > 3 && notAValidPasswordIsSlided) {
                    Start = metrics.widthPixels / 2 - notAValidPassword.getMeasuredWidth() / 2;
                    Destination = -metrics.widthPixels;
                    ObjectAnimator slider = ObjectAnimator.ofFloat(notAValidPassword, "x", Start, Destination);
                    slider.setDuration(500);
                    slider.start();
                    notAValidPasswordIsSlided = false;
                }
                if (!password.equals(repeatPassword) && !notMatchingPasswordIsSlided) {
                    Start = metrics.widthPixels;
                    Destination = metrics.widthPixels / 2 - notMatchingPassword.getMeasuredWidth() / 2;
                    ObjectAnimator slider = ObjectAnimator.ofFloat(notMatchingPassword, "x", Start, Destination);
                    slider.setDuration(500);
                    slider.start();
                    notMatchingPasswordIsSlided = true;
                    notMatchingPassword.setVisibility(View.VISIBLE);
                } else if (password.equals(repeatPassword) && notMatchingPasswordIsSlided){
                    Start = metrics.widthPixels / 2 - notMatchingPassword.getMeasuredWidth() / 2;
                    Destination = -metrics.widthPixels;
                    ObjectAnimator slider = ObjectAnimator.ofFloat(notMatchingPassword, "x", Start, Destination);
                    slider.setDuration(500);
                    slider.start();
                    notMatchingPasswordIsSlided = false;
                }
            }
        });
    }
}
