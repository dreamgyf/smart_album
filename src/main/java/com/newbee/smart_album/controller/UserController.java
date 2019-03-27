package com.newbee.smart_album.controller;

import com.newbee.smart_album.exception.AlreadyLogInException;
import com.newbee.smart_album.exception.NotLogInException;
import com.newbee.smart_album.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Map<String, Object> register(@RequestBody Map<String,String> map,HttpServletRequest request)
    {
        if(request.getSession().getAttribute("userId") != null)
            throw new AlreadyLogInException();
        userService.register(map.get("username"),map.get("password"),map.get("email"));
        Map<String,Object> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody Map<String,String> map, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getSession().getAttribute("userId") != null)
            throw new AlreadyLogInException();
        int userId = userService.login(map.get("username"),map.get("password"));
        HttpSession session = request.getSession();
        session.setAttribute("userId",userId);
        Map<String,Object> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "logout")
    public Map<String, Object> logout(HttpServletRequest request)
    {
        request.getSession().removeAttribute("userId");
        Map<String,Object> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/changePassword")
    public Map<String,String> changePassword(@RequestParam String prePassword,@RequestParam String newPassword,HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        userService.changePassword(userId,prePassword,newPassword);
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/getInfo")
    public Map<String,Object> getInfo(HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        return userService.getInfo(userId);
    }

    @RequestMapping(value = "/editInfo",method = RequestMethod.POST)
    public Map<String,String> editInfo(@RequestParam(required = false) MultipartFile avatar,
                                       @RequestParam String nickname,
                                       @RequestParam int gender,
                                       @RequestParam String signature,
                                       HttpServletRequest request)
    {
        Object userIdObject = request.getSession().getAttribute("userId");
        if(userIdObject == null)
            throw new NotLogInException();
        int userId = Integer.parseInt(userIdObject.toString());
        userService.editInfo(userId,avatar,nickname,gender,signature);
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/retrievePasswordByEmail",method = RequestMethod.GET)
    public Map<String,String> retrievePasswordByEmail(@RequestParam String email)
    {
        userService.retrievePasswordByEmail(email);
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }

    @RequestMapping(value = "/verifySid",method = RequestMethod.GET)
    public Map<String,Object> verifySid(@RequestParam String sid)
    {
        int userId = userService.verifySid(sid);
        Map<String,Object> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        mapReturn.put("userId",userId);
        return mapReturn;
    }

    @RequestMapping(value = "/retrievePassword",method = RequestMethod.POST)
    public Map<String,String> retrievePassword(@RequestParam String sid,@RequestParam int userId,@RequestParam String newPassword)
    {
        userService.retrievePassword(sid,userId,newPassword);
        Map<String,String> mapReturn = new HashMap<>();
        mapReturn.put("status","ok");
        return mapReturn;
    }
}
