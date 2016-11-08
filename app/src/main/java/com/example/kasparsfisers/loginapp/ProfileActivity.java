package com.example.kasparsfisers.loginapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kasparsfisers.loginapp.data.User;
import com.example.kasparsfisers.loginapp.utils.SharedPreferencesUtils;

import static com.example.kasparsfisers.loginapp.GalleryActivity.decodeToBase64;


public class ProfileActivity extends AppCompatActivity {
            SharedPreferencesUtils preferences;
            ProfileView val;
            User user;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.custom_view);

                val = (ProfileView)findViewById(R.id.valSelect);


                preferences = new SharedPreferencesUtils(this);
                String value = preferences.sessionData();

                user = new User(preferences.loginName(value),preferences.loginEmail(value),preferences.loginImage(value));

                val.setUser(user);
    }



}

