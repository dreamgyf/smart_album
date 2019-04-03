package com.newbee.smart_album;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.newbee.smart_album.dao.mapper")
@EnableScheduling
@ServletComponentScan
public class SmartAlbumApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartAlbumApplication.class, args);
    }

}
