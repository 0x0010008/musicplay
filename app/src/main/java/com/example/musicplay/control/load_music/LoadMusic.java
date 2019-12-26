package com.example.musicplay.control.load_music;

import com.example.musicplay.control.MusicPlayException;
import com.example.musicplay.models.Music;

import java.io.File;

public interface LoadMusic {

    /**
     * 初始化音乐信息
     * @param file
     * @return
     */
    public Music musicFactory(File file) throws MusicPlayException;


    /**
     * 初始化Bass库
     */
    public void initControler() throws MusicPlayException;
}
