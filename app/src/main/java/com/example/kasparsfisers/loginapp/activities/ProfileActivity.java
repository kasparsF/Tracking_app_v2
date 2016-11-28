package com.example.kasparsfisers.loginapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kasparsfisers.loginapp.utils.Functions;
import com.example.kasparsfisers.loginapp.views.ProfileView;
import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.data.User;
import com.example.kasparsfisers.loginapp.utils.SharedPreferencesUtils;


public class ProfileActivity extends AppCompatActivity {
    SharedPreferencesUtils preferences;
    ProfileView val;
    String sessionData;
    String imageCode;
    User user;
    int placeCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_custom_view);
        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            placeCount =  (int)extras.getFloat(Functions.CURRENT_LOC);
            // and get whatever type user account id is
        }
        val = (ProfileView) findViewById(R.id.valSelect);


        preferences = SharedPreferencesUtils.getInstance(this);
        sessionData = preferences.sessionData();
        imageCode = preferences.loginImage(sessionData);
        user = new User(preferences.loginName(sessionData), preferences.loginEmail(sessionData), imageCode);
        val.setCount1(placeCount);
        val.setUser(user);
        val.setCount2(preferences.getDistance());
    }

    @Override
    protected void onResume() {
        super.onResume();
       if(!imageCode.equals(preferences.loginImage(sessionData))){
          user.setImageId(preferences.loginImage(sessionData));
           val.refresh(user);
       }

    }

}

