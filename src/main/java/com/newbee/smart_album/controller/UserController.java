package com.newbee.smart_album.controller;

import com.newbee.smart_album.entity.User;
import com.newbee.smart_album.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Map<String, Object> register(@RequestBody Map<String,String> map)
    {
        userService.register(map.get("username"),map.get("password"),map.get("email"));
        Map<String,Object> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody Map<String,String> map, HttpServletRequest request, HttpServletResponse response)
    {
        int userId = userService.login(map.get("username"),map.get("password"));
        HttpSession session = request.getSession();
        session.setAttribute("userId",userId);
        session.setMaxInactiveInterval(5 * 60 * 60);
        Map<String,Object> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/home")
    public Map<String,Object> home(HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject != null)
        {
            int userId = Integer.parseInt(userIdObject.toString());
            User user = userService.getUserDataByUserId(userId);
            Map<String,Object> map = new HashMap<>();
            map.put("userId",user.getUserId());
            map.put("username",user.getUsername());
            map.put("email",user.getEmail());
            map.put("gender",user.getGender());
            return map;
        }
        else
            return null;
    }


}
