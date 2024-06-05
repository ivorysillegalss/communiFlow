package org.chenzc.communi.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
//    所有的append都可以变成 common里的 StringUtils.join TODO
    public static String append(Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args)
            sb.append(String.valueOf(arg));
        return sb.toString();
    }

    /**
     * 分割
     * @param a 待分解
     * @param b 分解介质
     * @return {@link String }
     */
    public static Set<String> spilt(String a, String b){
        String[] split = a.split(b);
       return Arrays.stream(split).collect(Collectors.toSet());
    }
}
