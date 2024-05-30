package org.chenzc.communi.utils;

/**
 * 封装常用字符串拼接操作
 *
 * @author chenz
 * @date 2024/05/29
 */
public class StringUtils {

    /**
     * 添加
     *
     * @param args
     * @return {@link String }
     */
    public static String append(Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args)
            sb.append(String.valueOf(arg));
        return sb.toString();
    }
}
