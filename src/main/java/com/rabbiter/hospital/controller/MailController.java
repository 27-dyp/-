package com.rabbiter.hospital.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.rabbiter.hospital.utils.MailUtil;
import com.rabbiter.hospital.utils.ResponseData;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Objects;

/**
 * @Author dongyanpeng
 * @Date 2024/10/10 10:06
 */
@RestController
@RequestMapping("/sendEmail")
public class MailController {
    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping(value = "/getCode")
    public ResponseData sendCode(@RequestParam String email) throws MessagingException {
        //log.info("邮箱码：{}",email);
        //发送验证码
        mailUtil.mail(email);
        //从redis中取出验证码信息
        String code = Objects.requireNonNull(redisTemplate.opsForValue().get(email)).toString();
        if (!code.isEmpty()) {
            return ResponseData.success("验证码发送成功！");
        }
        return ResponseData.fail("邮箱不正确或为空！");
    }

    //校验验证码
    @GetMapping(value = "/checkCode")
    public ResponseData checkCode(@RequestParam String email, @RequestParam String code) {
        Boolean b = mailUtil.checkCode(email, code);
        if (b) {
            return ResponseData.success("验证码正确！");
        }return ResponseData.fail("验证码有误或验证码已过期！");
    }
}
