package com.efree.fileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@SpringBootApplication
public class FileserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileserviceApplication.class, args);
	}

}
