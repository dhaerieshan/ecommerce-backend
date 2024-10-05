package com.borneo.ecommerce;

import com.borneo.ecommerce.dto.LoginRequest;
import com.borneo.ecommerce.dto.SignupRequest;
import com.borneo.ecommerce.dto.UserUpdateRequest;
import com.borneo.ecommerce.repository.RoleRepository;
import com.borneo.ecommerce.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@ActiveProfiles("test") // Use a test profile if needed
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


	@Test
	public void updateUserPassword() throws Exception {

		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setUsername("TestUsername");
		signupRequest.setPassword("TestPassword");
		signupRequest.setEmail("testEmail@gmail.com");
		Set<String> roles = new HashSet<>();
		roles.add("USER");
		signupRequest.setRoles(roles);

		mockMvc.perform(post("/api/auth/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(signupRequest)))
				.andExpect(status().isOk());

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("TestUsername");
		loginRequest.setPassword("TestPassword");

		MvcResult result = mockMvc.perform(post("/api/auth/signin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andReturn();

		String responseContent = result.getResponse().getContentAsString();
		token = objectMapper.readTree(responseContent).get("token").asText();

		UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
		userUpdateRequest.setUsername("TestUsername");
		userUpdateRequest.setOldPassword("TestPassword");
		userUpdateRequest.setNewPassword("changedPassword");

		mockMvc.perform(get("/api/user/update")
						.header("Authorization", "BEARER " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userUpdateRequest)))
				.andExpect(status().isOk());
	}
	@Test
	public void testGetUserProfile() throws Exception {

		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setUsername("TestUsername1");
		signupRequest.setPassword("TestPassword1");
		signupRequest.setEmail("TestEmail1@gmail.com");
		Set<String> roles = new HashSet<>();
		roles.add("USER");
		signupRequest.setRoles(roles);

		mockMvc.perform(post("/api/auth/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(signupRequest)))
				.andExpect(status().isOk());

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("TestUsername1");
		loginRequest.setPassword("TestPassword1");

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
				.andExpect(jsonPath("$.username").value("TestUsername1"))
				.andExpect(jsonPath("$.email").value("TestEmail1@gmail.com"));
	}
	}