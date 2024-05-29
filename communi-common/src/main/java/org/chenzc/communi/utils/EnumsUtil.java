package org.chenzc.communi.utils;

import org.chenzc.communi.enums.PowerfulEnums;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author chenzc
 * 枚举工具类（获取枚举的描述、获取枚举的code、获取枚举的code列表）
 * @date 2024/05/29
 */
public class EnumsUtil {

    private EnumsUtil() {
    }

    public static <T extends PowerfulEnums> String getDescriptionByCode(Integer code, Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst().map(PowerfulEnums::getMessage).orElse("");
    }

    public static <T extends PowerfulEnums> T getEnumByCode(Integer code, Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst().orElse(null);
    }

    public static <T extends PowerfulEnums> List<String> getCodeList(Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(PowerfulEnums::getCode)
                .collect(Collectors.toList());
    }
}
