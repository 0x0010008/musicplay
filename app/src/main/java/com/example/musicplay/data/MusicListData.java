package com.example.musicplay.data;

import com.example.musicplay.models.MusicList;

public class MusicListData {
    private static MusicList musicList;

    public static MusicList getMusicList() {
        return musicList;
    }

    public static void setMusicList(MusicList musicList) {
        MusicListData.musicList = musicList;
    }
}
