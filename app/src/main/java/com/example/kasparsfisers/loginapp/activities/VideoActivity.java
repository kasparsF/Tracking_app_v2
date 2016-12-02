package com.example.kasparsfisers.loginapp.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.kasparsfisers.loginapp.R;

public class VideoActivity extends AppCompatActivity {
    VideoView view;
    MediaController mediaControls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        // Find your VideoView in your video_main.xml layout
         view = (VideoView)findViewById(R.id.videoView);

        if (mediaControls == null) {
            // create an object of media controller class
            mediaControls = new MediaController(this);
            mediaControls.setAnchorView(view);
        }
        // set the media controller for video view
        view.setMediaController(mediaControls);
        // set the uri for the video view
        String path = "android.resource://" + getPackageName() + "/" + R.raw.sample360;
        view.setVideoURI(Uri.parse(path));

        // implement on completion listener on video view
        view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getApplicationContext(), "Thank You...!!!", Toast.LENGTH_LONG).show(); // display a toast when an video is completed
            }
        });
        view.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getApplicationContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show(); // display a toast when an error is occured while playing an video
                return false;
            }
        });
    }
    }

