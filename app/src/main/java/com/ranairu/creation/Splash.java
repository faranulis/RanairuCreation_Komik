package com.ranairu.creation;

import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class Splash extends Helper {

    VideoView videoView;
    DisplayMetrics dm;
    Button skipAnim;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        videoView = (VideoView) findViewById(R.id.videoViewUtama);
        dm = new DisplayMetrics();
        skipAnim = (Button) findViewById(R.id.skipAnim);

        //buat jadi fullscreen dengan bantuan manifest android:theme="@style/Theme.AppCompat.NoActionBar"
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        File buatfolder = new File(Environment.getExternalStorageDirectory(), "/RanairuCreation/splash.mp4");

        if (buatfolder.exists()){
            Uri video = Uri.parse(String.valueOf(buatfolder));
            videoView.setVideoURI(video);
        } else {
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
            videoView.setVideoURI(video);
        }

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                try {
                    startNextActivity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        videoView.start();

        skipAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}