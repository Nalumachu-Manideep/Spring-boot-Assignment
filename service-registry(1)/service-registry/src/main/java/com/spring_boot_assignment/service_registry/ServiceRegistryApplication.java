package com.spring_boot_assignment.service_registry;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {
	public static void main(String[] args) {
		Dotenv dotenv=Dotenv.load();
		System.setProperty("MYSQL_USERNAME",dotenv.get("MYSQL_USERNAME"));
		System.setProperty("MYSQL_PASSWORD",dotenv.get("MYSQL_PASSWORD"));
		SpringApplication.run(ServiceRegistryApplication.class, args);
	}
}
