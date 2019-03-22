package com.newbee.smart_album.tools;

import com.newbee.smart_album.dao.mapper.TempFileMapper;
import com.newbee.smart_album.exception.UploadFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class ZipTool {

    @Autowired
    private PhotoTool photoTool;

    @Resource
    private TempFileMapper tempFileMapper;

    public String createZip(List<String> fileFullName,List<String> filePath) {
        String zipPath = photoTool.TEMP_DIR + UUID.randomUUID() + ".zip";
        File zipFile = new File(photoTool.LOCAL_DIR + zipPath);
        if(!zipFile.getParentFile().exists())
        {
            if(!zipFile.getParentFile().mkdirs())
                throw new UploadFailedException();//上传失败,文件创建失败
        }
        InputStream inputStream = null;
        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            //设置缓冲区
            int len = 0;
            byte[] buffer = new byte[1024];
            for(int i = 0;i < fileFullName.size();++i)
            {
                inputStream = new FileInputStream(new File(photoTool.LOCAL_DIR + filePath.get(i)));
                zipOutputStream.putNextEntry(new ZipEntry(fileFullName.get(i)));
                while((len = inputStream.read(buffer)) > 0)
                {
                    zipOutputStream.write(buffer,0,len);
                }
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(zipOutputStream != null)
            {
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            tempFileMapper.insert(zipPath,new Timestamp(System.currentTimeMillis()));
        }
        return zipPath;
    }
}
