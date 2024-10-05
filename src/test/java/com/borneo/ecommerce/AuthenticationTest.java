package com.borneo.ecommerce;

import com.borneo.ecommerce.dto.LoginRequest;
import com.borneo.ecommerce.dto.SignupRequest;
import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.RoleRepository;
import com.borneo.ecommerce.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.mock.mockito.MockBean; // Remove this if not mocking
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Use a test profile if needed
public class AuthenticationTest {

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
		// Clean up repositories
		userRepository.deleteAll();
		roleRepository.deleteAll();

		// Create roles
		Role userRole = new Role();
		userRole.setName("USER");
		roleRepository.save(userRole);

		// Create a test user
		User user = new User();
		user.setUsername("Dhaerieshan");
		user.setPassword(passwordEncoder.encode("securepassword123"));
		user.setEmail("dhaerieshan@example.com");
		Set<Role> roles = new HashSet<>();
		roles.add(userRole);
		user.setRoles(roles);
		userRepository.save(user);

		// Authenticate and get token
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("Dhaerieshan");
		loginRequest.setPassword("securepassword123");

		MvcResult result = mockMvc.perform(post("/api/auth/signin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andReturn();

		String responseContent = result.getResponse().getContentAsString();
		token = objectMapper.readTree(responseContent).get("token").asText();
	}

//	@Test
//	public void testSignInWithValidCredentials() throws Exception {
//		LoginRequest loginRequest = new LoginRequest();
//		loginRequest.setUsername("Dhaerieshan_u");
//		loginRequest.setPassword("securepassword123");
//
//		mockMvc.perform(post("/api/auth/signin")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(objectMapper.writeValueAsString(loginRequest)))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.token").exists())
//				.andExpect(jsonPath("$.username").value("Dhaerieshan_u"));
//	}

	@Test
	public void testGetUserProfile() throws Exception {
		mockMvc.perform(get("/api/user/profile")
						.header("Authorization", "BEARER " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("Dhaerieshan"))
				.andExpect(jsonPath("$.email").value("dhaerieshan@example.com"));
	}
}