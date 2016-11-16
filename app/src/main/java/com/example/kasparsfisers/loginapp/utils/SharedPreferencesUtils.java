package com.example.kasparsfisers.loginapp.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtils {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static final String MYPREFS = "MYPREFS";
    private static final String DATA = "data";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String IMAGE = "image";
    private static final String TIME = "time";
    private static final String TIME_DEFAULT = "10";
    private static final String SESSION = "session";
    private static final String SESSION_DATA = "sessionData";
    private static final String MAX_LOCATIONS = "maxLocations";
    private static final int MAX_LOCATIONS_DEF = 10;
    private static final String COLOR_CIRCLE = "colorCircle";
    private static final String COLOR_PROGRESS = "colorProgress";
    private static final String TEXT_COLOR = "colorText";
    private static final String TITLE_SIZE = "titleSize";
    private static final String SUBTITLE_SIZE = "subTitleSize";
    private static final String RADGROUP_1 = "id1";
    private static final String RADGROUP_2 = "id2";
    private static final String RADGROUP_3 = "id3";
    private static final String RADGROUP_4 = "id4";
    private static final String RADGROUP_5 = "id5";
    private static final String COLOR_CIRCLE_DEF = "#787878";
    private static final String COLOR_PROGRESS_DEF = "#bada55";
    private static final String TEXT_COLOR_DEF = "#000000";
    private static final int TEXT_SIZE_DEF = 60;


    private SharedPreferencesUtils(Context context) {
        prefs = context.getSharedPreferences(MYPREFS, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    private static SharedPreferencesUtils instance;

    public static SharedPreferencesUtils getInstance(Context ctx) {
        if (instance == null) {
            instance = new SharedPreferencesUtils(ctx);
        }
        return instance;
    }

    public void register(String newUser, String newPass, String newEmail, String newName) {
        editor.putString(newUser + newPass + DATA, newUser + "\n" + newEmail);
        editor.putString(newUser + newPass + NAME, newName);
        editor.putString(newUser + newPass + EMAIL, newEmail);
        editor.apply();
    }

    public String login(String user, String pass) {
        return prefs.getString(user + pass + DATA, "");
    }

    public String loginName(String data) {
        return prefs.getString(data + NAME, "");
    }

    public String loginEmail(String data) {
        return prefs.getString(data + EMAIL, "");
    }

    public void setTimer(String value) {
        editor.putString(TIME, value);
        editor.apply();
    }

    public String timer() {
        return prefs.getString(TIME, TIME_DEFAULT);
    }


    public void setLoginImage(String data, String value) {
        editor.putString(data + IMAGE, value);
        editor.apply();
    }

    public String loginImage(String data) {
        return prefs.getString(data + IMAGE, "");
    }


    public void sessionSetLoggedIn(Boolean logged) {
        editor.putBoolean(SESSION, logged);
        editor.apply();
    }

    public Boolean sessionLoggedIn() {
        return prefs.getBoolean(SESSION, false);
    }


    public void sessionSetData(String data) {
        editor.putString(SESSION_DATA, data);
        editor.apply();
    }

    public String sessionData() {
        return prefs.getString(SESSION_DATA, "");
    }

    public void setMaxLoc(int size) {
        editor.putInt(MAX_LOCATIONS, size);
        editor.apply();
    }

    public int getMaxLoc() {
        return prefs.getInt(MAX_LOCATIONS, MAX_LOCATIONS_DEF);
    }


    //For custom attributes

    public void setColorOfCircle(String color) {
        editor.putString(COLOR_CIRCLE, color);
        editor.apply();
    }

    public String getColorOfCircle() {
        return prefs.getString(COLOR_CIRCLE, COLOR_CIRCLE_DEF);
    }

    public void setColorOfProgress(String color) {
        editor.putString(COLOR_PROGRESS, color);
        editor.apply();
    }

    public String getColorOfProgress() {
        return prefs.getString(COLOR_PROGRESS, COLOR_PROGRESS_DEF);
    }

    public void setTitleSize(int size) {
        editor.putInt(TITLE_SIZE, size);
        editor.apply();
    }

    public int getTitleSize() {
        return prefs.getInt(TITLE_SIZE, TEXT_SIZE_DEF);
    }

    public void setMaxTxtSize(int size) {
        editor.putInt(SUBTITLE_SIZE, size);
        editor.apply();
    }

    public int getMaxTxtSize() {
        return prefs.getInt(SUBTITLE_SIZE, TEXT_SIZE_DEF);
    }

    public void setTextColor(String color) {
        editor.putString(TEXT_COLOR, color);
        editor.apply();
    }

    public String getTextColor() {
        return prefs.getString(TEXT_COLOR, TEXT_COLOR_DEF);
    }


    // Radiobutton settings

    public void setRad1(int id) {
        editor.putInt(RADGROUP_1, id);
        editor.apply();
    }

    public void setRad2(int id) {
        editor.putInt(RADGROUP_2, id);
        editor.apply();
    }

    public void setRad3(int id) {
        editor.putInt(RADGROUP_3, id);
        editor.apply();
    }

    public void setRad4(int id) {
        editor.putInt(RADGROUP_4, id);
        editor.apply();
    }

    public void setRad5(int id) {
        editor.putInt(RADGROUP_5, id);
        editor.apply();
    }

    public int getRad1() {
        return prefs.getInt(RADGROUP_1, 0);
    }

    public int getRad2() {
        return prefs.getInt(RADGROUP_2, 0);
    }

    public int getRad3() {
        return prefs.getInt(RADGROUP_3, 0);
    }

    public int getRad4() {
        return prefs.getInt(RADGROUP_4, 0);
    }

    public int getRad5() {
        return prefs.getInt(RADGROUP_5, 0);
    }


}