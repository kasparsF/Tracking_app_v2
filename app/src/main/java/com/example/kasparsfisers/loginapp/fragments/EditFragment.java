package com.example.kasparsfisers.loginapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.activities.GoogleMapsActivity;
import com.example.kasparsfisers.loginapp.utils.SharedPreferencesUtils;

public class EditFragment extends DialogFragment {
    EditText username, name, email, password, password2;
    Button btnRegister;
    String newUser, newEmail, newName, newPass, newPass2;
    SharedPreferencesUtils preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_frag, container, false);


        preferences = SharedPreferencesUtils.getInstance(getActivity());


        username = (EditText) rootView.findViewById(R.id.mUsername);
        name = (EditText) rootView.findViewById(R.id.mName);
        email = (EditText) rootView.findViewById(R.id.mEmail);
        password = (EditText) rootView.findViewById(R.id.mPass);
        password2 = (EditText) rootView.findViewById(R.id.mPass2);
        btnRegister = (Button) rootView.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Fragment", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), GoogleMapsActivity.class);
                startActivityForResult(intent, 1);
                dismiss();

            }
        });


        return rootView;
    }


}