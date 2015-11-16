package cn.yrh.java.zoo;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

/**
 * Created with IntelliJ IDEA.
 * User: yrh
 * Date: 11/20/14
 * Time: 10:43
 * To change this template use File | Settings | File Templates. t
 */

public class OperZoo implements Watcher {
    private static final int SESSION_ITMEOUT = 5000;

    private ZooKeeper zk;
    private CountDownLatch connectedSignal = new CountDownLatch(1);
    private static final Charset CHARSET = Charset.forName("UTF-8");

    public void connect(String hosts) throws InterruptedException, IOException {
        System.out.println("connect");
        zk = new ZooKeeper(hosts, SESSION_ITMEOUT, this);
        connectedSignal.await();
        System.out.println("connect over");
    }

    private String genPath(String base, String ...names) {
        String path = "/" + base;
        for (String name: names) {
            path += "/" + name;
        }

        return path;
    }

    public void process(WatchedEvent event) {
        //Will be run after connect,
        if(event.getState() == Event.KeeperState.SyncConnected) {
            connectedSignal.countDown();
        }
    }

    public void create(String groupName) throws KeeperException, InterruptedException {
        System.out.println("create");
        String path = "/" + groupName;
        String createPath = zk.create(path, null/*data*/, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println("Create " + createPath);
    }

    public void join(String groupName, String memberName) throws KeeperException, InterruptedException {
        String path =  "/" + groupName + "/" + memberName;
        String createPath = zk.create(path, null/*data*/, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Create " + createPath);
    }

    public void list(String groupName) throws KeeperException, InterruptedException {
        String path = "/" + groupName;
        try {
            List<String> children = zk.getChildren(path, false);
            if (children.isEmpty()) {
                System.out.printf("No members in groups %s\n", groupName);
                System.exit(1);
            }

            for (String child: children) {
                System.out.println(child);
            }

        }  catch (KeeperException.NoNodeException e) {
            System.out.printf("Group %s does not exist!\n", groupName);
            System.exit(1);
        }
    }

    public void delete(String GroupName) throws KeeperException, InterruptedException {
        String path = genPath(GroupName);
        List<String> children = zk.getChildren(path, false);

        try {
            for (String child: children) {
                zk.delete(path + "/" + child, -1);
            }
            zk.delete(path, -1);
        } catch (KeeperException.NoNodeException e) {
            System.out.printf("Groups %s does not exist!\n", GroupName);
            System.exit(1);
        }
    }

    public void write(String path, String value) throws InterruptedException, KeeperException {
        Stat stat = zk.exists(path, false);
        if (stat == null) {
            zk.create(path, value.getBytes(CHARSET), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } else {
            zk.setData(path, value.getBytes(CHARSET), -1);
        }
    }

    public String read(String path, Watcher watcher) throws InterruptedException, KeeperException {
        byte[] data = zk.getData(path, watcher, null/*stat*/);
        return new String(data, CHARSET);
    }

    public void close() throws InterruptedException {
        zk.close();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Start!");
        OperZoo operZoo = new OperZoo();
        operZoo.connect("192.168.1.60:2181");

        operZoo.create("monitor");
        operZoo.join("monitor", "app1");
        operZoo.list("monitor");
        operZoo.delete("monitor");
        sleep(10000);
        operZoo.close();
        System.out.println("End!");
    }
}
