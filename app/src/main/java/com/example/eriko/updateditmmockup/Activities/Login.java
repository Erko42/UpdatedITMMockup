package com.example.eriko.updateditmmockup.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.eriko.updateditmmockup.Classes.User;
import com.example.eriko.updateditmmockup.Helpers.DatabaseHelper;
import com.example.eriko.updateditmmockup.R;

public class Login extends AppCompatActivity {

    DatabaseHelper db;
    Cursor res;

    EditText emailView;
    EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        res = db.getAllData(DatabaseHelper.USER_TABLE);

        while (res.moveToNext()) {
            User user = new User(res.getString(1), res.getString(2));
            Log.d("tag", "user12345" + user.getEmail() + " " + user.getPassword());
        }
        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
    }

    public void login(View view) {
        while (res.moveToNext()) {
        User user = new User(res.getString(1), res.getString(2));
        if (emailView.getText().toString().equals(user.getEmail()) && passwordView.getText().toString().equals(user.getPassword())) {
                Intent intent = new Intent(Login.this, Ticket.class);
                startActivity(intent);
            }
        }
    }
}