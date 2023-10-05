package org.acme.storage;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

@MongoEntity(collection = "User", clientName = "acme")
public class UserEntity extends PanacheMongoEntity{
    @BsonProperty("username")
    public String username;

    public UserEntity(){
    }

    public UserEntity(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}