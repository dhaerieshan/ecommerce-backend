package com.borneo.ecommerce.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserResponseDTO {

    @NotBlank
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private Set<String> roles;


    private String firstName;
    private String lastName;
    private String address;

    private String fatherName;
    private LocalDate DOB;

}
