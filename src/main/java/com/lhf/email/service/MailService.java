package com.lhf.email.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @ClassName: MailService
 * @Description: SpringBoot发送邮件
 * @Author: liuhefei
 * @Date: 2018/11/11
 **/
@Service
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //发件人
    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送文本邮件
     * @param to  ： 收件人
     * @param subject  ：主题
     * @param content  ：内容
     */
    public void sendSimpleMail(String to, String subject, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(from);

        //发送邮件
        mailSender.send(message);
    }

    /**
     * 发送Html邮件
     * @param to : 收件人
     * @param subject  ： 主题
     * @param content ： 内容
     */
    public void sendHtmlMail(String to, String subject, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        logger.info("发送html邮件开始：{},{},{},{}",to, subject, content, from);
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom(from);

            mailSender.send(mimeMessage);
            logger.info("发送html邮件成功");
        } catch (MessagingException e) {
            logger.error("发送html失败",e);
        }

    }

    /**
     * 附件邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param contnet HTML内容
     * @param filePath 附件路径
     * @throws MessagingException
     */
    public void sendAttachmentsMail(String to, String subject, String contnet,
                                    String filePath)  {
        MimeMessage message = mailSender.createMimeMessage();

        logger.info("发送附件邮件：{},{},{},{}", to, subject, contnet, filePath);
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(contnet, true);
            helper.setFrom(from);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = file.getFilename();
            helper.addAttachment(fileName, file);

            mailSender.send(message);
            logger.info("发送附件邮件成功");
        } catch (MessagingException e) {
            logger.error("发送附件邮件失败：", e);
        }


    }

    /**
     * 图片邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param contnet HTML内容
     * @param recPath 图片路径
     * @param rscId 图片ID
     * @throws MessagingException
     */
    public void sendInlinkResourceMail(String to, String subject, String contnet,
                                       String recPath, String rscId)  {
        MimeMessage message = mailSender.createMimeMessage();

        logger.info("发送图片邮件：{},{},{},{},{}",to,subject,contnet,recPath,rscId);
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(contnet, true);
            helper.setFrom(from);

            FileSystemResource res = new FileSystemResource(new File(recPath));
            helper.addInline(rscId, res);

            mailSender.send(message);
            logger.info("发送图片邮件成功");
        } catch (MessagingException e) {
            logger.error("发送图片邮件失败：", e);
        }

    }
}
