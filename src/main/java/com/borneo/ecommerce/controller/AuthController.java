package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.SignupRequest;
import com.borneo.ecommerce.dto.LoginRequest;
import com.borneo.ecommerce.security.JwtUtils;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.repository.UserRepository;
import com.borneo.ecommerce.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.HashSet;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<String> responseEntity(@RequestBody SignupRequest signupRequest){

        if (userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body("Error: Username is already in use");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest().body("Error: Email is already in use");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null){
            Role userRole = roleRepository.findByName("USER");
            roles.add(userRole);
        }else{
            strRoles.forEach(role ->{
                switch (role){
                    case "admin":
                        Role adminRole = roleRepository.findByName("ADMIN");
                        roles.add(adminRole);
                        break;
                    case "vendor":
                        Role vendorRole = roleRepository.findByName("VENDOR");
                        roles.add(vendorRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName("USER");
                        roles.add(userRole);

                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication.getName());

        //String result =
        return ResponseEntity.ok(jwt);

    }
}
