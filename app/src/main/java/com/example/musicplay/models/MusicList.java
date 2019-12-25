package com.example.musicplay.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicList {
    private List<Music> collection;
    private int nextPos;
    private int nowPos;
    private LoopStatue loopStatue;
    private List<Music> randomCollection;

    /**
     * 从传入播放列表的任意位置开始播放
     * @param collection
     * @param nowPos
     * @param loopStatue
     */
    public MusicList(List<Music> collection, int nowPos, LoopStatue loopStatue) {
        this.collection = collection;
        this.nowPos = nowPos;
        if(collection.size()>1)this.nextPos=nowPos+1;
        else nextPos=0;
        this.loopStatue = loopStatue;
    }

    /**
     * 从传入播放列表的第一首开始播放
     * @param collection
     * @param loopStatue
     */
    public MusicList(List<Music> collection, LoopStatue loopStatue) {
        this.collection = collection;
        this.loopStatue = loopStatue;
        resetPos();
    }

    /**
     * 从传入播放列表的第一首开始播放，并且启用列表循环模式
     * @param collection
     */
    public MusicList(List<Music> collection) {
        this.collection = collection;
        loopStatue=LoopStatue.loop;
        resetPos();
    }

    /**
     * 重置播放位置信息，从第一首开始
     */
    private void resetPos()
    {
        this.nowPos = 0;
        if(collection.size()>1)this.nextPos=nowPos+1;
        else nextPos=0;
    }

    public boolean moveToNext()
    {
        if(getNextMusic()==null)return false;
        else return true;
    }

    public boolean moveToPrevious()
    {
        if(getPreviousMusic()==null)return false;
        else return true;
    }

    public List<Music> getCollection() {
        return collection;
    }

    public void setCollection(List<Music> collection) {
        this.collection = collection;
    }

    public int getNextPos() {
        int resPos;
        if(this.loopStatue==LoopStatue.looprandom||this.loopStatue==LoopStatue.unlooprandom)
        {
            Music nowMusic=randomCollection.get(nextPos);
            resPos= collection.indexOf(nowMusic);
        }
        else resPos= nextPos;
        return resPos;
    }

    public void setNextPos(int nextPos) {
        this.nextPos = nextPos;
        if(!(nextPos>=collection.size()))
        {
            this.nextPos=nextPos;
        }
    }

    public int getNowPos() {
        int resPos;
        if(this.loopStatue==LoopStatue.looprandom||this.loopStatue==LoopStatue.unlooprandom)
        {
            Music nowMusic=randomCollection.get(nowPos);
            resPos= collection.indexOf(nowMusic);
        }
        else resPos= nowPos;
        return resPos;
    }

    public void setNowPos(int nowPos) {
        if(!(nowPos>=collection.size()))
        {
            this.nowPos = nowPos;
            nextPos=nowPos+1;
        }
    }

    public LoopStatue getLoopStatue() {
        return loopStatue;
    }

    public void setLoopStatue(LoopStatue loopStatue) {
        //当循环状态发生改变时判断之前的状态是否为随机，如果是，将正在播放的音乐索引从randomCollection对应到collection，防止切换到单曲播放时出现播错音乐
//        if(this.loopStatue==LoopStatue.looprandom||this.loopStatue==LoopStatue.unlooprandom)
//        {
//            Music nowMusic=randomCollection.get(nowPos);
//            nowPos=collection.indexOf(nowMusic);
//            nextPos=nowPos+1;
//        }
        nowPos=getNowPos();
        nextPos=nowPos+1;
        this.loopStatue=loopStatue;
    }

    private Music getPrePositionMusic(List<Music> collection)
    {
        Music resMusic=null;
        if(nowPos>0)
        {
            resMusic=collection.get(nowPos-1);
            nowPos=nowPos-1;
            nextPos=nowPos+1;
        }
        return resMusic;
    }

    /**
     * 得到下一首音乐，需要检查是否为空，若为空表示不循环且播放至最后一首，请停止播放
     * @return
     */
    public Music getNextMusic()
    {
        Music music=null;
        switch (loopStatue)
        {
            case loop:music=getLoopMusic();
            case unloop:music=getUnloopMusic();
            case looprandom:music=getLoopRandomMusic();
            case unlooprandom:music=getUnloopRandomMusic();
            case single:music=getSingleMusic();
            case singleloop:music=getSingleLoopMusic();
        }
        return music;
    }

    /**
     * 得到现在播放/准备播放的音乐
     * @return
     */
    public Music getNowMusic()
    {
        return collection.get(getNowPos());
    }

    /**
     * 得到前一首音乐,若已是第一首，则返回null
     * @return
     */
    public Music getPreviousMusic()
    {
        Music resMusic=null;
        if(this.loopStatue==LoopStatue.looprandom||this.loopStatue==LoopStatue.unlooprandom)
        {
            resMusic=getPrePositionMusic(randomCollection);
        }
        else{
            resMusic=getPrePositionMusic(collection);
        }
        return resMusic;
    }

    private void cloneList()
    {
        randomCollection=new ArrayList<>();
        for(Music music:collection)randomCollection.add(music);
    }

    /**
     * 使用洗牌算法生成随机音乐列表
     */
    private void shuffleMusic()
    {
        cloneList();
        Random rd = new Random();
        for(int i=0;i<randomCollection.size();i++)
        {
            int j = rd.nextInt(randomCollection.size());//生成随机数
            Music temp = randomCollection.get(i);//交换
            randomCollection.set(i,randomCollection.get(j));
            randomCollection.set(j,temp);
        }
    }

    /**
     * 循环方法，返回下一首音乐，传入一个音乐列表即可对该列表进行循环播放
     * @param musicList
     * @return
     */
    private Music loop(List<Music> musicList)
    {
        Music resMusic;
        if(nowPos+1>=musicList.size()){
            resMusic=musicList.get(0);
            resetPos();
        }
        else {
            resMusic= musicList.get(nextPos);
            nowPos=nextPos;
            nextPos++;
        }
        return resMusic;
    }

    /**
     * 不循环播放方法，返回下一首音乐，传入一个音乐列表即可进行不循环的播放，当播放至最后一首时下一首返回null
     * @param musicList
     * @return
     */
    private Music unloop(List<Music> musicList)
    {
        Music resMusic=null;
        if(nowPos+1>=musicList.size()){
            resetPos();
        }
        else {
            resMusic= musicList.get(nextPos);
            nowPos=nextPos;
            nextPos++;
        }
        return resMusic;
    }

    /**
     * 循环播放音乐列表得到的下一首音乐，如果列表只有一首音乐，请更改模式为单曲循环
     * @return
     */
    private Music getLoopMusic()
    {
        return loop(collection);
    }

    /**
     * 不循环播放音乐模式下得到的下一首歌，当为该列表的最后一首歌时，返回null
     * @return
     */
    private Music getUnloopMusic()
    {
        return unloop(collection);
    }

    /**
     * 随机循环模式下得到下一首歌，先打乱列表顺序，此时pos对应的音乐列表为randomCollection，不是collection
     * @return
     */
    private Music getLoopRandomMusic()
    {
        shuffleMusic();
        return loop(randomCollection);
    }

    /**
     * 随机不循环模式下得到下一首歌，先打乱列表顺序，此时pos对应的音乐列表为randomCollection，不是collection，若播放到最后一首歌，下一首歌返回null
     * @return
     */
    private Music getUnloopRandomMusic()
    {
        shuffleMusic();
        return unloop(randomCollection);
    }

    /**
     * 单曲播放模式，下一首歌直接返回空
     * @return
     */
    private Music getSingleMusic()
    {
        return null;
    }

    /**
     * 单曲循环模式，pos复位到同一值，然后返回同样歌曲
     * @return
     */
    private Music getSingleLoopMusic()
    {
        nextPos=nowPos;
        return collection.get(nextPos);
    }


}
