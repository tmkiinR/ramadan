package com.ramadan.azkar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.widget.VideoView;

import java.util.Locale;

import com.ramadan.azkar.R;


/**
 * Created by IronCodes on 11.04.2019.
 * Website : http://ironcodes.tech/
 * "All rights reserved ©2014-2019"
 * Support & Emails : dev@ironcodes.tech , ironcodesdev@gmail.com
 */



public class SplashActivity extends Activity implements Runnable {
    public String USER_LANGUAGES = "user_langu";
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userlango = preferences.getString(USER_LANGUAGES , "ar");

        Locale locale = new Locale(userlango);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && preferences.getString(USER_LANGUAGES, "").equalsIgnoreCase("")) {

            videoView = (VideoView) findViewById(R.id.splashvideoView);
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.ramadan_anim);
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (isFinishing())
                        return;
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();

                }
            });
        }

        try {
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion

                    (MediaPlayer mp) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                String userlango = preferences.getString(USER_LANGUAGES , "ar");
                if (userlango !=null) {
                    Locale locale = new Locale(userlango);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                }

                jump();
            }
        });
        videoView.start();
    } catch (Exception ex) {
        jump();
    }
}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        jump();
        return true;
    }

    private void jump() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }






    @Override
    public void run() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


}
