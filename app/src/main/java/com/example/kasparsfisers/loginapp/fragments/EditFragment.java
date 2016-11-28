package com.example.kasparsfisers.loginapp.fragments;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.activities.GoogleMapsActivity;
import com.example.kasparsfisers.loginapp.data.LocationContract;
import com.example.kasparsfisers.loginapp.utils.Functions;

public class EditFragment extends DialogFragment {
    EditText placeName;
    Button btnAccept;
    Uri mCurrentCoordinatesUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mCurrentCoordinatesUri = Uri.parse(bundle.getString(GoogleMapsActivity.CURRENT_URI, ""));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_place, container, false);

        placeName = (EditText) rootView.findViewById(R.id.mPlaceName);
        btnAccept = (Button) rootView.findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if(Functions.isEmpty(placeName.getText().toString())){
                    placeName.setError(getString(R.string.empty_error));
                    return;
                }

                ContentValues values = new ContentValues();
                values.put(LocationContract.LocationEntry.COLUMN_LOCNAME, placeName.getText().toString());


                int rowsAffected = getContext().getContentResolver().update(mCurrentCoordinatesUri, values, null, null);

                if (rowsAffected == 0) {

                } else {
                    Toast.makeText(getContext(), R.string.made_changes, Toast.LENGTH_SHORT).show();

                }
                dismiss();

            }
        });


        return rootView;
    }


}