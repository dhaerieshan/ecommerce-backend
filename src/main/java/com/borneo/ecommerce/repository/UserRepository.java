package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
    User findByUsernameIgnoreCase(@Param("username")String username);
}
