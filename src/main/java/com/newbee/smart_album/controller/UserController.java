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

    @RequestMapping(value = "/register.do",method = RequestMethod.POST)
    public Map<String, Object> register(@RequestBody Map<String,String> map)
    {
        Map<String,Object> map_return = new HashMap<>();
        map_return.put("status",userService.register(map.get("username"),map.get("password"),map.get("email")));
        return map_return;
    }

    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody Map<String,String> map, HttpServletRequest request, HttpServletResponse response)
    {
        String state = userService.login(map.get("username"),map.get("password"));
        Map<String,Object> map_return = new HashMap<>();
        //1表示成功，-1表示密码错误，-2表示用户或邮箱不存在
        if(!state.equals("username or email does not exist") && !state.equals("wrong password"))
        {
            HttpSession session = request.getSession();
            session.setAttribute("user_id",Integer.parseInt(state));
            map_return.put("status","ok");
            return map_return;
        }
        else
        {
            map_return.put("status",state);
            return map_return;
        }
    }

    @RequestMapping(value = "/home")
    public Map<String,Object> home(HttpServletRequest request)
    {
        Object user_id_object = request.getSession().getAttribute("user_id");
        if(user_id_object != null)
        {
            int user_id = Integer.parseInt(user_id_object.toString());
            User user = userService.getUserDataByUserId(user_id);
            Map<String,Object> map = new HashMap<>();
            map.put("user_id",user.getUserId());
            map.put("username",user.getUsername());
            map.put("email",user.getEmail());
            map.put("gender",user.getGender());
            return map;
        }
        else
            return null;
    }


}
