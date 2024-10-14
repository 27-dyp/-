package com.rabbiter.hospital.utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.UUID;

/**
 * @Author dongyanpeng
 * @Date 2024/10/10 9:40
 */
@Component
public class MailUtil {
    @Value("${spring.mail.username}")
    private String mail;

    @Resource
    private JavaMailSenderImpl mailSender;
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    //生成验证码
    public boolean mail(String email) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //生成随机验证码
        String code = UUID.randomUUID().toString().substring(0, 6);
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        //设置一个html邮件信息
        helper.setText(
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                        ".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                        ".header { text-align: center; margin-bottom: 20px; }" +
                        ".header h1 { color: #333333; margin: 0; }" +
                        ".content { margin-bottom: 20px; }" +
                        ".content p { color: #333333; line-height: 1.6; }" +
                        ".footer { text-align: center; color: #999999; font-size: 12px; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<div class='header'>" +
                        "<h1>FlowerPotNet 验证码</h1>" +
                        "</div>" +
                        "<div class='content'>" +
                        "<p>尊敬的王某某用户，您好！</p>" +
                        "<p>您的验证码为：<strong style='color: blue'>" + code + "</strong>，请在有效期内完成验证。</p>" +
                        "<p>验证码有效期为1分钟。</p>" +
                        "</div>" +
                        "<div class='footer'>" +
                        "<p>如果您未请求此验证码，请忽略此邮件。</p>" +
                        "<p>FlowerPotNet 团队</p>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>",
                true
        );

// 设置邮件主题名
        helper.setSubject("FlowerPotNet 验证码");
        //发给谁-》邮箱地址
        helper.setTo(email);
        //谁发的-》发送人邮箱
        helper.setFrom(mail);
        //将邮箱验证码以邮件地址为key存入redis,1分钟过期
        redisTemplate.opsForValue().set(email, code, Duration.ofMinutes(1));
        mailSender.send(mimeMessage);
        return true;
    }

    //校验验证码
    public Boolean checkCode (String email,String code) {
        String rCode= redisTemplate.opsForValue().get(email);
        if (rCode==null) {
            return false;
        }else return rCode.equals(code);
    }
}
