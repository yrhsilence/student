package cn.yrh.java.threadpool;

/**
 * Created by yrh on 5/7/15.
 */
public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "is running...");
    }
}
