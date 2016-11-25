package com.example.kasparsfisers.loginapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.utils.Functions;

import java.io.File;

public class DisplayPlaceActivity extends AppCompatActivity {
    ImageView bilde;
    private File output = null;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_place);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong(Functions.CURRENT_ID);
            // and get whatever type user account id is
        }
        bilde = (ImageView) findViewById(R.id.imageView2);


        File dir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        output = new File(dir, Functions.FOLDER_MAIN + "/" + id + ".jpeg");


        if (output.exists()) {
            bilde.setImageURI(Uri.fromFile(output));
        }

    }
}
