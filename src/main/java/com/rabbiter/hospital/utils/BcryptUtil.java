package com.rabbiter.hospital.utils;
import org.mindrot.jbcrypt.BCrypt;
/**
 * @Author dongyanpeng
 * @Date 2024/10/9 17:07
 */
public class BcryptUtil {

    // 日志记录器
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BcryptUtil.class);

    /**
     * 生成BCrypt哈希值
     *
     * @param str 输入字符串
     * @return BCrypt哈希值
     */
    public static String getBcryptHash(String str) {
        try {
            // 生成随机盐值
            String salt = BCrypt.gensalt(12); // 12 是工作因子，可以调整
            String hash = BCrypt.hashpw(str, salt);
            return hash;
        } catch (Exception e) {
            logger.error("生成BCrypt哈希值时发生错误: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 验证密码
     *
     * @param plainText 密码明文
     * @param hashedPassword 哈希值
     * @return 验证结果
     */
    public static boolean verifyPassword(String plainText, String hashedPassword) {
        return BCrypt.checkpw(plainText, hashedPassword);
    }

    public static void main(String[] args) {
        // 单元测试
        String input = "testString";
        String bcryptHash = getBcryptHash(input);
        System.out.println("BCrypt: " + bcryptHash);

        // 验证密码
        boolean isMatch = verifyPassword(input, bcryptHash);
        System.out.println("Password match: " + isMatch);
    }
}
