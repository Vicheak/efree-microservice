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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    private String username;
    private String password;
    private String authSource;
    private String connectionString;

    @Override
    protected String getDatabaseName() {
        return Objects.nonNull(this.database) ? this.database : "efree_mongodb";
    }

    @Override
    public MongoClient mongoClient() {
        this.host = Objects.nonNull(this.host) ? this.host : "34.87.163.126";
        this.port = Objects.nonNull(this.port) ? this.port : 5201;
        this.username = Objects.nonNull(this.username) ? this.username : "efree";
        this.password = Objects.nonNull(this.password) ? this.password : "efree@123";
        this.authSource = Objects.nonNull(this.authSource) ? this.authSource : "admin";

        //URL encode username and password
        try {
            String encodedUsername = URLEncoder.encode(this.username, StandardCharsets.UTF_8.toString());
            String encodedPassword = URLEncoder.encode(this.password, StandardCharsets.UTF_8.toString());

            //build connection string with encoded username and password
            this.connectionString = "mongodb://%s:%s@%s:%d/%s?authSource=%s".formatted(
                    encodedUsername, encodedPassword, this.host, this.port, this.getDatabaseName(), this.authSource);
        } catch (UnsupportedEncodingException e) {
            log.error("Failed to encode username or password", e);
            throw new RuntimeException("Failed to encode username or password", e);
        }

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
