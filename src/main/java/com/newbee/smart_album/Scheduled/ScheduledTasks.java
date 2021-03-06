package com.newbee.smart_album.Scheduled;

import com.newbee.smart_album.dao.mapper.PhotoMapper;
import com.newbee.smart_album.dao.mapper.RetrievePasswordMapper;
import com.newbee.smart_album.dao.mapper.TempFileMapper;
import com.newbee.smart_album.dao.mapper.UserMapper;
import com.newbee.smart_album.entity.Photo;
import com.newbee.smart_album.externalAPI.Baidu;
import com.newbee.smart_album.tools.PhotoTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private PhotoTool photoTool;

    @Autowired
    private Baidu baidu;

    @Resource
    private TempFileMapper tempFileMapper;

    @Resource
    private PhotoMapper photoMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RetrievePasswordMapper retrievePasswordMapper;

    //每天检查一次回收站是否有过期照片
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void cleanRecycleBin()
    {
        List<Integer> list = photoMapper.selectPhotoIdWhereExpired();
        for(int id : list)
        {
            Photo photo = photoMapper.selectAllByPhotoId(id);
            String path = photo.getPath();
            photoMapper.deleteByPhotoId(id);
            userMapper.updatePhotoInRecycleBinAmountByUserId(photo.getUserId(),-1);
            File file = new File(photoTool.LOCAL_DIR + path);
            file.delete();
        }
    }

    //每天清理一次24小时之前的临时文件
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void cleanTempFile()
    {
        List<Integer> list = tempFileMapper.selectTempFileIdWhereExpired();
        for(int id : list)
        {
            String path = tempFileMapper.selectPathByTempFileId(id);
            File file = new File(photoTool.LOCAL_DIR + path);
            file.delete();
            tempFileMapper.deleteByTempFileId(id);
        }
    }

    //每天清理一次24小时之前的找回密码申请id
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void cleanSid()
    {
        List<Integer> list = retrievePasswordMapper.selectRetrievePasswordIdWhereExpired();
        for(int id : list)
        {
            retrievePasswordMapper.deleteByRetrievePasswordId(id);
        }
    }

    //每25天更新一次baidu的access_token
    @Scheduled(fixedRate = 1000L * 60 * 60 * 24 * 25)
    public void updateBaiduAccessToken()
    {
        baidu.updateAccessToken();
    }
}
