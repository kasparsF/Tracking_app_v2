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

    public void setTimer(String value) {
        editor.putString("timer", value);
        editor.apply();
    }

    public String timer(String data) {
        return prefs.getString(data+"timer","10000");
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

    public void setMaxLoc(int size) {
        editor.putInt("maxLoc",size);
        editor.apply();
    }

    public int getMaxLoc() {
        return prefs.getInt("maxLoc", 10);
    }

    //For custom attributes

    public void setColorOfCircle(String color) {
        editor.putString("colorCircle",color);
        editor.apply();
    }

    public String getColorOfCircle() {
        return prefs.getString("colorCircle","#787878");
    }

    public void setColorOfProgress(String color) {
        editor.putString("colorProgress",color);
        editor.apply();
    }

    public String getColorOfProgress() {
        return prefs.getString("colorProgress","#bada55");
    }

    public void setTitleSize(int size) {
        editor.putInt("titleSize",size);
        editor.apply();
    }

    public int getTitleSize() {
        return prefs.getInt("titleSize", 60);
    }

    public void setMaxTxtSize(int size) {
        editor.putInt("maxSize",size);
        editor.apply();
    }

    public int getMaxTxtSize() {
        return prefs.getInt("maxSize",60);
    }

    public void setTextColor(String color) {
        editor.putString("textColor",color);
        editor.apply();
    }

    public String getTextColor() {
        return prefs.getString("textColor","#a91111");
    }
}