package com.example.musicplay;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.example.musicplay.memoryPool.*;

public class ListActivity extends AppCompatActivity {
    RecyclerView musicRv;
    //数据源
    List<music> mDatas;
    String directory;
    private LocalMusicListAdapter adapter;
    //记录当前播放对象
    private music curmusic=null;
    //记录当前正在播放的位置
    int curposition=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initView();
        mDatas=loadLocalmusic(directory);//需要扫描方法
        ListDirectory.setMdatas(mDatas);//放入静态库
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

    private void setEventListener() {
        /*设置每一项的点击事件*/
        adapter.setOnItemClickListener(new LocalMusicListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                if(curposition!=-1&&curmusic!=null){
                    //当前播放对象停止播放音乐  curposition=-1而注意curmusic没有为空的情况 在考虑
                    curmusic.musihandler.stopplay();
                    curposition=-1;
                    curmusic=null;
                }else {
                    curposition = position;
                    curmusic = mDatas.get(position);
                    curmusic.musicHandler.startplay();//开始播放音乐
                    ListDirectory.setPosition(curposition);//往交换区放入播放位置
                    Intent intent=new Intent(ListActivity.this,PlayActivity.class);//跳转
                    startActivity(intent);
                }
            }
        });//调用传递过来的接口

    }

    private void initView(){
        /*初始化控件的函数*/
        musicRv.findViewById(R.id.local_music_rv);


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
}
