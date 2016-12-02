package com.example.kasparsfisers.loginapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.utils.SharedPreferencesUtils;

import static com.example.kasparsfisers.loginapp.R.color.grey;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4, radioGroup5;
    Button accept;
    String colorOfCircle;
    String colorOfText;
    String colorOfProgress;
    int sizeOfTitle;
    int sizeOfMaxText;
    SharedPreferencesUtils preferences;

    EditText timer;
    String newTimer;
    EditText maxLoc;
    String newMaxLoc;

    private static final int SMALL = 30;
    private static final int MEDIUM = 60;
    private static final int LARGE = 90;
    private static final String COLOR_BLACK = "#000000";
    private static final String COLOR_ORANGE = "#EF6C00";
    private static final String COLOR_GREY = "#787878";
    private static final String COLOR_GREY_DARK = "#3f3f3e";
    private static final String COLOR_GREEN = "#bada55";
    private static final String COLOR_YELLOW = "#FFF1E61E";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        radioGroup1 = (RadioGroup) findViewById(R.id.rGroup1);
        radioGroup2 = (RadioGroup) findViewById(R.id.rGroup2);
        radioGroup3 = (RadioGroup) findViewById(R.id.rGroup3);
        radioGroup4 = (RadioGroup) findViewById(R.id.rGroup4);
        radioGroup5 = (RadioGroup) findViewById(R.id.rGroup5);
        timer = (EditText) findViewById(R.id.timeForTracking);
        maxLoc = (EditText) findViewById(R.id.maxLocations);
        preferences = SharedPreferencesUtils.getInstance(this);
        accept = (Button) findViewById(R.id.accept);
        accept.setOnClickListener(this);

        radioGroup1.check(preferences.getRad1());
        radioGroup2.check(preferences.getRad2());
        radioGroup3.check(preferences.getRad3());
        radioGroup4.check(preferences.getRad4());
        radioGroup5.check(preferences.getRad5());
        maxLoc.setText(Integer.toString(preferences.getMaxLoc()));
        timer.setText(preferences.timer());
    }

    @Override
    public void onClick(View v) {

        switch (radioGroup1.getCheckedRadioButtonId()) {
            case R.id.percentSmall:
                sizeOfTitle = SMALL;
                break;
            case R.id.percentMedium:
                sizeOfTitle = MEDIUM;
                break;
            case R.id.percentLarge:
                sizeOfTitle = LARGE;
                break;
            default:
                return;
        }

        switch (radioGroup2.getCheckedRadioButtonId()) {
            case R.id.maxSmall:
                sizeOfMaxText = SMALL;
                break;
            case R.id.maxMedium:
                sizeOfMaxText = MEDIUM;
                break;
            case R.id.maxLarge:
                sizeOfMaxText = LARGE;
                break;
            default:
                return;
        }

        switch (radioGroup3.getCheckedRadioButtonId()) {
            case R.id.circleBlack:
                colorOfCircle = COLOR_BLACK;
                break;
            case R.id.circleGrey:
                colorOfCircle = COLOR_GREY;
                break;
            case R.id.circleRed:
                colorOfCircle = COLOR_ORANGE;
                break;
            default:
                return;
        }

        switch (radioGroup4.getCheckedRadioButtonId()) {
            case R.id.progGreen:
                colorOfProgress = COLOR_GREEN;
                break;
            case R.id.progRed:
                colorOfProgress = COLOR_ORANGE;
                break;
            case R.id.progYellow:
                colorOfProgress = COLOR_YELLOW;
                break;
            default:
                return;
        }

        switch (radioGroup5.getCheckedRadioButtonId()) {
            case R.id.textBlack:
                colorOfText = COLOR_BLACK;
                break;
            case R.id.textGrey:
                colorOfText = COLOR_GREY_DARK;
                break;
            case R.id.textRed:
                colorOfText = COLOR_ORANGE;
                break;
            default:
                return;
        }

        if (v.getId() == R.id.accept) {
            preferences.setTitleSize(sizeOfTitle);
            preferences.setMaxTxtSize(sizeOfMaxText);
            preferences.setColorOfCircle(colorOfCircle);
            preferences.setColorOfProgress(colorOfProgress);
            preferences.setTextColor(colorOfText);
            preferences.setRad1(radioGroup1.getCheckedRadioButtonId());
            preferences.setRad2(radioGroup2.getCheckedRadioButtonId());
            preferences.setRad3(radioGroup3.getCheckedRadioButtonId());
            preferences.setRad4(radioGroup4.getCheckedRadioButtonId());
            preferences.setRad5(radioGroup5.getCheckedRadioButtonId());
            newTimer = timer.getText().toString();


            if (newTimer.equals(""))
                newTimer = null;


            newMaxLoc = maxLoc.getText().toString();
            if (newMaxLoc.equals("")) {
                newMaxLoc = "10";
            }

            preferences.setTimer(newTimer);
            preferences.setMaxLoc(Integer.valueOf(newMaxLoc));

            finish();

        }


    }
}
