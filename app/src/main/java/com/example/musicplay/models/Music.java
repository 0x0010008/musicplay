package com.example.musicplay.models;

import java.io.File;

public class Music {
    private int musicHandler;
    private MusicInfo musicInfo;
    private File musicFile;

    public Music(File musicFile) {
        this.musicFile = musicFile;
    }

    public Music(int musicHandler, MusicInfo musicInfo, File musicFile) {
        this.musicHandler = musicHandler;
        this.musicInfo = musicInfo;
        this.musicFile=musicFile;
    }

    public Music(int musicHandler) {
        this.musicHandler = musicHandler;
    }

    public int getMusicHandler() {
        return musicHandler;
    }

    public void setMusicHandler(int musicHandler) {
        this.musicHandler = musicHandler;
    }

    public MusicInfo getMusicInfo() {
        return musicInfo;
    }

    public void setMusicInfo(MusicInfo musicInfo) {
        this.musicInfo = musicInfo;
    }

    public File getMusicFile() {
        return musicFile;
    }

    public void setMusicFile(File musicFile) {
        this.musicFile = musicFile;
    }


}
