package com.example.kasparsfisers.loginapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kasparsfisers.loginapp.data.User;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.kasparsfisers.loginapp.GalleryActivity.decodeToBase64;


public class ProfileView extends RelativeLayout {
    View rootView;
    TextView profileName;
    TextView profileEmail;
    CircleImageView profileImg;
    Bitmap imageB;



    public ProfileView(Context context) {
        super(context);
        init(context);
    }

    public ProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }



    private void init(final Context context) {
        rootView = inflate(context, R.layout.profile_fields, this);
        profileName = (TextView) rootView.findViewById(R.id.profileName);
        profileEmail = (TextView) rootView.findViewById(R.id.profileEmail);
        profileImg = (CircleImageView) rootView.findViewById(R.id.profileImg);

        profileImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        getContext(),GalleryActivity.class);

               context.startActivity(i);
            }
        });

    }

    public void setUser(User user){

        if (user.hasImage()) {
            imageB = decodeToBase64(user.getImageId());
            profileName.setText(user.getName());
            profileEmail.setText(user.getEmail());
            profileImg.setImageBitmap(imageB);

        } else {
            profileName.setText(user.getName());
            profileEmail.setText(user.getEmail());
            profileImg.setImageResource(R.drawable.empty_user);
        }


    }



}