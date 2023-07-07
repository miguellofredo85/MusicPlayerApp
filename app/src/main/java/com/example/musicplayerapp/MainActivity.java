package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button btnBack, btnFwd, btnPause, btnPlay;
    TextView textViewTime, textViewSongName;
    SeekBar seekBar;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();

    double startTime = 0, endTime   = 0;
    int frwTime = 10000, backTime = 10000;
    static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//WIDGET
        btnBack  = findViewById(R.id.back);
        btnFwd   = findViewById(R.id.forward);
        btnPause = findViewById(R.id.pause);
        btnPlay  = findViewById(R.id.play);
        textViewTime = findViewById(R.id.textViewTime);
        textViewSongName = findViewById(R.id.textViewNameSong);
        seekBar = findViewById(R.id.seekbar);

        mediaPlayer = MediaPlayer.create(this, R.raw.memories);
        seekBar.setClickable(false);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusic();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });
        btnFwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if((startTime + frwTime) <= endTime){
                    startTime = startTime + frwTime;
                    mediaPlayer.seekTo((int) startTime);
                }else {
                    Toast.makeText(MainActivity.this, "Can't Jump Fwd", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if((startTime - backTime ) >= 0){
                    startTime = startTime - backTime;
                    mediaPlayer.seekTo((int) startTime);
                }else {
                    Toast.makeText(MainActivity.this, "Can't Jump back", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void PlayMusic() {
        mediaPlayer.start();
        endTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0){
            seekBar.setMax((int) endTime);
            oneTimeOnly = 1;
        }
        textViewTime.setText(String.format(
                "%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) endTime),
                TimeUnit.MILLISECONDS.toSeconds((long) endTime) -
                        TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) endTime))
        ));
        seekBar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime, 100);
    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            textViewSongName.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                    TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toSeconds((long) startTime))));

            seekBar.setProgress((int)startTime);
            handler.postDelayed(this, 100);
        }
    };

    }