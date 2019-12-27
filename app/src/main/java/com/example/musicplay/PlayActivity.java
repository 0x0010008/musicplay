package com.example.musicplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplay.control.MusicControler;
import com.example.musicplay.control.MusicPlayException;
import com.example.musicplay.control.MusicStatue;
import com.example.musicplay.control.ToolCase;
import com.example.musicplay.control.music_control.PlayToEnd;
import com.example.musicplay.data.MusicListData;
import com.example.musicplay.models.LoopStatue;
import com.example.musicplay.models.Music;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {
    TextView songName,singer,firstTime,lastTime;
    ImageView album,before,start,next,listBack,repeatMode;
    SeekBar songProgressBar;
    private int SynHandler;
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int pauseposition=0;//当前音乐播放的进度
    private Timer timer;
    private int curposition;//现在在正在播放时间的位置
    //private Music curmusic;//现在正在播放的对象
    private MusicStatue curStatue;//记录歌曲现在的播放情况
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver receiver;
//    private LoopStatue curLoopStatue;//记录当前循环状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadMusic();
            }
        };
        intentFilter =new IntentFilter();
        intentFilter.addAction("playend");
        localBroadcastManager.registerReceiver(receiver,intentFilter);
        initView();//初始化
        //curmusic=MusicListData.getMusicList().getMusic();
        try {
            //curmusic=MusicListData.getMusicList().getMusic();
            if(false)curposition=Integer.parseInt(ToolCase.parseSecToTimeStr((int)MusicControler.getNowPos()));//得到当前播放的时间位置
            curposition=0;
        } catch (MusicPlayException e) {
            MyToast(e.getMessage());
        }
        loadMusic();//加载页面

    }
    /*得到对象和当前播放时间后加载页面*/
    private void loadMusic() {

        songName.setText(MusicListData.getMusicList().getMusic().getMusicInfo().getSongName());
        singer.setText(MusicListData.getMusicList().getMusic().getMusicInfo().getAuthor());
        album.setImageBitmap(MusicListData.getMusicList().getMusic().getMusicInfo().getImage());//得到当前专辑图片
        firstTime.setText(ToolCase.parseSecToTimeStr(curposition));//设置当前播放时间
        lastTime.setText(ToolCase.parseSecToTimeStr(MusicListData.getMusicList().getMusic().getMusicInfo().getLength()));//设置音乐总共时长
        songProgressBar.setMax(MusicListData.getMusicList().getMusic().getMusicInfo().getLength());
        StartProgress();
    }

    private void initView(){
        songName=findViewById(R.id.play_local_music_song_name);
        singer=findViewById(R.id.play_local_music_singer);
        firstTime=findViewById(R.id.play_local_music_firstTime);
        lastTime=findViewById(R.id.play_local_music_lastTime);
        album=findViewById(R.id.play_local_music_album_picture);
        before=findViewById(R.id.play_local_music_before);
        start=findViewById(R.id.play_local_music_start);
        next=findViewById(R.id.play_local_music_next);
        listBack=findViewById(R.id.play_local_music_list_back);
        repeatMode=findViewById(R.id.play_local_music_repeat_mode);
        songProgressBar=findViewById(R.id.play_local_music_song_seekBar);
        before.setOnClickListener(this);
        start.setOnClickListener(this);
        next.setOnClickListener(this);
        listBack.setOnClickListener(this);
        repeatMode.setOnClickListener(this);
        songProgressBar.setOnSeekBarChangeListener(new MySeekBar());
        ModeChange();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_local_music_before:
                playBefore();
                break;
            case R.id.play_local_music_start:
                action_start_stop();
                break;
            case R.id.play_local_music_next:
                playNext();
                break;
            case R.id.play_local_music_repeat_mode:
                ModeChange();
                break;
            case R.id.play_local_music_list_back:
                ListBack();
                break;
        }


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
            try {
                MusicControler.jumpToPos(songProgressBar.getProgress());//音乐跳到对应的位置
            } catch (MusicPlayException e) {
                MyToast(e.getMessage());
            }
        }
    }
    private void MyToast(String error){
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }
    private void ModeChange(){
        if( MusicListData.getMusicList().getLoopStatue()==LoopStatue.loop)
        {MusicListData.getMusicList().setLoopStatue(LoopStatue.singleloop);
        repeatMode.setImageResource(R.drawable.ic_action_repeat_one);}
        else if(MusicListData.getMusicList().getLoopStatue()==LoopStatue.singleloop)
        {MusicListData.getMusicList().setLoopStatue(LoopStatue.looprandom);
        repeatMode.setImageResource(R.drawable.ic_action_random);}
        else if(MusicListData.getMusicList().getLoopStatue()==LoopStatue.looprandom)
        {MusicListData.getMusicList().setLoopStatue(LoopStatue.loop);
        repeatMode.setImageResource(R.drawable.ic_action_list_cycle);}
    }
    private void action_start_stop(){
        try {
            curStatue=MusicControler.getMusicStatue();
        } catch (MusicPlayException e) {
            MyToast(e.getMessage());
        }
        if (curStatue== MusicStatue.playing){
            try {
                MusicControler.pause();
                pauseposition=curposition;
                timer.cancel();
                start.setImageResource(R.drawable.ic_action_start);
                //进度条和firstTime的变化
            } catch (MusicPlayException e) {
                MyToast(e.getMessage());
            }
        }else if (curStatue==MusicStatue.pause){
            try {
                MusicControler.play();
                StartProgress();
                start.setImageResource(R.drawable.ic_action_pause);
                //进度条和first的变化
            } catch (MusicPlayException e) {
                MyToast(e.getMessage());
            }
        }
    }
    private void playBefore(){
        try {
            if(timer!=null)
            {
                timer.cancel();
            }
            MusicControler.playPrevious();
            start.setImageResource(R.drawable.ic_action_pause);
            loadMusic();
            StartProgress();
        } catch (MusicPlayException e) {
            MyToast(e.getMessage());
        }
    }
    private void playNext(){
        try {
            if(timer!=null)
            {
                timer.cancel();
            }
            MusicControler.playNext();
            start.setImageResource(R.drawable.ic_action_pause);
            loadMusic();
            StartProgress();
        } catch (MusicPlayException e) {
            MyToast(e.getMessage());
        }
    }
    private void ListBack(){
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(receiver);
    }
    private void StartProgress(){
        timer = new Timer();
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            curposition=Integer.parseInt((int)MusicControler.getNowPos()+"");//得到当前播放的时间位置
                            if(!isSeekBarChanging){
                                songProgressBar.setProgress(curposition);
                                firstTime.setText(ToolCase.parseSecToTimeStr(curposition));
                            }
                        } catch (MusicPlayException e) {
                            MyToast(e.getMessage());
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask,0,1000);
    }
}
