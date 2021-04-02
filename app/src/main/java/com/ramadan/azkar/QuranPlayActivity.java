package com.ramadan.azkar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.ramadan.azkar.utilities.Utilities;

import java.io.IOException;
import java.util.Locale;


/**
 * Created by IronCodes on 11.04.2019.
 * Website : http://ironcodes.tech/
 * "All rights reserved ©2014-2019"
 * Support & Emails : dev@ironcodes.tech , ironcodesdev@gmail.com
 */


public class QuranPlayActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    SeekBar seek_bar;
    TextView surahname;
    ImageView play_image;
    ImageView image_Rhythm;
    TextView txt_Status, current_time, sound_duration;
    MediaPlayer media_voice;
    AnimationDrawable mAnimation;
    Handler mHandler = new Handler();
    Utilities utils;
    ProgressDialog pDialog;
    public String USER_LANGUAGES = "user_langu";
    private AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quranplayer);

        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        adView.setAdSize(AdSize.BANNER);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layAdsQuranPlayer);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userlango = preferences.getString(USER_LANGUAGES, "ar");
        Locale locale = new Locale(userlango);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        media_voice = new MediaPlayer();
        linking_elements();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        utils = new Utilities();
        seek_bar.setOnSeekBarChangeListener(this);
        seek_bar.setMax(media_voice.getDuration());


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni == null) { // Check for Internet
                pDialog = ProgressDialog.show(QuranPlayActivity.this, getString(R.string.no_internet), getString(R.string.nointernet_msg));
                pDialog.setCancelable(true);
                play_image.setEnabled(false);
                seek_bar.setEnabled(false);
            } else {
                // Internet Check
                if (URLUtil.isValidUrl(getIntent().getExtras().getString("url"))) {
                    playQuran();
                } else {
                    Toast.makeText(this, getString(R.string.wrong_link), Toast.LENGTH_SHORT).show();
                    play_image.setEnabled(false);
                    seek_bar.setEnabled(false);
                }
            }

        }


        // Play and Stop
        play_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (media_voice.isPlaying()) {
                    if (media_voice != null) {
                        media_voice.pause();
                        stopRhythm();
                        txt_Status.setText(getString(R.string.player_stop));
                        play_image.setImageResource(R.drawable.img_btn_play);
                    }
                } else {
                    if (media_voice != null) {
                        media_voice.start();
                        startRhythm();
                        txt_Status.setText(getString(R.string.player_play));
                        play_image.setImageResource(R.drawable.img_btn_pause);


                    }
                }
            }
        });

    }


    public void linking_elements() {
        surahname = (TextView) findViewById(R.id.surahname);
        seek_bar = (SeekBar) findViewById(R.id.seekbar);
        txt_Status = (TextView) findViewById(R.id.quranCurrentDurationLabel);
        current_time = (TextView) findViewById(R.id.quranCurrentDurationLabel1);
        sound_duration = (TextView) findViewById(R.id.quranTotalDurationLabel);
        play_image = (ImageView) findViewById(R.id.btnPlay);
        image_Rhythm = (ImageView) findViewById(R.id.img_equilizer);
        image_Rhythm.setBackgroundResource(R.drawable.simple_animation);
        mAnimation = (AnimationDrawable) image_Rhythm.getBackground();
    }


    public void playQuran() {
        try {
            media_voice.reset();
            media_voice.setAudioStreamType(AudioManager.STREAM_MUSIC);
            media_voice.setDataSource(getIntent().getExtras().getString("url"));
            surahname.setText(getIntent().getExtras().getString("name"));
            txt_Status.setText(getString(R.string.sound_prepaer));

            media_voice.prepareAsync();
            media_voice.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    startRhythm();
                    updateProgressBar();
                    play_image.setImageResource(R.drawable.img_btn_pause);
                    txt_Status.setText(getString(R.string.player_play));
                }
            });

            media_voice.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    txt_Status.setText(getString(R.string.sound_finished));
                    current_time.setText("");
                    stopRhythm();
                    play_image.setImageResource(R.drawable.img_btn_play);
                }
            });

            play_image.setImageResource(R.drawable.img_btn_pause);

            seek_bar.setProgress(0);
            seek_bar.setMax(100);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void startRhythm() {

        image_Rhythm.post(new Runnable() {
            public void run() {
                mAnimation.start();
            }
        });
    }

    private void stopRhythm() {
        image_Rhythm.post(new Runnable() {
            public void run() {
                mAnimation.stop();
            }
        });
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            if (media_voice != null) {
                long totalDuration = media_voice.getDuration();
                long currentDuration = media_voice.getCurrentPosition();

                sound_duration.setText("" + utils.milliSecondsToTimer(totalDuration));
                current_time.setText("" + utils.milliSecondsToTimer(currentDuration));

                int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                seek_bar.setProgress(progress);

                mHandler.postDelayed(this, 100);
            }
        }
    };


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = media_voice.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        media_voice.seekTo(currentPosition);

        updateProgressBar();
    }


    @Override
    public void finish() {

        super.finish();
        if (media_voice != null) {
            if (media_voice.isPlaying()) {
                media_voice.stop();
            }
            media_voice.release();
            media_voice = null;
            finish();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        media_voice = null;
        if (media_voice != null) {
            media_voice.stop();

        }
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                if (media_voice != null) {
                    media_voice.stop();
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    seek_bar.setProgress(0);
                }


        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(mUpdateTimeTask);
        seek_bar.setProgress(0);
        finish();
        super.onBackPressed();
    }

}