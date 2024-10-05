package com.borneo.ecommerce;

import com.borneo.ecommerce.dto.LoginRequest;
import com.borneo.ecommerce.dto.SignupRequest;
import com.borneo.ecommerce.dto.UserUpdateRequest;
import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.RoleRepository;
import com.borneo.ecommerce.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EcommerceApplicationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private ObjectMapper objectMapper = new ObjectMapper();

	private String token;

	@BeforeEach
	public void setup() throws Exception {

//		userRepository.deleteAll();
//		roleRepository.deleteAll();

		Role userRole = new Role();
		userRole.setName("USER");
		roleRepository.save(userRole);

		Role adminRole = new Role();
		adminRole.setName("ADMIN");
		roleRepository.save(adminRole);

		User adminUser = new User();
		adminUser.setUsername("Admin");
		adminUser.setPassword(passwordEncoder.encode("admin"));
		adminUser.setEmail("admin@gmail.com");
		Set<Role> adminRoles = new HashSet<>();
		adminRoles.add(adminRole);
		adminUser.setRoles(adminRoles);
		userRepository.save(adminUser);

		User user = new User();
		user.setUsername("User");
		user.setPassword(passwordEncoder.encode("user"));
		user.setEmail("user@gmail.com");
		Set<Role> userRoles = new HashSet<>();
		userRoles.add(userRole);
		user.setRoles(userRoles);
		userRepository.save(user);

	}
	@Test
	public void updateUserPassword() throws Exception {

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("User");
		loginRequest.setPassword("user");

		MvcResult result = mockMvc.perform(post("/api/auth/signin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andReturn();

		String responseContent = result.getResponse().getContentAsString();
		token = objectMapper.readTree(responseContent).get("token").asText();

		UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
		userUpdateRequest.setUsername("User");
		userUpdateRequest.setOldPassword("user");
		userUpdateRequest.setNewPassword("newpass");

		mockMvc.perform(get("/api/user/update")
						.header("Authorization", "BEARER " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userUpdateRequest)))
				.andExpect(status().isOk());
	}
	@Test
	public void testGetUserProfile() throws Exception {


		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("User");
		loginRequest.setPassword("user");

		MvcResult result = mockMvc.perform(post("/api/auth/signin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andReturn();

		String responseContent = result.getResponse().getContentAsString();
		token = objectMapper.readTree(responseContent).get("token").asText();

		mockMvc.perform(get("/api/user/profile")
						.header("Authorization", "BEARER " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("User"))
				.andExpect(jsonPath("$.email").value("user@gmail.com"));
	}
	@Test
	public void testAdminGetUsers() throws Exception{

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("Admin");
		loginRequest.setPassword("admin");

		MvcResult result = mockMvc.perform(post("/api/auth/signin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andReturn();

		String responseContent = result.getResponse().getContentAsString();
		token = objectMapper.readTree(responseContent).get("token").asText();

		mockMvc.perform(get("/api/admin/userlist")
						.header("Authorization", "BEARER " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.users").isArray())
				.andReturn();

	}
}