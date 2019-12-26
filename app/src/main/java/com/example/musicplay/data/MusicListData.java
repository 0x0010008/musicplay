package com.example.musicplay.data;

import com.example.musicplay.models.MusicListCursor;

public class MusicListData {
    private static MusicListCursor musicList;

    public static MusicListCursor getMusicList() {
        return musicList;
    }

    public static void setMusicList(MusicListCursor musicList) {
        MusicListData.musicList = musicList;
    }
}
