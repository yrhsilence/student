package cn.vobile.java.python;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by vobile on 5/12/15.
 */
public class PythonTest {
    public static void main(String[] args) {
        System.out.println("start");
        try {
            Process pr = Runtime.getRuntime().exec("python /tmp/test.py");

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
            System.out.println("end");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
