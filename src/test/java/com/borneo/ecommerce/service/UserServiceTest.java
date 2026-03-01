package com.borneo.ecommerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("UserService Tests")
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserService userService;

  private User testUser;
  private Role userRole;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    userRole = new Role();
    userRole.setId(1L);
    userRole.setName("USER");

    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testuser");
    testUser.setEmail("testuser@example.com");
    testUser.setPassword("hashedpassword");
    testUser.setFirstName("Test");
    testUser.setLastName("User");
    testUser.setAddress("123 Street");
    testUser.setRoles(Set.of(userRole));
  }

  @Test
  @DisplayName("Should get user by username successfully")
  void testFindByUsername() {
    when(userRepository.findByUsername("testuser")).thenReturn(testUser);

    User result = userService.findByUsername("testuser");

    assertNotNull(result);
    assertEquals("testuser", result.getUsername());
    verify(userRepository, times(1)).findByUsername("testuser");
  }

  @Test
  @DisplayName("Should return null when user not found by username")
  void testFindByUsernameNotFound() {
    when(userRepository.findByUsername("nonexistent")).thenReturn(null);

    User result = userService.findByUsername("nonexistent");

    assertNull(result);
    verify(userRepository, times(1)).findByUsername("nonexistent");
  }

  @Test
  @DisplayName("Should get user by ID successfully")
  void testFindById() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

    Optional<User> result = userService.findById(1L);

    assertTrue(result.isPresent());
    assertEquals("testuser", result.get().getUsername());
    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Should return empty Optional when user not found by ID")
  void testFindByIdNotFound() {
    when(userRepository.findById(999L)).thenReturn(Optional.empty());

    Optional<User> result = userService.findById(999L);

    assertFalse(result.isPresent());
    verify(userRepository, times(1)).findById(999L);
  }

  @Test
  @DisplayName("Should get all users")
  void testFindAll() {
    List<User> users = new ArrayList<>();
    users.add(testUser);
    when(userRepository.findAll()).thenReturn(users);

    List<User> result = userService.findAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("testuser", result.get(0).getUsername());
    verify(userRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Should return empty list when no users exist")
  void testFindAllEmpty() {
    when(userRepository.findAll()).thenReturn(new ArrayList<>());

    List<User> result = userService.findAll();

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Should delete user by ID")
  void testDeleteById() {
    doNothing().when(userRepository).deleteById(1L);

    userService.deleteById(1L);

    verify(userRepository, times(1)).deleteById(1L);
  }

  @Test
  @DisplayName("Should find users by role name")
  void testFindByRoleName() {
    List<User> users = List.of(testUser);
    when(userRepository.findByRoleName("USER")).thenReturn(users);

    List<User> result = userService.findByRoleName("USER");

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(userRepository, times(1)).findByRoleName("USER");
  }
}
