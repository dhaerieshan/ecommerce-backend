package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager entityManager;

  private Role adminRole;
  private Role userRole;

  @BeforeEach
  void setup() {
    adminRole = new Role();
    adminRole.setName("ADMIN");

    userRole = new Role();
    userRole.setName("USER");

    entityManager.persist(adminRole);
    entityManager.persist(userRole);
  }

  @Test
  void shouldReturnTrueIfUsernameExists() {
    User user = new User();
    user.setUsername("john");
    user.setEmail("john@test.com");
    user.setPassword("password");
    user.setAddress("address");
    user.setRoles(Set.of(userRole));

    entityManager.persist(user);
    entityManager.flush();

    boolean exists = userRepository.existsByUsername("john");

    assertThat(exists).isTrue();
  }

  @Test
  void shouldReturnTrueIfEmailExists() {
    User user = new User();
    user.setUsername("mary");
    user.setEmail("mary@test.com");
    user.setPassword("password");
    user.setAddress("address");
    user.setRoles(Set.of(userRole));

    entityManager.persist(user);
    entityManager.flush();

    boolean exists = userRepository.existsByEmail("mary@test.com");

    assertThat(exists).isTrue();
  }

  @Test
  void shouldFindUserWithRolesByUsername() {
    User user = new User();
    user.setUsername("admin");
    user.setEmail("admin@test.com");
    user.setPassword("password");
    user.setAddress("address");
    user.setRoles(Set.of(adminRole));

    entityManager.persist(user);
    entityManager.flush();

    User found = userRepository.findByUsername("admin");

    assertThat(found).isNotNull();
    assertThat(found.getRoles()).hasSize(1);
    assertThat(found.getRoles().iterator().next().getName()).isEqualTo("ADMIN");
  }

  @Test
  void shouldFindUsersByRoleName() {
    User user1 = new User();
    user1.setUsername("user1");
    user1.setEmail("user1@test.com");
    user1.setPassword("password");
    user1.setAddress("address");
    user1.setRoles(Set.of(adminRole));

    entityManager.persist(user1);
    entityManager.flush();

    List<User> admins = userRepository.findByRoleName("ADMIN");

    assertThat(admins).hasSize(1);
    assertThat(admins.get(0).getUsername()).isEqualTo("user1");
  }

  @Test
  void shouldCheckExistsByEmailAndUsernameNot() {
    User user = new User();
    user.setUsername("john");
    user.setEmail("john@test.com");
    user.setPassword("password");
    user.setAddress("address");
    user.setRoles(Set.of(userRole));

    entityManager.persist(user);
    entityManager.flush();

    boolean exists = userRepository.existsByEmailAndUsernameNot(
            "john@test.com", "otherUsername");

    assertThat(exists).isTrue();
  }

  @Test
  void findByUserId(){
    User user = new User();
    user.setUsername("john");
    user.setEmail("");
    user.setPassword("password");
    user.setAddress("address");
    user.setRoles(Set.of(userRole));

    entityManager.persist(user);
    entityManager.flush();

    User found = userRepository.findByUserId(user.getId());
    assertNotNull(found);
    assertEquals("john", found.getUsername());
  }
}