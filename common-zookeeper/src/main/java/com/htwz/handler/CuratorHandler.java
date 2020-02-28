package com.htwz.handler;



import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface CuratorHandler {
    /**
     * 启动ZooKeeper客户端
     */
    void start();

    /**
     * 启动ZooKeeper客户端，直到第一次连接成功
     *
     * @throws Exception
     */
    void startAndBlock() throws Exception;

    /**
     * 启动ZooKeeper客户端，直到第一次连接成功，为每一次连接配置超时
     */
    void startAndBlock(int maxWaitTime, TimeUnit units) throws Exception;

    /**
     * 关闭ZooKeeper客户端连接
     */
    void close();

    /**
     * 获取ZooKeeper客户端是否初始化
     */
    boolean isInitialized();

    /**
     * 获取ZooKeeper客户端连接是否正常
     */
    boolean isStarted();

    /**
     * 检查ZooKeeper是否是启动状态
     */
    void validateStartedStatus();

    /**
     * 检查ZooKeeper是否是关闭状态
     */
    void validateClosedStatus();

    /**
     * 获取ZooKeeper客户端
     */
    CuratorFramework getCurator();

    /**
     * 判断路径是否存在
     */
    boolean pathExist(String path) throws Exception;

    /**
     * 判断stat是否存在
     */
    Stat getPathStat(String path) throws Exception;

    /**
     * 创建路径
     */
    void createPath(String path) throws Exception;

    /**
     * 创建路径，并写入数据
     */
    void createPath(String path, byte[] data) throws Exception;

    /**
     * 创建路径
     */
    void createPath(String path, CreateMode mode) throws Exception;

    /**
     * 创建路径，并写入数据
     */
    void createPath(String path, byte[] data, CreateMode mode) throws Exception;

    /**
     * 删除路径
     */
    void deletePath(String path) throws Exception;

    /**
     * 获取子节点名称列表
     */
    List<String> getChildNameList(String path) throws Exception;

    /**
     * 获取子节点路径列表
     */
    List<String> getChildPathList(String path) throws Exception;

    /**
     * 组装根节点路径
     */
    String getRootPath(String prefix);

    /**
     * 组装节点路径
     */
    String getPath(String prefix, String key);
}