package com.example.musicplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicplay.memoryPool.ListDirectory;

import java.util.ArrayList;
import java.util.Timer;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {
    TextView songName,singer,firstTime,lastTime;
    ImageView album,before,start,next,listBack,repeatMode;
    SeekBar songProgressBar;
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentProgress;//当前音乐播放的进度
    private Timer timer;
    private int position;//传过来的在列表中的位置
    private int curposition;//现在在正在播放时间的位置
    private music curmusic;//现在正在播放的对象
    private List<music> mdatas;//拿到的列表
    private int mode=0;//记录歌曲循环模式 当mode=0时为列表循环（默认），当mode=1时为单曲循环

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initView();//初始化
        mdatas= ListDirectory.getMdatas();//得到列表
//        mdatas=new ArrayList<>();//实例化列表
        position=ListDirectory.getPosition();//得到在列表中的位置
        curmusic=mdatas.get(position);//根据位置得到对象   请大佬改进这个方法
        curposition=curmusic.getmusictime();//得到当前播放的时间位置
        loadMusic();//加载页面
    }
    /*得到对象和当前播放时间后加载页面*/
    private void loadMusic() {
        songName.setText(curmusic.getName());
        singer.setText(curmusic.getsinger());
        album.setImageResource(curmusic.getpicture);//得到当前专辑图片
        firstTime.setText(curposition);//设置当前播放时间
        lastTime.setText(curmusic.getdurtime());//设置音乐总共时长
        songProgressBar.setMax(curmusic.getdurtime());
    }

    private void initView(){
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
        songProgressBar.setOnSeekBarChangeListener(new MySeekBar());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_local_music_before:

                break;
            case R.id.play_local_music_start:
                playMusic();
                break;
            case R.id.play_local_music_next:

                break;
            case R.id.play_local_music_repeat_mode:

                break;
            case  R.id.play_local_music_list_back:

                break;
        }


    }
    /*播放音乐的函数*/
    private void playMusic() {
        if()
    }

    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }
        //滑动时应当停止计时器
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;

        }
        //滑动结束时，重新设置值
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            curmusic.toposition(songProgressBar.getProgress());//音乐跳到对应的位置
        }
    }
}
