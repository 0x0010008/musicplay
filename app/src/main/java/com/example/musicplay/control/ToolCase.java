package com.example.musicplay.control;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToolCase {
    public static String parseSecToTimeStr(int sec)
    {
        Date date=new Date(sec*1000);
        SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
        String resTime=sdf.format(date);
        return resTime;
    }
}
