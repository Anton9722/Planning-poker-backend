package com.PlanningPoker.PlanningPoker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class PlanningPokerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlanningPokerApplication.class, args);
	}

}
