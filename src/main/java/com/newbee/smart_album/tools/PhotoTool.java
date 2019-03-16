package com.newbee.smart_album.tools;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class PhotoTool {

    public static final String temp_dir = "/Users/gaoyunfeng/smart_album/temp/";

    public static final String upload_dir = "/Users/gaoyunfeng/smart_album/";

    public static final String default_avatar_file = "/Users/gaoyunfeng/smart_album/avatar/default_avatar.png";

    public static final String default_cover_file = "/Users/gaoyunfeng/smart_album/cover/default_cover.png";

    private static final String[] allow_suffix = {"jpg","jpeg","png","bmp"};

    public boolean checkSuffix(String suffix)
    {
        for(String c : allow_suffix)
        {
            if(c.equals(suffix.toLowerCase()))
                return true;
        }
        return false;
    }

    public boolean is_jpeg(String suffix)
    {
        if("jpeg".equals(suffix.toLowerCase()) || "jpg".equals(suffix.toLowerCase()))
            return true;
        else
            return false;
    }

//    public boolean is_png(String suffix)
//    {
//        return "png".equals(suffix.toLowerCase());
//    }

    public Timestamp exifTimeToTimestamp(String exif_time)
    {
        int tag = exif_time.indexOf(" ");
        String date = exif_time.substring(0,tag);
        String time = exif_time.substring(tag + 1);
        date = date.replace(":","-");
        Timestamp timestamp = Timestamp.valueOf(date + " " + time);
        return timestamp;
    }
}
