package com.bytecode.womenshakti;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class Video extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        VideoView videoView= (VideoView) findViewById(R.id.videoview);
        //videoView.setVideoPath("");
         videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/"+R.raw.video));
         videoView.start();
    }
}