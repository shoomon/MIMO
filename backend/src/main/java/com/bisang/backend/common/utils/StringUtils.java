package com.bisang.backend.common.utils;

import static lombok.AccessLevel.PRIVATE;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.slf4j.helpers.MessageFormatter;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class StringUtils {
    /**
     * 형식과 객체가 주어졌을 때 해당 형식에 맞춰 객채를 배치한다.
     * @param format
     * @param objects
     * @return
     */
    public static String format(String format, Object... objects) {
        return MessageFormatter.arrayFormat(format, objects).getMessage();
    }

    public static String randomAlphaNumeric(Integer length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public static String encodeString(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }
}
