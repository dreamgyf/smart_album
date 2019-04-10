package com.newbee.smart_album.httpSessionListener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class OnlineUserStatistics implements HttpSessionListener {

    private int onlineUserCount = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        synchronized(this) {
            onlineUserCount++;
            se.getSession().setAttribute("onlineUserCount", onlineUserCount);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        synchronized(this) {
            onlineUserCount--;
            se.getSession().setAttribute("onlineUserCount", onlineUserCount);
        }
    }

}
