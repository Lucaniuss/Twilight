package me.lucanius.twilight.storage.builder;

import me.lucanius.twilight.storage.MongoServer;

/**
 * @author Lucanius - Thanks to Clouke for the idea <3
 * @since May 20, 2022
 */
public class MongoBuilder implements IMongoBuilder {

    private String host;
    private int port;
    private String database;
    private boolean auth;
    private String user;
    private String pass;
    private String authDb;

    @Override
    public MongoBuilder host(String host) {
        this.host = host;
        return this;
    }

    @Override
    public MongoBuilder port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public MongoBuilder database(String database) {
        this.database = database;
        return this;
    }

    @Override
    public MongoBuilder auth(boolean auth) {
        this.auth = auth;
        return this;
    }

    @Override
    public MongoBuilder user(String user) {
        this.user = user;
        return this;
    }

    @Override
    public MongoBuilder pass(String pass) {
        this.pass = pass;
        return this;
    }

    @Override
    public MongoBuilder authDb(String authDb) {
        this.authDb = authDb;
        return this;
    }

    @Override
    public MongoServer build() {
        return new MongoServer(host, port, database, auth, user, pass, authDb);
    }
}
