package com.example.musicplay.models;

import android.graphics.Bitmap;

public class MusicInfo {
    private String songName;//歌名
    private String author;//作者
    private String album;//专辑
    private int length;//曲长
    private Bitmap image;//专辑封面

    public MusicInfo(String author, String album, int length, Bitmap image) {
        this.author = author;
        this.album = album;
        this.length = length;
        this.image = image;
    }

    public MusicInfo() {
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
