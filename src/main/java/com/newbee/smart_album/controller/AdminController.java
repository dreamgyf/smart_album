package com.newbee.smart_album.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @RequestMapping("/onlineUserCount")
    public int onlineUserCount(HttpSession session)
    {
        Object onlineUserCountObject = session.getAttribute("onlineUserCount");
        if(onlineUserCountObject == null)
            return 0;
        else
            return Integer.parseInt(onlineUserCountObject.toString());
    }
}
