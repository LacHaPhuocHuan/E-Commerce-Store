package com.huancoder.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ECommerceStoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(ECommerceStoreApplication.class, args);

	}
}
