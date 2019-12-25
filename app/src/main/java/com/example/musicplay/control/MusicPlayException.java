package com.example.musicplay.control;

import androidx.annotation.Nullable;

public class MusicPlayException extends Exception{
    private String msg;
    private Exception subException;


    public MusicPlayException(String msg) {
        this.msg=msg;
    }

    public MusicPlayException(String msg,Exception e) {
        this.msg=msg;
        this.subException=e;
    }

    public Exception getSubException() {
        return subException;
    }


    @Nullable
    @Override
    public String getMessage() {
        return msg;
    }
}
