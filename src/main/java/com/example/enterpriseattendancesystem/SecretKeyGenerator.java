package com.example.enterpriseattendancesystem;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.text.SimpleDateFormat;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        String secretKey = SecretKeyGenerator.generateSecretKey();
        System.out.println("Generated Secret Key: " + secretKey);
        System.out.println(stringToDate("2024-03-27"));
        System.out.println(stringToDate("2024-03-29"));
    }
    public static String generateSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[64];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public static Date stringToDate(String dateString) {
        try {
            // 将日期字符串转化为Date对象并返回
            return format.parse(dateString);
        } catch (Exception e) {
            // 如果转化失败，打印异常信息并返回null
            e.printStackTrace();
            return null;
        }
    }
}
