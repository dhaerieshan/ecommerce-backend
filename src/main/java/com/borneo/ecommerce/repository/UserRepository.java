package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByEmailAndUsernameNot(String email, String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :id")
    User findByUserId(@Param("id") long id);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE UPPER(r.name) = UPPER(:roleName)")
    List<User> findByRoleName(String roleName);
}
