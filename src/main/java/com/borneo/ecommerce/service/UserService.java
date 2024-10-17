package com.borneo.ecommerce.service;

import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Data
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> findByRoleName(String roleName) {
        return userRepository.findByRoleName(roleName);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}