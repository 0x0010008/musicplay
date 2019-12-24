package com.example.musicplay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {
    TextView songName,singer,firstTime,lastTime;
    ImageView album,before,start,next,listBack,repeatMode;
    SeekBar songProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
    }
    private void init(){
        songName.findViewById(R.id.play_local_music_song_name);
        singer.findViewById(R.id.play_local_music_singer);
        firstTime.findViewById(R.id.play_local_music_firstTime);
        lastTime.findViewById(R.id.play_local_music_lastTime);
        album.findViewById(R.id.play_local_music_album_picture);
        before.findViewById(R.id.play_local_music_before);
        start.findViewById(R.id.play_local_music_start);
        next.findViewById(R.id.play_local_music_next);
        listBack.findViewById(R.id.play_local_music_list_back);
        repeatMode.findViewById(R.id.play_local_music_repeat_mode);
        songProgressBar.findViewById(R.id.play_local_music_song_seekBar);
        before.setOnClickListener(this);
        start.setOnClickListener(this);
        next.setOnClickListener(this);
        listBack.setOnClickListener(this);
        repeatMode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_local_music_before:

                break;
            case R.id.play_local_music_start:

                break;
            case R.id.play_local_music_next:

                break;
            case R.id.play_local_music_repeat_mode:

                break;
            case  R.id.play_local_music_list_back:

                break;
        }


    }
}
