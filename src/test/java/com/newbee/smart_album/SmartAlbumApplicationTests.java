package com.newbee.smart_album;

import com.newbee.smart_album.dao.mapper.TagMapper;
import com.newbee.smart_album.dao.mapper.TempFileMapper;
import com.newbee.smart_album.tools.PhotoTool;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmartAlbumApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PhotoTool photoTool;

    @Resource
    private TempFileMapper tempFileMapper;

    @Resource
    private TagMapper tagMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Test
    public void testAI() throws MalformedURLException, IOException {
        String base64 = encodeImageToBase64(new File(photoTool.LOCAL_DIR + "/images/10/屏幕快照 2019-03-24 下午2.07.04.png"));
        SortedMap<String,Object> map = new TreeMap<>();
        map.put("app_id",2113835477);
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
        params.add("app_id",map.get("app_id"));
        params.add("time_stamp",map.get("time_stamp"));
        params.add("nonce_str",map.get("nonce_str"));
        params.add("sign",map.get("sign"));
        params.add("image",map.get("image"));
        HttpEntity<MultiValueMap<String,Object>> request = new HttpEntity<>(params,httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(url,request,String.class);
        System.out.println(response.getBody());

    }

    public String calculationSign(Map<String,Object> map)
    {
        StringBuilder result = new StringBuilder();
        for(String key : map.keySet())
        {
            if(!"".equals(map.get(key).toString()))
            {
                result.append(key + "=" + URLEncoder.encode(map.get(key).toString()) + "&");
            }
        }
        result.append("app_key=L8enOrgzXIEJsqNZ");
        return DigestUtils.md5DigestAsHex(result.toString().getBytes()).toUpperCase();
    }

    public String encodeImageToBase64(File imageFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(outputStream.toByteArray());
    }

    @Test
    public void testJson() throws IOException
    {
        /**
         * 获取API访问token
         * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
         * @param ak - 百度云官网获取的 API Key
         * @param sk - 百度云官网获取的 Securet Key
         * @return assess_token 示例：
         * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
         */
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + "tjQGBnNkwakPkMtjoAHEQ3nE"
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + "LHyDms6VzAu6k3xzN6eMRb40o8rEgE1s";
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
//            JSONObject jsonObject = new JSONObject(result);
//            String access_token = jsonObject.getString("access_token");
//            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }

    }

    @Test
    public void testEmail() throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage,true);
        helper.setFrom("497163175@qq.com");
        helper.setTo("g2409197994@gmail.com");
        helper.setSubject("test");

        Context context = new Context();
        //context.setVariable("url","test");
        Map<String,Object> map = new HashMap<>();
        map.put("url","test");
        context.setVariables(map);

        String text = templateEngine.process("retrievePasswordEmail",context);
        helper.setText(text,true);

        mailSender.send(mailMessage);
    }
}
