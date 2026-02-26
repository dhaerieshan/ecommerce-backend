package com.borneo.ecommerce.dto;


import lombok.Data;

import java.util.Set;

@Data
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
    private String firstName;
    private String lastName;
    private String address;

}
