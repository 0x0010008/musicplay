package com.example.musicplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplay.control.ToolCase;
import com.example.musicplay.models.Music;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LocalMusicListAdapter extends RecyclerView.Adapter<LocalMusicListAdapter.LocalMusicListViewHolder> {
    Context context;
    List<Music> mDatas;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){ //传递接口给activity
        this.onItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener{  //监听接口
        public void OnItemClick(View view,int position);
    }

    public LocalMusicListAdapter(Context context, List<Music> mDatas) {
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
        Music musicBean=mDatas.get(position);
//        holder.num.setText(musicBean.getMusicInfo().);
        holder.songName.setText(musicBean.getMusicInfo().getSongName());
        holder.singer.setText(musicBean.getMusicInfo().getAuthor());
        holder.album.setText(musicBean.getMusicInfo().getAlbum());
        holder.time.setText(ToolCase.parseSecToTimeStr(musicBean.getMusicInfo().getLength()));
        if(onItemClickListener!=null)holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        TextView songName,singer,album,time;
        public LocalMusicListViewHolder(@NonNull View itemView) {
            super(itemView);
//            num=itemView.findViewById(R.id.item_local_music_num);
            songName=itemView.findViewById(R.id.item_local_music_song);
            singer=itemView.findViewById(R.id.item_local_music_singer);
            album=itemView.findViewById(R.id.item_local_music_album);
            time=itemView.findViewById(R.id.item_local_music_time);
        }
    }
}
