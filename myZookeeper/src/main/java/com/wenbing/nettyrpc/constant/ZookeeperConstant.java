package com.wenbing.nettyrpc.constant;

/**
 * zookeeper有关常量
 *
 * @author 沈文兵
 */
public interface ZookeeperConstant {
    int ZK_SESSION_TIMEOUT = 5000;
    String ZK_REGISTRY_PATH = "/registry";
    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
}
