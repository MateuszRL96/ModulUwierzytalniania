package com.example.autorizate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
@SpringBootApplication
public class AutorizateApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutorizateApplication.class, args);
		System.err.println("hello");
	}

}
