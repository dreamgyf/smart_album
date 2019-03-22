package com.newbee.smart_album;

import com.newbee.smart_album.dao.mapper.TempFileMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmartAlbumApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Resource
    private TempFileMapper tempFileMapper;

    @Test
    public void contextLoads() {
    }


}
