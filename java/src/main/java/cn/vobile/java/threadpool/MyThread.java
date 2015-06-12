package cn.vobile.java.threadpool;

/**
 * Created by vobile on 5/7/15.
 */
public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "is running...");
    }
}
