package com.newbee.smart_album.externalAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbee.smart_album.tools.PhotoTool;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Component
public class Tencent {

    @Autowired
    private PhotoTool photoTool;

    private static final int APP_ID = ;

    private static final String APP_KEY = "";

    public String photoTagIdentification(File imageFile, String suffix)
    {
        String base64 = photoTool.encodeImageToBase64(imageFile, suffix);
        SortedMap<String,Object> map = new TreeMap<>();
        map.put("app_id",APP_ID);
        map.put("time_stamp",System.currentTimeMillis() / 1000);
        map.put("nonce_str", RandomStringUtils.random(32,true,true));
        map.put("sign","");
        map.put("image",base64);
        map.put("sign",calculationSign(map));
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.ai.qq.com/fcgi-bin/image/image_tag";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> params= new LinkedMultiValueMap<>();
        params.add("APP_ID",map.get("APP_ID"));
        params.add("time_stamp",map.get("time_stamp"));
        params.add("nonce_str",map.get("nonce_str"));
        params.add("sign",map.get("sign"));
        params.add("image",map.get("image"));
        HttpEntity<MultiValueMap<String,Object>> request = new HttpEntity<>(params,httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(url,request,String.class);
        return response.getBody();
    }

    public String calculationSign(Map<String,Object> map) {
        StringBuilder result = new StringBuilder();
        for (String key : map.keySet()) {
            if (!"".equals(map.get(key).toString())) {
                result.append(key + "=" + URLEncoder.encode(map.get(key).toString()) + "&");
            }
        }
        result.append("APP_KEY=").append(APP_KEY);
        return DigestUtils.md5DigestAsHex(result.toString().getBytes()).toUpperCase();
    }

    public List<String> photoTag(String tagJsonString) throws IOException {
        JsonNode json = new ObjectMapper().readValue(tagJsonString, JsonNode.class);
        List<String> tagListReturn = new ArrayList<>();
        if(json.get("msg").asText().equals("ok"))
        {
            JsonNode data = json.path("data");
            JsonNode tag = data.path("tag_list");
            Iterator<JsonNode> tagList =  tag.elements();
            while(tagList.hasNext())
            {
                JsonNode next = tagList.next();
                tagListReturn.add(next.get("tag_name").asText());
            }
        }
        return tagListReturn;
    }
}
