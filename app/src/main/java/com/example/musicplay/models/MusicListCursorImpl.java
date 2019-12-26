package com.example.musicplay.models;

import java.util.List;
import java.util.Random;

public class MusicListCursorImpl implements MusicListCursor {
    private List<Music> collection;
    private int[] defaultArr;
    private int[] randomArr;
    private int pos;
    private LoopStatue loopStatue;

    /**
     * 基本构造方法
     * @param collection 音乐列表
     * @param pos 首次播放位置
     * @param loopStatue 循环模式
     */
    public MusicListCursorImpl(List<Music> collection, int pos, LoopStatue loopStatue) {
        this.collection = collection;
        this.pos = pos;
        this.loopStatue = loopStatue;
        defaultArr=new int[collection.size()];
        randomArr=new int[collection.size()];
        for(int i=0;i<defaultArr.length;i++)defaultArr[i]=i;
        for(int i=0;i<randomArr.length;i++)randomArr[i]=i;
    }

    /**
     * 默认从列表第一首开始播放的构造方法
     * @param collection 音乐列表
     * @param loopStatue 循环模式
     */
    public MusicListCursorImpl(List<Music> collection, LoopStatue loopStatue) {
        this.collection = collection;
        this.loopStatue = loopStatue;
        this.pos=0;
        defaultArr=new int[collection.size()];
        randomArr=new int[collection.size()];
        for(int i=0;i<defaultArr.length;i++)defaultArr[i]=i;
        for(int i=0;i<randomArr.length;i++)randomArr[i]=i;
    }

    /**
     * 默认从第一首开始播放，且循环模式为列表循环的构造方法
     * @param collection 音乐列表
     */
    public MusicListCursorImpl(List<Music> collection) {
        this.collection = collection;
        this.loopStatue=LoopStatue.loop;
        this.pos=0;
        defaultArr=new int[collection.size()];
        randomArr=new int[collection.size()];
        for(int i=0;i<defaultArr.length;i++)defaultArr[i]=i;
        for(int i=0;i<randomArr.length;i++)randomArr[i]=i;
    }


    /**
     * 使用洗牌算法生成随机音乐列表
     */
    private void shuffleMusic()
    {
        Random rd = new Random();
        for(int i=0;i<randomArr.length;i++)
        {
            int j = rd.nextInt(randomArr.length);//生成随机数
            int temp = randomArr[i];//交换
            randomArr[i]=randomArr[j];
            randomArr[j]=temp;
        }
    }

    private void nextLoop(int[] arr)
    {
        if(pos+1>=arr.length)
        {
            pos=0;
        }
        else
        {
            pos++;
        }
    }

    private void previousLoop(int[] arr)
    {
        if(pos-1<0)
        {
            pos=arr.length-1;
        }
        else
        {
            pos--;
        }
    }

    private boolean nextUnloop(int[] arr)
    {
        boolean res=true;
        if(pos+1>=arr.length)
        {
            res=false;
        }
        else
        {
            pos++;
        }
        return res;
    }

    private boolean prevoiusUnloop()
    {
        boolean res=true;
        if(pos-1<0)
        {
            res=false;
        }
        else
        {
            pos--;
        }
        return res;
    }

    //列表循环

    private void nextListLoop()
    {
        nextLoop(defaultArr);
    }

    private void previousListLoop()
    {
        previousLoop(defaultArr);
    }

    //列表播放

    private boolean nextListUnloop()
    {
        return nextUnloop(defaultArr);
    }

    private boolean previousListUnloop()
    {
        return prevoiusUnloop();
    }

    //随机循环

    private void nextRandomLoop()
    {
        nextLoop(randomArr);
    }

    private void previousRandomLoop()
    {
        previousLoop(randomArr);
    }

    //随机列表

    private boolean nextRandomUnloop()
    {
        return nextUnloop(randomArr);
    }

    private boolean previousRandomUnloop()
    {
        return prevoiusUnloop();
    }

    //单曲循环

    private void anySingleLoop()
    {
        //do nothing
    }

    //单曲播放

    private boolean anySingleUnloop()
    {
        return false;
    }

    @Override
    public boolean moveToNext() {
        boolean res=true;
        switch (loopStatue)
        {
            case loop:nextListLoop();break;
            case unloop:res=nextListUnloop();break;
            case looprandom:nextRandomLoop();break;
            case unlooprandom:res=nextRandomUnloop();break;
            case singleloop:anySingleLoop();break;
            case single:res=anySingleUnloop();break;
        }
        return res;
    }

    @Override
    public boolean moveToPrevious() {
        boolean res=true;
        switch (loopStatue)
        {
            case loop:previousListLoop();break;
            case unloop:res=previousListUnloop();break;
            case looprandom:previousRandomLoop();break;
            case unlooprandom:res=previousRandomUnloop();break;
            case singleloop:anySingleLoop();break;
            case single:res=anySingleUnloop();break;
        }
        return res;
    }

    @Override
    public void moveToPosition(int pos) {
        this.pos=pos;
    }

    @Override
    public Music getMusic() {
        Music resMusic;
        if(loopStatue==LoopStatue.unlooprandom||loopStatue==LoopStatue.unlooprandom)
        {
            resMusic=collection.get(randomArr[pos]);
        }
        else
        {
            resMusic=collection.get(defaultArr[pos]);
        }
        return resMusic;
    }

    @Override
    public int getPos() {
        int res;
        if(loopStatue==LoopStatue.unlooprandom||loopStatue==LoopStatue.unlooprandom)
        {
            res=randomArr[pos];
        }
        else
        {
            res=defaultArr[pos];
        }
        return res;
    }

    @Override
    public void setLoopStatue(LoopStatue loopStatue) {
        if(loopStatue==LoopStatue.looprandom||loopStatue==LoopStatue.unlooprandom)
        {
            shuffleMusic();
        }
        this.loopStatue=loopStatue;
    }

    @Override
    public LoopStatue getLoopStatue() {
        return loopStatue;
    }

    @Override
    public void setMusicList(List<Music> musicList) {
        this.collection=musicList;
        this.pos=0;
        defaultArr=new int[collection.size()];
        randomArr=new int[collection.size()];
        for(int i=0;i<defaultArr.length;i++)defaultArr[i]=i;
        for(int i=0;i<randomArr.length;i++)randomArr[i]=i;
    }

    @Override
    public List<Music> getMusicList() {
        return collection;
    }
}
