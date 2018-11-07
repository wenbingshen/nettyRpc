package com.wenbing.zookeeper;

import org.apache.zookeeper.*;

//原生zookeeper客户端使用
public class zookeeperSelfTest {

    private static final String connectString = "192.168.159.128:2181,192.168.159.133:2181,192.168.159.134:2181";
    private static final int sessionTimeout = 3000;


    public static void main(String[] args) throws Exception {
//        创建一个与服务器的连接，需要(服务器的ip+端口)(session过期时间)(Watcher监听注册)
        ZooKeeper zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            //                监听所有被触发的事件
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("监听来了:" + watchedEvent.toString());
            }
        });
        System.out.println("OK!");
//        创建一个目录节点
        /**
         * CreateMode:
         *      PERSISTENT(持续的，相对于EPHEMERAL，不会随着client的断开而消失
         *      PERSISTENT_SEQUENTIAL（持久的且带顺序的)
         *      EPHEMERAL (短暂的，生命周期依赖于client session)
         *      EPHEMERAL_SEQUENTIAL  (短暂的，带顺序的)
         */
        if (zk.exists("/test01", false) == null) {
            zk.create("/test01", "goodboy".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
//        创建一个子目录节点
        zk.create("/test01/test01", "goodgirl".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(zk.getData("/test01", false,null).toString());
//        取出子目录节点列表
        System.out.println(zk.getChildren("/test01", true));
//        创建另一个子目录节点
        zk.create("/test01/test02", "goodgirl2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(zk.getChildren("/test01", true));
//      修改子目录节点数据
        zk.setData("/test01/test01", "goodboy/boy02".getBytes(), -1);
        byte[] datas = zk.getData("/test01/test01", false, null);
        String str = new String(datas, "UTF-8");
        System.out.println(str);

//        删除整个子目录 -1代表version版本号,-1是删除所有版本
        zk.delete("/test01/test01", -1);
        zk.delete("/test01/test02", -1);
        zk.delete("/test01", -1);
        System.out.println(str);
        Thread.sleep(15000);
        zk.close();
        System.out.println("OK!结束！");
    }

}
