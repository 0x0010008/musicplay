package com.example.musicplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocalMusicListAdapter extends RecyclerView.Adapter<LocalMusicListAdapter.LocalMusicListViewHolder> {
    Context context;
    List<music> mDatas;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){ //传递接口给activity
        this.onItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener{  //监听接口
        public void OnItemClick(View view,int position);
    }

    public LocalMusicListAdapter(Context context, List<music> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }
    /*
    初始化
     */
    @NonNull
    @Override
    public LocalMusicListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_music,parent,false);
        LocalMusicListViewHolder holder=new LocalMusicListViewHolder(view);
        return holder;
    }
    /*
    绑定数据
     */
    @Override
    public void onBindViewHolder(@NonNull LocalMusicListViewHolder holder, final int position) {
        music musicBean=mDatas.get(position);
        holder.num.setText(musicBean.getnum);
        holder.songName.setText(musicBean.getname);
        holder.singer.setText(musicBean.getsinger);
        holder.album.setText(musicBean.getalbum);
        holder.time.setText(musicBean.gettime);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(v,position);  //接口回调

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class LocalMusicListViewHolder extends RecyclerView.ViewHolder{
        TextView num,songName,singer,album,time;
        public LocalMusicListViewHolder(@NonNull View itemView) {
            super(itemView);
            num=itemView.findViewById(R.id.item_local_music_num);
            songName=itemView.findViewById(R.id.item_local_music_song);
            singer=itemView.findViewById(R.id.item_local_music_singer);
            album=itemView.findViewById(R.id.item_local_music_album);
            time=itemView.findViewById(R.id.item_local_music_time);
        }
    }
}
