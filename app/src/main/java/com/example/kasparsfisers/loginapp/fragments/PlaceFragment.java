package com.example.kasparsfisers.loginapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.kasparsfisers.loginapp.R;

import java.io.File;


public class PlaceFragment extends Fragment {
    private View mView;
    String picturePath;
    File output;

    public PlaceFragment() {
        // Required empty public constructor
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_place, container, false);
        picturePath = getArguments().getString("message");
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final ImageView picture = (ImageView) view.findViewById(R.id.pictureMap);

        picture.post(new Runnable() {
            @Override
            public void run() {

                output = new File(picturePath);

                if (output.exists()) {

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;

                    BitmapFactory.decodeFile(output.getAbsolutePath(), options);
                    double width = options.outWidth;
                    double height = options.outHeight;


                    double pictureHeight = picture.getHeight();
                    double realWidth = width / (height / pictureHeight);

                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(output.getAbsolutePath(), bmOptions);
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) realWidth, (int) pictureHeight, true);

                    LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((int) realWidth, (int) pictureHeight);
                    picture.setLayoutParams(parms);


                    picture.setImageBitmap(bitmap);


                }
            }
        });


    }


}
