package com.borneo.ecommerce;

import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.RoleRepository;
import com.borneo.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoData(UserRepository userRepository, RoleRepository roleRepository) {
		return args -> {
			User user = new User();
			user.setId(101L);
			user.setUsername("testUsername");
			user.setPassword("testPass");
			user.setEmail("test@gmail.com");
			user.setFirstname("firstname");
			user.setLastname("lastname");
			user.setDateOfBirth(LocalDate.of( 2012 , 12 , 7 ));

			userRepository.save(user);


			User user1 = new User();
			user1.setId(201L);
			user1.setUsername("testUsername1");
			user1.setPassword("testPass1");
			user1.setEmail("test1@gmail.com");
			user1.setFirstname("firstname1");
			user1.setLastname("lastname1");
			user1.setDateOfBirth(LocalDate.of( 2012 , 12 , 7 ));

			User user2 = new User();
			user2.setId(102L);
			user2.setUsername("testUsername2");
			user2.setPassword("testPass2");
			user2.setEmail("test2@gmail.com");
			user2.setFirstname("firstname2");
			user2.setLastname("lastname2");
			user2.setDateOfBirth(LocalDate.of( 2012 , 12 , 7 ));

			User user3 = new User();
			user3.setId(202L);
			user3.setUsername("testUsername3");
			user3.setPassword("testPass3");
			user3.setEmail("test3@gmail.com");
			user3.setFirstname("firstname3");
			user3.setLastname("lastname3");
			user3.setDateOfBirth(LocalDate.of( 2012 , 12 , 7 ));

			Role role1 = new Role();
			role1.setId(100L);
			role1.setName("USER");

			Role role2 = new Role();
			role2.setId(200L);
			role2.setName("ADMIN");

			userRepository.save(user1);
			userRepository.save(user2);
			userRepository.save(user3);

			roleRepository.save(role1);
			roleRepository.save(role2);


		};
	}
}
