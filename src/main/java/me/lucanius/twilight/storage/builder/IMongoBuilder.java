package me.lucanius.twilight.storage.builder;

import me.lucanius.twilight.storage.MongoServer;

/**
 * @author Lucanius - Thanks to Clouke for the idea <3
 * @since May 20, 2022
 */
public interface IMongoBuilder {

    MongoBuilder host(String host);

    MongoBuilder port(int port);

    MongoBuilder database(String database);

    MongoBuilder auth(boolean auth);

    MongoBuilder user(String user);

    MongoBuilder pass(String pass);

    MongoBuilder authDb(String authDb);

    MongoServer build();

}
