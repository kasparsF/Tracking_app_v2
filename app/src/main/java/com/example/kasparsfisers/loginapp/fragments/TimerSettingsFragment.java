package com.example.kasparsfisers.loginapp.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.utils.SharedPreferencesUtils;

import static com.example.kasparsfisers.loginapp.R.id.btnSetInterval;


public class TimerSettingsFragment extends DialogFragment {
    EditText timer;
    Button btnSet;
    String sessionData;
    String newTimer;
    SharedPreferencesUtils preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.timer_frag, container, false);


        preferences = SharedPreferencesUtils.getInstance(getActivity());
        sessionData = preferences.sessionData();

        timer = (EditText) rootView.findViewById(R.id.mTime);

        btnSet = (Button) rootView.findViewById(btnSetInterval);
        btnSet.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

            newTimer = timer.getText().toString() + "000";
                preferences.setTimer(sessionData,newTimer);
                dismiss();
            }
        });


        return rootView;
    }
}