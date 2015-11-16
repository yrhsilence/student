package cn.yrh.java.zoo;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.KeeperException;

/**
 * Created with IntelliJ IDEA.
 * User: yrh
 * Date: 11/24/14
 * Time: 6:03
 * To change this template use File | Settings | File Templates.
 */
public class ConfigUpdater {
    public static final String PATH = "/config";

    private OperZoo oper;
    private Random random = new Random();

    public ConfigUpdater(String hosts) throws IOException, InterruptedException {
        oper = new OperZoo();
        oper.connect(hosts);
    }

    public void run() throws InterruptedException, KeeperException {
         while (true) {
             String value = random.nextInt(100) + "";
             oper.write(PATH, value);
             System.out.printf("Set %s to %s\n", PATH, value);
             TimeUnit.SECONDS.sleep(random.nextInt(10));
         }
    }

    public static void main(String[] args) throws Exception {
        ConfigUpdater configUpdater = new ConfigUpdater("192.168.1.60:2181,192.168.1.50:2181");
        configUpdater.run();
    }
}
