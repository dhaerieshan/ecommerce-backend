package com.borneo.ecommerce;

import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.repository.UserRepository;
import com.borneo.ecommerce.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
class EcommerceApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@BeforeEach
	void setup(){
		userRepository.deleteAll();
		roleRepository.deleteAll();
	}

	@Test
	void addData() {
		User user = new User();
		user.setUsername("testUsername");
		user.setPassword("testPass");
		user.setEmail("test@gmail.com");
		user.setFirstname("firstname");
		user.setLastname("lastname");
		user.setDateOfBirth(LocalDate.of( 2012 , 12 , 7 ));

		userRepository.save(user);

//		User foundUser = userRepository.findById(user.getId()).orElse(null);
//		System.out.println(foundUser);
//		assertThat(foundUser).isNotNull();
//		assertThat(foundUser.getUsername()).isEqualTo("testUsername");
//		assertThat(foundUser.getPassword()).isEqualTo("testPass");
//		assertThat(foundUser.getEmail()).isEqualTo("test@gmail.com");
//		assertThat(foundUser.getFirstname()).isEqualTo("firstname");
//		assertThat(foundUser.getLastname()).isEqualTo("lastname");
//		assertThat(foundUser.getDateOfBirth()).isEqualTo(LocalDate.of( 2012 , 12 , 7 ));
//
//		userRepository.findAll().forEach(System.out::println);

	}

	@Test
	void addRole (){
		Role userRole = roleRepository.findByName("USER");
		if(userRole == null){
			userRole = new Role();
			userRole.setName("USER");
			roleRepository.save(userRole);
		}
		User user = new User();
		user.setId(100L);
		user.setUsername("testUsername");
		user.setPassword("testPass");
		user.setEmail("test@gmail.com");
		user.setFirstname("firstname");
		user.setLastname("lastname");
		user.setDateOfBirth(LocalDate.of( 2012 , 12 , 7 ));

		userRepository.save(user);
//
//		Role foundRole = roleRepository.findById(userRole.getId()).orElse(null);
//		assertThat(foundRole).isNotNull();
//		assertThat(foundRole.getName()).isEqualTo("USER");
//		user.setRole(foundRole.getName());
//		assertThat(user.getRole()).isEqualTo("USER");

	}

}
