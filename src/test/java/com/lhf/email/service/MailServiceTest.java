package com.lhf.email.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

import javax.annotation.Resource;
import javax.mail.MessagingException;


/**
 * @ClassName: MailServiceTest
 * @Description:
 * @Author: liuhefei
 * @Date: 2018/11/11
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Resource
    MailService mailService;

    @Resource
    private TemplateEngine templateEngine;

    @Test
    public void sendSimpleMailTest(){
        mailService.sendSimpleMail("xxxx@163.com","文本邮件","没有了你，万杯觥筹只不过是提醒寂寞");
        System.out.println("邮件发送成功");
    }

    @Test
    public void sendHtmlMailTest() throws MessagingException {
        String content = "<html>\n"
                +"<body>\n"
                +"<h2>html邮件</h2>"
                +"</body>\n"
                +"</html>";
        mailService.sendHtmlMail("xxxx@163.com","html邮件",content);
        System.out.println("html邮件发送成功");
    }

    @Test
    public void sendAttachmentsMail() throws MessagingException {
        String filePath = "image/1_x.jpg";  //附件的地址
        String content = "<html>\n" +
                "<body>\n" +
                "<h3>hello world</h3>\n" +
                "<h1>html</h1>\n" +
                "<h1>附件传输</h1>\n" +
                "<body>\n" +
                "</html>\n";
        mailService.sendAttachmentsMail("xxxx@163.com","这是一封HTML邮件",content, filePath);
    }

    @Test
    public void sendInlinkResourceMail() throws MessagingException {
        String imgPath = "image/2_x.jpg";
        String rscId = "mv";
        String content = "<html>\n" +
                "<body>\n" +
                "<h3>hello world</h3>\n" +
                "<h1>html</h1>\n" +
                "<h1>图片邮件</h1>\n" +
                "<img src='cid:"+rscId+"'></img>" +
                "<body>\n" +
                "</html>\n";

        mailService.sendInlinkResourceMail("xxxx@163.com","这是一封图片邮件",content, imgPath, rscId);
    }

    @Test
    public void testTemplateMailTest() throws MessagingException {
        Context context = new Context();
        context.setVariable("id","110");

        String emailContent = templateEngine.process("emailTeplate", context);
        mailService.sendHtmlMail("xxxx@163.com","这是一封HTML邮件",emailContent);

    }

}
