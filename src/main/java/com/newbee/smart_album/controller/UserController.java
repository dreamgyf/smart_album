package com.newbee.smart_album.controller;

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
        Map<String,Object> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

}
