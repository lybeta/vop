package cn.skyliuyang.util;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 14-3-27.
 */
public class MongoDBUtil {

    private static Mongo mongo;
    private static String host = System.getProperty("mongodb.host", "127.0.0.1");
    private static Integer port = Integer.valueOf(System.getProperty("mongodb.port", "27017"));

    private MongoDBUtil() {
    }

    private static class Holder {
        static MongoDBUtil instance = new MongoDBUtil();
    }

    public static MongoDBUtil getInstance() {
        return Holder.instance;
    }

    public void connect() {
        if (mongo == null) {
            try {
                mongo = new MongoClient(host, port);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if (mongo != null) {
            mongo.close();
        }
    }

    public void insert(String db, String collection, BasicDBObject object) {
        connect();
        mongo.getDB(db).getCollection(collection).insert(object);
    }

    public void insert(String collection, BasicDBObject object) {
        String db = System.getProperty("mongodb.db", "hop");
        insert(db, collection, object);
    }

    public void remove(String db, String collection, BasicDBObject object) {
        connect();
        mongo.getDB(db).getCollection(collection).remove(object);
    }

    public void remove(String collection, BasicDBObject object) {
        String db = System.getProperty("mongodb.db", "hop");
        remove(db, collection, object);
    }

    public void update(String db, String collection, String key, String oldValue, String newValue) {
        connect();
        BasicDBObject query = new BasicDBObject();
        query.put(key, oldValue);

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put(key, newValue);

        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", newDocument);

        mongo.getDB(db).getCollection(collection).update(query, updateObj);
    }

    public void update(String collection, String key, String oldValue, String newValue) {
        String db = System.getProperty("mongodb.db", "hop");
        update(db, collection, key, oldValue, newValue);
    }

    public List<DBObject> find(String db, String collection, BasicDBObject object) {
        connect();
        List<DBObject> list = new ArrayList<DBObject>();
        DBCursor cursor = mongo.getDB(db).getCollection(collection).find(object);
        while (cursor.hasNext()) {
            list.add(cursor.next());
            cursor.remove();
        }
        return list;
    }

    public List<DBObject> find(String collection, BasicDBObject object) {
        String db = System.getProperty("mongodb.db", "hop");
        return find(db, collection, object);
    }

    public DBObject findOne(String db, String collection, BasicDBObject object) {
        connect();
        DBObject result = mongo.getDB(db).getCollection(collection).findOne(object);
        return result;
    }

    public DBObject findOne(String collection, BasicDBObject object) {
        String db = System.getProperty("mongodb.db", "hop");
        return findOne(db, collection, object);
    }
}
