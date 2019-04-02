package com.newbee.smart_album.tools;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;

@Component
public class PhotoTool {

    public final String LOCAL_DIR;

    public final String TEMP_DIR = "/images/temp/";

    public final String UPLOAD_DIR = "/images/";

    public final String THUMBNAIL_DIR = "/images/thumbnail/";

    public final String DEFAULT_AVATAR_FILE = "/images/avatar/default_avatar.png";

    public final String DEFAULT_COVER_FILE = "/images/cover/default_cover.png";

    private static final String[] ALLOW_SUFFIX = {"jpg","jpeg","png","bmp","tiff","tif","gif"};

    public PhotoTool()
    {
        //生成相对路径
        File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        String path = file.getParentFile().getParentFile().getParentFile().getAbsolutePath();
        int start = path.indexOf("/file:");
        if(start != -1)
            LOCAL_DIR = path.substring(0,start);
        else
            LOCAL_DIR = path;
        System.err.println("路径是" + LOCAL_DIR);
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

    public boolean isPng(String suffix)
    {
        return "png".equals(suffix.toLowerCase());
    }

    public boolean isBmp(String suffix)
    {
        return "bmp".equals(suffix.toLowerCase());
    }

    public boolean isTiff(String suffix)
    {
        return "tif".equals(suffix.toLowerCase()) || "tiff".equals(suffix.toLowerCase());
    }

    public boolean isGif(String suffix)
    {
        return "gif".equals(suffix.toLowerCase());
    }

    public Timestamp exifTimeToTimestamp(String exifTime)
    {
        int tag = exifTime.indexOf(" ");
        String date = exifTime.substring(0,tag);
        String time = exifTime.substring(tag + 1);
        date = date.replace(":","-");
        return Timestamp.valueOf(date + " " + time);
    }

    public String encodeImageToBase64(File imageFile,String suffix) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, suffix.toLowerCase(), outputStream);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(outputStream.toByteArray());
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
