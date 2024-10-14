package com.rabbiter.hospital.utils;

import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashUtil {

    //盐，用于混交md5
    private static final String SALT = "asdwqAsd12_qS";

//    public static String getMD5(String str) {
//        String base = str + "/" + salt;
//        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
//        return md5;
//    }

    // 日志记录器
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(HashUtil.class);

    /**
     * 生成带有盐值的SHA-256哈希值
     *
     * @param str 输入字符串
     * @return 带有盐值的SHA-256哈希值
     */
    public static String getSHA256(String str) {
        try {
            String base = str + "/" + SALT;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            logger.error("生成SHA-256哈希值时发生错误: {}", e.getMessage(), e);
            return null;
        }
    }

    public static void main(String[] args) {
        // 单元测试
        String input = "testString";
        String sha256 = getSHA256(input);
        System.out.println("SHA-256: " + sha256);
    }

}
