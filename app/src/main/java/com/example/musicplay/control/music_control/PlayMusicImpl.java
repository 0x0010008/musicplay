package com.example.musicplay.control.music_control;


import com.example.musicplay.control.MusicPlayException;
import com.example.musicplay.control.MusicStatue;
import com.example.musicplay.data.MusicListData;
import com.example.musicplay.models.Music;
import com.un4seen.bass.BASS;

public class PlayMusicImpl implements PlayMusic {
    @Override
    public void loadToRam(Music music) throws MusicPlayException {
        int res;
        res=BASS.BASS_StreamCreateFile(music.getMusicFile().getPath(),0l,0l,BASS.BASS_SAMPLE_FLOAT);
        if(res>-1&&res<11)throw new MusicPlayException("文件读取异常");
        music.setMusicHandler(res);
    }

    @Override
    public void stop(Music music) throws MusicPlayException {
        checkMusicHandler(music);
        if(!BASS.BASS_ChannelStop(music.getMusicHandler()))throw new MusicPlayException("停止失败");
    }

    @Override
    public void play(Music music, final PlayToEnd playToEnd) throws MusicPlayException{
        checkMusicHandler(music);
        BASS.SYNCPROC callback=new BASS.SYNCPROC() {
            @Override
            public void SYNCPROC(int handle, int channel, int data, Object user) {
                try {
                    if(user!=null)((PlayToEnd)user).playToEndFunc();
                    BASS.BASS_StreamFree(handle);
                    if(MusicListData.getMusicList().getNextMusic()!=null){
                        loadToRam(MusicListData.getMusicList().getNextMusic());
                        play(MusicListData.getMusicList().getNextMusic(),playToEnd);
                    }
                } catch (MusicPlayException e) {
                    e.printStackTrace();
                }
            }
        };
        BASS.BASS_ChannelSetSync(music.getMusicHandler(), BASS.BASS_SYNC_MIXTIME,BASS.BASS_SYNC_END,callback,playToEnd);
        if(!BASS.BASS_ChannelPlay(music.getMusicHandler(), false))throw new MusicPlayException("播放失败");
    }

    @Override
    public void pause(Music music) throws MusicPlayException {
        checkMusicHandler(music);
        if(!BASS.BASS_ChannelPause(music.getMusicHandler()))throw new MusicPlayException("暂停失败");
    }

    @Override
    public MusicStatue getMusicStaute(Music music) throws MusicPlayException{
        checkMusicHandler(music);
        MusicStatue statue;
        switch (BASS.BASS_ChannelIsActive(music.getMusicHandler()))
        {
            case 1:statue=MusicStatue.playing;break;
            case 3:statue=MusicStatue.pause;break;
            default:statue=MusicStatue.stop;break;
        }
        return statue;
    }

    @Override
    public void jumpToTime(Music music, double sec) throws MusicPlayException {
        checkMusicHandler(music);
        long bits=BASS.BASS_ChannelSeconds2Bytes(music.getMusicHandler(),sec);
        if(!BASS.BASS_ChannelSetPosition(music.getMusicHandler(),bits, BASS.BASS_POS_BYTE))throw new MusicPlayException("时间跳转失败");
    }

    @Override
    public double getNowTime(Music music) throws MusicPlayException{
        checkMusicHandler(music);
        long bits=BASS.BASS_ChannelGetPosition(music.getMusicHandler(), BASS.BASS_POS_BYTE);
        return BASS.BASS_ChannelBytes2Seconds(music.getMusicHandler(),bits);
    }

    @Override
    public void destroy(Music music) throws MusicPlayException {
        checkMusicHandler(music);
        if(!BASS.BASS_StreamFree(music.getMusicHandler()))throw new MusicPlayException("销毁音乐内存失败");
        else music.setMusicHandler(0);
    }

    @Override
    public void chear() throws MusicPlayException {
        if(!BASS.BASS_Free())throw new MusicPlayException("从Ram清空音乐缓存失败");
    }

    private void checkMusicHandler(Music music) throws MusicPlayException {
        if(music==null)throw new MusicPlayException("音乐文件尚未加载");
        if(music.getMusicHandler()==0)throw new MusicPlayException("音乐尚未载入内存");
    }
}
