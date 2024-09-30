package com.borneo.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.borneo.ecommerce.model.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}