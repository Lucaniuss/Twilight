package me.lucanius.twilight.storage;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.lucanius.twilight.tools.Tools;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@Getter
public class MongoServer {

    private final String host;
    private final int port;
    private final String database;
    private final boolean auth;
    private final String user;
    private final String password;
    private final String authDb;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private MongoCollection<Document> profiles;
    private final List<MongoCollection<Document>> collections;

    public MongoServer(String host, int port, String database, boolean auth, String user, String password, String authDb) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.auth = auth;
        this.user = user;
        this.password = password;
        this.authDb = authDb;

        this.collections = new ArrayList<>();

        this.connect();
    }

    public void connect() {
        try {
            if (this.auth) {
                MongoCredential credential = MongoCredential.createCredential(this.user, this.authDb, this.password.toCharArray());
                this.mongoClient = new MongoClient(new ServerAddress(this.host, this.port), Collections.singletonList(credential));
            } else {
                this.mongoClient = new MongoClient(this.host, this.port);
            }
            this.mongoDatabase = this.mongoClient.getDatabase(this.database);
            this.profiles = this.mongoDatabase.getCollection("profiles");
            Tools.log("&aSuccessfully connected to Mongo.");
        } catch (final Exception e) {
            e.printStackTrace();
            Tools.log("&cFailed to connect to Mongo.");
            Bukkit.shutdown();
        }
    }

    public void dispose() {
        if (this.mongoClient != null) {
            this.mongoClient.close();
            Tools.log("&aSuccessfully disconnected from Mongo.");
        }
    }

    /**
     * Adds a collection to Mongo, will be very easy to use with the API.
     */
    public MongoCollection<Document> addCollection(String name) {
        MongoCollection<Document> collection = this.mongoDatabase.getCollection(name);
        this.collections.add(collection);
        return collection;
    }
}
