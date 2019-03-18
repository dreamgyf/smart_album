package com.newbee.smart_album.tools;

import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Timestamp;

@Component
public class PhotoTool {

    public final String TEMP_DIR;

    public final String UPLOAD_DIR;

    public final String DEFAULT_AVATAR_FILE;

    public final String DEFAULT_COVER_FILE;

    private static final String[] ALLOW_SUFFIX = {"jpg","jpeg","png","bmp","tiff"};

    public PhotoTool()
    {
        //生成相对路径
        File file = new File(this.getClass().getResource("/").getPath());
        String path = file.getParentFile().getParent();
        TEMP_DIR = path + "/image/temp/";
        UPLOAD_DIR = path + "/image/";
        DEFAULT_AVATAR_FILE = path + "/image/avatar/default_avatar.png";
        DEFAULT_COVER_FILE = path + "/image/cover/default_cover.png";
    }

    public boolean checkSuffix(String suffix)
    {
        for(String c : ALLOW_SUFFIX)
        {
            if(c.equals(suffix.toLowerCase()))
                return true;
        }
        return false;
    }

    public boolean isJpeg(String suffix)
    {
        return ("jpeg".equals(suffix.toLowerCase()) || "jpg".equals(suffix.toLowerCase()));
    }

//    public boolean is_png(String suffix)
//    {
//        return "png".equals(suffix.toLowerCase());
//    }

    public Timestamp exifTimeToTimestamp(String exifTime)
    {
        int tag = exifTime.indexOf(" ");
        String date = exifTime.substring(0,tag);
        String time = exifTime.substring(tag + 1);
        date = date.replace(":","-");
        return Timestamp.valueOf(date + " " + time);
    }

//    public Timestamp pngTimeToTimestamp(String png_time)
//    {
//        String year = png_time.substring(png_time.lastIndexOf(" ") + 1);
//        int temp = png_time.indexOf(" ");
//        png_time = png_time.substring(temp + 1);
//        temp = png_time.indexOf("月");
//        String mouth = png_time.substring(0,temp);
//        png_time = png_time.substring(temp + 2);
//        temp = png_time.indexOf(" ");
//        String day = png_time.substring(0,temp);
//        png_time = png_time.substring(temp + 1);
//        temp = png_time.indexOf(" ");
//        String time = png_time.substring(0,temp);
//        String fullTime = year + "-" + mouth + "-" + day + " " + time;
//        return Timestamp.valueOf(fullTime);
//    }
}
