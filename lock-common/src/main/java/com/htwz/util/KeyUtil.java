package com.htwz.util;

/**
 * redis key 工具类
 *
 * @author lxs
 */
public class KeyUtil {

    /**
     * 生成分布式锁key
     *
     * @param prefix
     * @param name
     * @param key
     * @return
     */
    public static String getCompositeKey(String prefix, String name, String key) {
        return prefix + "_" + name + "_" + key;
    }

    public static String getCompositeWildcardKey(String prefix, String name) {
        return prefix + "_" + name + "*";
    }

    public static String getCompositeWildcardKey(String key) {
        return key + "*";
    }
}