package com.example.musicplay.models;

import java.util.List;

public interface MusicListCursor {

    /**
     * 移动当前游标到模式下一首
     * @return 下一首是否可供播放
     */
    public boolean moveToNext();

    /**
     * 移动当前游标到模式上一首
     * @reutrn 上一首是否可供播放
     */
    public boolean moveToPrevious();

    /**
     * 移动当前游标到任意位置
     * @param pos
     */
    public void moveToPosition(int pos);

    /**
     * 得到目前游标标识的音乐
     * @return
     */
    public Music getMusic();

    /**
     * 得到目前游标在音乐列表的位置
     * @return
     */
    public int getPos();

    /**
     * 设置当前的循环模式
     * @param loopStatue
     */
    public void setLoopStatue(LoopStatue loopStatue);

    /**
     * 得到当前的循环模式
     * @return
     */
    public LoopStatue getLoopStatue();

    /**
     * 设置默认音乐列表
     * @param musicList
     */
    public void setMusicList(List<Music> musicList);

    /**
     * 得到默认音乐列表
     * @return
     */
    public List<Music> getMusicList();

}
