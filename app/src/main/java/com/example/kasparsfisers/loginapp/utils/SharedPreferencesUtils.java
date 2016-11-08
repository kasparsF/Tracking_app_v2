package com.example.kasparsfisers.loginapp.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtils {
    private Context context;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPreferencesUtils(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("MYPREFS", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    private static SharedPreferencesUtils instance;

    public static SharedPreferencesUtils getInstance(Context ctx){
        if(instance == null){
            instance = new SharedPreferencesUtils(ctx);
        }
        return  instance;
    }

    public void register(String newUser, String newPass, String newEmail, String newName) {
        editor.putString(newUser + newPass + "data", newUser + "\n" + newEmail);
        editor.putString(newUser + newPass + "name", newName);
        editor.putString(newUser + newPass + "email", newEmail);
        editor.apply();
    }

    public String login(String user, String pass) {
        return prefs.getString(user + pass + "data", "");
    }

    public String loginName(String data) {
        return prefs.getString(data + "name", "");
    }

    public String loginEmail(String data) {
        return prefs.getString(data + "email", "");
    }

    public void setLoginImage(String data, String value) {
        editor.putString(data+"image", value);
        editor.apply();
    }

    public String loginImage(String data) {
        return prefs.getString(data+"image","");
    }

    public void sessionSetLoggedIn(Boolean logged) {
        editor.putBoolean("session",logged);
        editor.apply();
    }

    public Boolean sessionLoggedIn() {
        return prefs.getBoolean("session",false);
    }


    public void sessionSetData(String data) {
        editor.putString("sessionData",data);
        editor.apply();
    }

    public String sessionData() {
        return prefs.getString("sessionData","");
    }
}