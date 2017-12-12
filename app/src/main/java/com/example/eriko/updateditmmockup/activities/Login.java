package com.example.eriko.updateditmmockup.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.eriko.updateditmmockup.classes.User;
import com.example.eriko.updateditmmockup.helpers.DatabaseHelper;
import com.example.eriko.updateditmmockup.R;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    DatabaseHelper db;
    Cursor res;
    ArrayList<User> users;

    EditText emailView;
    EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        res = db.getAllData(DatabaseHelper.USER_TABLE);
        users = new ArrayList<>();

        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        while (res.moveToNext()) {
            User user = new User(res.getString(1), res.getString(2));
            users.add(user);
        }
    }

    public void login(View view) {
        for (User user : users) {
            if (emailView.getText().toString().equals(user.getEmail()) && passwordView.getText().toString().equals(user.getPassword())) {
                Intent intent = new Intent(Login.this, Ticket.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }
}