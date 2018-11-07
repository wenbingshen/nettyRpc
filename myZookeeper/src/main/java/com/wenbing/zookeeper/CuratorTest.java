package com.wenbing.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;

import java.util.List;

//Apache Curator封装的zookeeperk客户端使用
public class CuratorTest {

//    psvm快捷键main方法生成
    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.159.128:2181", new RetryNTimes(10, 5000));
//        连接
        client.start();
//        获取子节点，顺便监控子节点
        List<String> children = client.getChildren().usingWatcher(new CuratorWatcher() {
            @Override
            public void process(WatchedEvent watchedEvent) throws Exception {
                System.out.println("监控:"+watchedEvent);
            }
        }).forPath("/");
        System.out.println(children);
//        创建节点
        String result = client.create().withMode(CreateMode.PERSISTENT).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath("/test", "Data".getBytes());
        System.out.println(result);
//        设置节点数据
        client.setData().forPath("/test", "111".getBytes());
        client.setData().forPath("/test", "222".getBytes());
//        删除节点
        System.out.println(client.checkExists().forPath("/test"));
//        client.delete().withVersion(-1).forPath("/test");
        System.out.println(client.checkExists().forPath("/test"));
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
            }
        });
        thread.sleep(Long.MAX_VALUE);
        client.close();
        System.out.println("OK!");
    }

}
