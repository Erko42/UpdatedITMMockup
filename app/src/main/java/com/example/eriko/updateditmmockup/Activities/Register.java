package com.example.eriko.updateditmmockup.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.eriko.updateditmmockup.Helpers.DatabaseHelper;
import com.example.eriko.updateditmmockup.R;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    DatabaseHelper db;
    HashMap<String, String> hashMap;

    EditText emailView;
    EditText passwordView;
    EditText repeatPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        hashMap = new HashMap<>();

        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        repeatPasswordView = findViewById(R.id.repeatPassword);
    }

    public void register(View view) {
        if (emailView.getText().toString().contains("@") && passwordView.getText().toString().length() >= 4 &&
                passwordView.getText().toString().equals(repeatPasswordView.getText().toString())) {
            hashMap.put("email", emailView.getText().toString());
            hashMap.put("password", passwordView.getText().toString());
            db.insertData(DatabaseHelper.USER_TABLE, hashMap);
            Intent intent = new Intent(Register.this, RegisterAndLogin.class);
            startActivity(intent);
        }
    }
}
