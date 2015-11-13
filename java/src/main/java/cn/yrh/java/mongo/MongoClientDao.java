package cn.yrh.java.mongo;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MongoClientDao {
    private static final Logger log = LoggerFactory.getLogger(MongoClientDao.class);

    private MongoClient mongoclient = null;
    private volatile static MongoClientDao instance = null;

    private MongoClientDao(String uri) {
        if (mongoclient == null) {
            MongoClientOptions.Builder build = new MongoClientOptions.Builder();
            mongoclient = new MongoClient(new MongoClientURI(uri, build));
        }
    }

    private MongoClientDao(List<Address> addresses, List<Credential> credentials) {
        if (mongoclient == null) {
            List<MongoCredential> cl = new ArrayList<MongoCredential>();
            for (Credential credential: credentials) {
                MongoCredential mongoCredential = MongoCredential.createMongoCRCredential(
                                                    credential.getUserName(),
                                                    credential.getDatabase(),
                                                    credential.getPasswd()
                                                    );
                cl.add(mongoCredential);
            }

            List<ServerAddress> sl = new ArrayList<ServerAddress>();
            for (Address address: addresses) {
                ServerAddress server = new ServerAddress(address.getHost(), address.getPort());
                sl.add(server);
            }

            mongoclient = new MongoClient(sl, cl);
        }
    }

    private void close() {
        if (mongoclient != null) {
            mongoclient.close();
        }
    }

    /**
     * Init the singleton instance.
     *  * The format of the URI is:
     *   mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
     * @param uri
     */
    public static void initInstance(String uri) {
        if (instance == null) {
            synchronized (MongoClientDao.class) {
                if (instance == null) {
                    instance = new MongoClientDao(uri);
                }
            }
        }
    }

    /**
     * Init the singleton instance.
     * Support
     * @param addresses   the list of server Address
     * @param credential  the list of Credential used to authenticate all connections
     */
    public static void initInstance(List<Address> addresses, List<Credential> credential) {
        if (instance == null) {
            synchronized (MongoClientDao.class) {
                if (instance == null) {
                    instance = new MongoClientDao(addresses, credential);
                }
            }
        }
    }

    /**
     * Get the singleton instance.
     * @return
     */
    public static MongoClientDao getInstance() {
        return instance;
    }

    /**
     *
     */
    public static void destry() {
        if (instance != null) {
            synchronized (MongoClientDao.class) {
                instance.close();
                instance = null;
            }
        }
    }

    /**
     * Create index for Mongo collection.
     * @param dbName
     * @param collectionName
     * @param items             index item
     */
    public void createCollectionIndex(String dbName, String collectionName, List<String> items) {
        MongoCollection collection = mongoclient.getDatabase(dbName)
                .getCollection(collectionName);

        BasicDBObject indexs = new BasicDBObject();
        for (String item : items) {
            indexs.append(item, 1);
        }

        collection.createIndex(indexs);
    }

    public boolean test(String dbName, String collectionName) {
        try {
            MongoCollection collection = mongoclient.getDatabase(dbName)
                    .getCollection(collectionName);

            Document document = new Document().append("key", "3");
            FindIterable<Document> rets = collection.find(document);
            for (Document ret: rets) {
                System.out.println(ret.keySet());
            }

            collection.insertOne(document);

            Document update = new Document().append("$set", new Document().append("t", "a"));
            collection.updateOne(document, update);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

        return true;
    }


    /**
     * The Credential for Mongo Server.
     */
    public static class Credential {
        private String userName;
        private String database;
        private char[] passwd;

        public String getUserName() {
            return userName;
        }

        public Credential userName(String userName) {
            this.userName = userName;
            return this;
        }

        public String getDatabase() {
            return database;
        }

        public Credential database(String database) {
            this.database = database;
            return this;
        }

        public char[] getPasswd() {
            return passwd;
        }

        public Credential passwd(char[] passwd) {
            this.passwd = passwd;
            return this;
        }

        @Override
        public String toString() {
            return "Credential{" +
                    "userName='" + userName + '\'' +
                    ", database='" + database + '\'' +
                    ", passwd=" + Arrays.toString(passwd) +
                    '}';
        }
    }


    /**
     * Represents the location of a Mongo server.
     */
    public static class Address {
        private String host;
        private Integer port;

        public String getHost() {
            return host;
        }

        public Address host(String host) {
            this.host = host;
            return this;
        }

        public Integer getPort() {
            return port;
        }

        public Address port(Integer port) {
            this.port = port;
            return this;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    '}';
        }
    }

}
