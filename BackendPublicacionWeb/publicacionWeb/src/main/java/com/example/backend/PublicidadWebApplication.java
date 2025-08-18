package com.example.backend;

import com.example.backend.users.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PublicidadWebApplication {

	@Autowired
	private UserService userService;
	public static void main(String[] args) {
		SpringApplication.run(PublicidadWebApplication.class, args);
	}

	@PostConstruct
	public void init() {
		userService.insertAdminUser();
	}

    @PostConstruct
    public void checkEnv() {
        System.out.println("JWT_SECRET = " + System.getenv("JWT_SECRET"));
    }

}
