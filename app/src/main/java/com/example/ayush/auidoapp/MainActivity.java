package com.example.ayush.auidoapp;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    SeekBar volumeControl;
    SeekBar scrubControl;
    int maxVol;
    int maxProgress;
    int currVol;
    int currProgress;

    public void play(View view) {
        mediaPlayer.start();
    }

    public void pause(View view)
    {
        mediaPlayer.pause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.car);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        maxProgress = mediaPlayer.getDuration();

        volumeControl = (SeekBar) findViewById(R.id.volumeSeekBar);
        volumeControl.setMax(maxVol);
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.i("Seekbar Changed", Integer.toString(i));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        scrubControl = (SeekBar) findViewById(R.id.progressSeekBar);
        scrubControl.setMax(maxProgress);
        scrubControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.i("P_Seekbar Changed", Integer.toString(i));
                mediaPlayer.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
            }
        });
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currProgress = mediaPlayer.getCurrentPosition();
                scrubControl.setProgress(currProgress, true);
                currVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                volumeControl.setProgress(currVol, true);
            }
        },0, 250);
    }
}
