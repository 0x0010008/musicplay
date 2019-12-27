package com.example.musicplay;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.musicplay.control.MusicControler;
import com.example.musicplay.control.MusicPlayException;
import com.example.musicplay.control.load_music.LoadMusicImpl;
import com.example.musicplay.data.MusicCursor;
import com.example.musicplay.data.MusicListData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.musicplay.models.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    RecyclerView musicRv;
    FloatingActionButton scanfab;
    //数据源
    List<Music> mDatas;
    String directory;
    private LocalMusicListAdapter adapter;
    //记录当前播放对象
    private Music curmusic=null;
    //记录当前正在播放的位置
    int curposition=-1;
    List<File> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        musicRv=findViewById(R.id.local_music_rv);
        setSupportActionBar(toolbar);
        FloatingActionButton scanfab = findViewById(R.id.scanfab);
        initBass();//初始化音乐Bass库
        scanfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatas=syncMusicList();
                MusicListData.setMusicList(new MusicListCursorImpl(mDatas));
            }
        });
        mDatas=syncMusicList();

        MusicListData.setMusicList(new MusicListCursorImpl(mDatas));
        //创建适配器对象
        adapter=new LocalMusicListAdapter(this,mDatas);
        musicRv.setAdapter(adapter);
        //设置布局管理器，规定最终展示的形式，因为Recycle可以展示很多种形式，在这里选择ListView形式
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);//垂直向下，不反转正着来
        musicRv.setLayoutManager(linearLayoutManager);
        adapter.notifyDataSetChanged();//提示适配器更新
        //设置每一项的监听事件
        setEventListener();
    }

    private List<Music> syncMusicList()
    {
        if (Build.VERSION.SDK_INT >= 23)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        File scanPath = Environment.getExternalStorageDirectory();
        if(fileList==null)fileList=new ArrayList<>();
        else if(fileList.size()>0)fileList.clear();
        List<Music> musicList=new ArrayList<>();
        try {
            List<File> files=getFileList(scanPath);
            for(File file :files)
            {
                musicList.add((new LoadMusicImpl()).musicFactory(file));
            }
        } catch (MusicPlayException e) {
            MyToast(e.getMessage());
        }
        return musicList;
    }

    private List<File> getFileList(File dir) {
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i]); // 获取文件绝对路径
                } else if (fileName.endsWith("mp3")||fileName.endsWith("flac")||fileName.endsWith("wav")) { // 判断文件是否是音乐
                    String strFileName = files[i].getAbsolutePath();
                    fileList.add(files[i]);
                } else {
                    continue;
                }
            }

        }
        return fileList;
    }

    private void setEventListener() {
        /*设置每一项的点击事件*/
        adapter.setOnItemClickListener(new LocalMusicListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                try {
                    curposition = position;
                    MusicControler.playTarget(curposition);
                } catch (MusicPlayException e) {
                    MyToast(e.getMessage());
                }
//                    curmusic = mDatas.get(position);
//                    curmusic.musicHandler.startplay();//开始播放当前选择的音乐
                    Intent intent=new Intent(ListActivity.this,PlayActivity.class);//跳转
                    startActivity(intent);
            }
        });//调用传递过来的接口

    }
    public void initBass(){
        try {
            (new LoadMusicImpl()).initControler();
        } catch (MusicPlayException e) {
            MyToast(e.getMessage());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void MyToast(String error){
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }

}
