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

    public String timer() {
        return prefs.getString("timer","10");
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



    // Radiobutton settings

    public void setRad1(int id) {
        editor.putInt("id1",id);
        editor.apply();
    }
    public void setRad2(int id) {
        editor.putInt("id2",id);
        editor.apply();
    }
    public void setRad3(int id) {
        editor.putInt("id3",id);
        editor.apply();
    }
    public void setRad4(int id) {
        editor.putInt("id4",id);
        editor.apply();
    }
    public void setRad5(int id) {
        editor.putInt("id5",id);
        editor.apply();
    }

    public int getRad1() {
        return prefs.getInt("id1", 0);
    }
    public int getRad2() {
        return prefs.getInt("id2", 0);
    }
    public int getRad3() {
        return prefs.getInt("id3", 0);
    }
    public int getRad4() {
        return prefs.getInt("id4", 0);
    }
    public int getRad5() {
        return prefs.getInt("id5", 0);
    }


}