package com.efree.category.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@RefreshScope
@EnableFeignClients
@EnableSpringDataWebSupport(pageSerializationMode =
		EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@SpringBootApplication
public class CategoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CategoryApplication.class, args);
	}

}
