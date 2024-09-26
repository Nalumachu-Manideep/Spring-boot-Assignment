package com.auth_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv=Dotenv.load();
		System.setProperty("MYSQL_USERNAME",dotenv.get("MYSQL_USERNAME"));
		System.setProperty("MYSQL_PASSWORD",dotenv.get("MYSQL_PASSWORD"));
		System.setProperty("JWT_SECRET",dotenv.get("JWT_SECRET"));
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
