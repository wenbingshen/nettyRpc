package com.wenbing.nettyrpc.registry;

import com.wenbing.nettyrpc.constant.ZookeeperConstant;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 服务注册
 *
 * @author 沈文兵
 */

public class ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    public ServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void register(String data) {
        if (data != null) {
            ZooKeeper zk = connectServer();
            if (zk != null) {
                AddRootNode(zk);//如果不存在node，先创建
                createNode(zk, data);
            }
            System.out.println("服务注册了......");
        }
    }

    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, ZookeeperConstant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }

                }
            });
            latch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return zk;
    }

    private void AddRootNode(ZooKeeper zk) {
        try {
            Stat s = zk.exists(ZookeeperConstant.ZK_REGISTRY_PATH, false);
            if (s == null) {
                zk.create(ZookeeperConstant.ZK_REGISTRY_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (InterruptedException e) {
            logger.error(e.toString());
        } catch (KeeperException ex) {
            logger.error(ex.toString());
        }
    }

    private void createNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();
            String path = zk.create(ZookeeperConstant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.debug("创建临时有序节点:",path,data);
            System.out.println("临时节点创建成功......");
        } catch (InterruptedException e) {
            logger.error(e.toString());
        } catch (KeeperException ex) {
            logger.error(ex.toString());
        }
    }
}
