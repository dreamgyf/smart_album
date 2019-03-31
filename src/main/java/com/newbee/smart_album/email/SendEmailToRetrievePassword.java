package com.newbee.smart_album.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class SendEmailToRetrievePassword {

    private final String FROM = "497163175@qq.com";

    private final String DOMAIN = "http://localhost:3000";

    private final String ROUTE = "/retrievePassword";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void send(String to,String name,String sid) throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage,true);
        helper.setFrom(FROM);
        helper.setTo(to);
        helper.setSubject("找回密码");
        Context context = new Context();
        context.setVariable("link",DOMAIN + ROUTE + "?sid=" + sid);
        context.setVariable("name",name);
        String text = templateEngine.process("retrievePasswordEmail",context);
        helper.setText(text,true);
        mailSender.send(mailMessage);
    }
}
