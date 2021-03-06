package com.example.musicplay.control.load_music;
import android.graphics.BitmapFactory;

import com.example.musicplay.control.MusicPlayException;
import com.example.musicplay.models.Music;
import com.example.musicplay.models.MusicInfo;
import com.un4seen.bass.BASS;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import java.io.File;
import java.lang.reflect.Field;


public class LoadMusicImpl implements LoadMusic {
    @Override
    public Music musicFactory(File file) throws MusicPlayException{
        Music music=new Music(file);
        try {
            MusicInfo info=new MusicInfo();
            AudioFile f = AudioFileIO.read(file);
            Tag tag = f.getTag();
            AudioHeader header= f.getAudioHeader();
            if(!tag.isEmpty()) {
                Artwork artwork = tag.getFirstArtwork();
                if (artwork != null) info.setImage(BitmapFactory.decodeByteArray(artwork.getBinaryData(), 0, artwork.getBinaryData().length));
                if(!(tag.getFirst(FieldKey.TITLE)==null||tag.getFirst(FieldKey.TITLE).equals("")))info.setSongName(tag.getFirst(FieldKey.TITLE));
                else info.setSongName(getFileName(file.getName()));
                if(!(tag.getFirst(FieldKey.ARTIST)==null||tag.getFirst(FieldKey.ARTIST).equals("")))info.setAuthor(tag.getFirst(FieldKey.ARTIST));
                else info.setAuthor("未知艺术家");
                if(!(tag.getFirst(FieldKey.ALBUM)==null||tag.getFirst(FieldKey.ALBUM).equals("")))info.setAlbum(tag.getFirst(FieldKey.ALBUM));
                else info.setAuthor("未知专辑");
            }
            info.setLength(header.getTrackLength());
            music.setMusicInfo(info);
        } catch (Exception e) {
            throw new MusicPlayException("加载音乐文件失败",e);
        }
        return music;
    }

    private String getFileName(String rawName)
    {
        String resStr="";
        String[] temp=rawName.split(".");
        if(temp.length>2)
        {
            for(int i=0;i<temp.length-1;i++)resStr+=temp[i];
        }
        else resStr=temp[0];
        return resStr;
    }

    @Override
    public void initControler() throws MusicPlayException {
        // initialize default output device
        if (!BASS.BASS_Init(-1, 44100, 0)) {
            throw new MusicPlayException("Bass库初始化失败");
        }

        // check for floating-point capability
        if (BASS.BASS_GetConfig(BASS.BASS_CONFIG_FLOAT) == 0) {
            // no floating-point channel support
            //floatable = 0;
            // enable floating-point (8.24 fixed-point in this case) DSP/FX instead
            BASS.BASS_SetConfig(BASS.BASS_CONFIG_FLOATDSP, 1);
        }
    }
}
