package com.newbee.smart_album.externalAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbee.smart_album.tools.PhotoTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Component
public class Baidu {

    @Autowired
    private PhotoTool photoTool;

    public final static String API_KEY = "tjQGBnNkwakPkMtjoAHEQ3nE";

    public final static String SECRET_KEY = "LHyDms6VzAu6k3xzN6eMRb40o8rEgE1s";

    public static String ACCESS_TOKEN;

    public String photoTagIdentification(File imageFile, String suffix) {
        String base64 = photoTool.encodeImageToBase64(imageFile, suffix);
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general?access_token=" + ACCESS_TOKEN;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> params= new LinkedMultiValueMap<>();
        params.add("image", base64);
        HttpEntity<MultiValueMap<String,Object>> request = new HttpEntity<>(params,httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(url,request,String.class);
        return response.getBody();
    }
    public void updateAccessToken()
    {
        ACCESS_TOKEN = getAuth();
    }
    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     * @return access_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public String getAuth() {
        String ak = API_KEY;
        String sk = SECRET_KEY;
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JsonNode jsonNode = new ObjectMapper().readValue(result,JsonNode.class);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

    public List<Map<String ,Object>> photoTag(String tagJsonString) throws IOException {
        JsonNode json = new ObjectMapper().readValue(tagJsonString, JsonNode.class);
        JsonNode result = json.path("result");
        Iterator<JsonNode> resultList = result.elements();
        List<Map<String ,Object>> tagListReturn = new ArrayList<>();
        while(resultList.hasNext())
        {
            JsonNode finalResult = resultList.next();
            if(tagListReturn.size() == 0)
            {
                Map<String,Object> map1 = new HashMap<>();
                map1.put("keyword",finalResult.get("root").asText());
                map1.put("score",finalResult.get("score").asDouble());
                tagListReturn.add(map1);
            }
            for(int i = 0;i < tagListReturn.size();i++)
            {
                Map<String ,Object> tempMap = tagListReturn.get(i);
                if(tempMap.get("keyword").toString().equals(finalResult.get("root").asText()))
                    break;
                if(i == tagListReturn.size() - 1)
                {
                    Map<String,Object> map1 = new HashMap<>();
                    map1.put("keyword",finalResult.get("root").asText());
                    map1.put("score",finalResult.get("score").asDouble());
                    tagListReturn.add(map1);
                }
            }
            Map<String,Object> map2 = new HashMap<>();
            map2.put("keyword",finalResult.get("keyword").asText());
            map2.put("score",finalResult.get("score").asDouble());
            tagListReturn.add(map2);
        }
        return tagListReturn;
    }
}
