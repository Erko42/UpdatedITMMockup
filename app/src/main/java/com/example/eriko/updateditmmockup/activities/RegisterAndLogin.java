package com.example.eriko.updateditmmockup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.eriko.updateditmmockup.R;

public class RegisterAndLogin extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_login);
    }

    public void register(View view) {
        Intent intent = new Intent(RegisterAndLogin.this, Register.class);
        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(RegisterAndLogin.this, Login.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getBooleanExtra("isFromMultiApp", false)) {
            Intent intent = new Intent(RegisterAndLogin.this, ExhibitorListsbs.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(RegisterAndLogin.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
