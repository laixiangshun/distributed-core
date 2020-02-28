package com.htwz.proxy.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 自定义代理工具类
 *
 * @author lxs
 */
public class ProxyUtil {
    /**
     * 转换Class数组成字符串格式
     */
    public static String toString(Class<?>[] parameterTypes) {
        if (ArrayUtils.isEmpty(parameterTypes)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Class<?> clazz : parameterTypes) {
            builder.append(",").append(clazz.getCanonicalName());
        }

        String parameter = builder.toString().trim();
        if (parameter.length() > 0) {
            return parameter.substring(1);
        }
        return "";
    }

    /**
     * 转换String数组成字符串格式
     */
    public static String toString(String[] values) {
        if (ArrayUtils.isEmpty(values)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (String value : values) {
            builder.append(",").append(value);
        }

        String parameter = builder.toString().trim();
        if (parameter.length() > 0) {
            return parameter.substring(1);
        }
        return "";
    }
}