package com.borneo.ecommerce;

import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.UserRepository;
import com.borneo.ecommerce.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoData(UserRepository userRepository,PasswordEncoder passwordEncoder ) {
		return args -> {
			User user = new User();
			user.setUsername("testuser");
			user.setPassword("testPass");
			user.setEmail("testgmail.com");

			user.setPassword(passwordEncoder.encode("testpass"));
			userRepository.save(user);

			userRepository.findAll().forEach(System.out::println);
		};
	}
}
