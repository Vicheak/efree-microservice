package com.efree.category.api.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Configuration
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@EnableMongoRepositories(basePackages = "com.efree.category.api.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

    private String host;
    private Integer port;
    private String database;
    private String connectionString;

    @Override
    protected String getDatabaseName() {
        return Objects.nonNull(this.database) ? this.database : "efree_db";
    }

    @Override
    public MongoClient mongoClient() {
        this.host = Objects.nonNull(this.host) ? this.host : "localhost";
        this.port = Objects.nonNull(this.port) ? this.port : 27017;
        //build connection string
        this.connectionString = "mongodb://%s:%d/%s".formatted(
                this.host, this.port, this.getDatabaseName());

        //log.error("ConnectionString : {}", this.connectionString);

        ConnectionString connectionString = new ConnectionString(this.connectionString);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public Collection getMappingBasePackages() {
        return Collections.singleton("com.efree.category.api");
    }

}
