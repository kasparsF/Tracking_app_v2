package com.example.kasparsfisers.loginapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.utils.SharedPreferencesUtils;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    RadioGroup radioGroup1,radioGroup2,radioGroup3,radioGroup4,radioGroup5;
    Button accept;
    String colorOfCircle = "##787878";
    String colorOfText = "#a91111";
    String colorOfProgress = "#bada55";
    int sizeOfTitle = 60;
    int sizeOfMaxText = 60;
    SharedPreferencesUtils preferences;

    EditText timer;
    String newTimer;
    EditText maxLoc;
    String newMaxLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        radioGroup1 = (RadioGroup) findViewById(R.id.rGroup1);
        radioGroup2 = (RadioGroup) findViewById(R.id.rGroup2);
        radioGroup3 = (RadioGroup) findViewById(R.id.rGroup3);
        radioGroup4 = (RadioGroup) findViewById(R.id.rGroup4);
        radioGroup5 = (RadioGroup) findViewById(R.id.rGroup5);
        timer = (EditText)findViewById(R.id.timeForTracking);
        maxLoc = (EditText)findViewById(R.id.maxLocations);
        preferences = SharedPreferencesUtils.getInstance(this);
        accept = (Button)findViewById(R.id.accept);
        accept.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (radioGroup1.getCheckedRadioButtonId()) {
            case R.id.percentSmall:
                sizeOfTitle = 30;
                break;
            case R.id.percentMedium:
                sizeOfTitle = 60;
                break;
            case R.id.percentLarge:
                sizeOfTitle = 90;
                break;
            default:
                return;
        }

        switch (radioGroup2.getCheckedRadioButtonId()) {
            case R.id.maxSmall:
                sizeOfMaxText = 30;
                break;
            case R.id.maxMedium:
                sizeOfMaxText = 60;
                break;
            case R.id.maxLarge:
                sizeOfMaxText = 90;
                break;
            default:
                return;
        }

        switch (radioGroup3.getCheckedRadioButtonId()) {
            case R.id.circleBlack:
                colorOfCircle = "#FF000000";
                break;
            case R.id.circleGrey:
                colorOfCircle = "#787878";
                break;
            case R.id.circleRed:
                colorOfCircle = "#a91111";
                break;
            default:
                return;
        }

        switch (radioGroup4.getCheckedRadioButtonId()) {
            case R.id.progGreen:
                colorOfProgress = "#bada55";
                break;
            case R.id.progRed:
                colorOfProgress = "#a91111";
                break;
            case R.id.progYellow:
                colorOfProgress = "#FFF1E61E";
                break;
            default:
                return;
        }

        switch (radioGroup5.getCheckedRadioButtonId()) {
            case R.id.textBlack:
                colorOfText = "#FF000000";
                break;
            case R.id.textGrey:
                colorOfText = "#787878";
                break;
            case R.id.textRed:
                colorOfText = "#a91111";
                break;
            default:
                return;
        }

        if(v.getId() == R.id.accept){
            preferences.setTitleSize(sizeOfTitle);
            preferences.setMaxTxtSize(sizeOfMaxText);
            preferences.setColorOfCircle(colorOfCircle);
            preferences.setColorOfProgress(colorOfProgress);
            preferences.setTextColor(colorOfText);

            newTimer = timer.getText().toString() + "000";
            if(newTimer.equals(""))
                newTimer = null;


            newMaxLoc = maxLoc.getText().toString();
            if(newMaxLoc.equals(""))
                newMaxLoc = "10";

            preferences.setTimer(newTimer);
            preferences.setMaxLoc(Integer.valueOf(newMaxLoc));


            startActivity(new Intent(SettingsActivity.this, MainScreenActivity.class));

        }


    }
}
