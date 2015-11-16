package cn.yrh.java.zoo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: yrh
 * Date: 11/24/14
 * Time: 6:15
 * To change this template use File | Settings | File Templates.
 */
public class ConfigWatcher implements Watcher {
    private OperZoo oper;

    public ConfigWatcher(String hosts) throws IOException, InterruptedException {
        oper = new OperZoo();
        oper.connect(hosts);
    }

    public void disPlayConfig() throws InterruptedException, KeeperException {
        String value = oper.read(ConfigUpdater.PATH, this);
        System.out.printf("Read %s as %s\n", ConfigUpdater.PATH, value);
    }

    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDataChanged) {
            try {
                disPlayConfig();
            } catch (InterruptedException e) {
                System.err.println("Interrupted. Exiting.");
                Thread.currentThread().interrupt();
            } catch (KeeperException e) {
                System.err.printf("KeeperException: %s. Existing.\n", e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ConfigWatcher configWatcher = new ConfigWatcher("192.168.1.60:2181,192.168.1.50:2181");
        configWatcher.disPlayConfig();

        Thread.sleep(Long.MAX_VALUE);
    }
}
