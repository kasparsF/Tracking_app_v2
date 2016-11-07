package com.example.kasparsfisers.loginapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kasparsfisers.loginapp.activities.MainScreen;
import com.example.kasparsfisers.loginapp.R;


public class ThirdFragment extends Fragment {
    Button next;


    public ThirdFragment() {
        // Required empty public constructor
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        next = (Button) view.findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MainScreen.class);
                getActivity().startActivity(intent);
                getActivity().finish();

            }
        });
        return view;
    }

}
