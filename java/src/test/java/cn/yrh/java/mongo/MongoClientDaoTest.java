package cn.yrh.java.mongo;

/**
 * Created by silence on 15/11/13.
 */
public class MongoClientDaoTest {
    public static void main(String[] args) {
        String uri = "mongodb://bao:123@127.0.0.1:27017/?authSource=admin";
        MongoClientDao.initInstance(uri);
        MongoClientDao mongoClient = MongoClientDao.getInstance();
        MongoClientDao.destry();
    }

}