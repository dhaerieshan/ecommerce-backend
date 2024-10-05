package com.borneo.ecommerce.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponseDTO {

    @NotBlank
    private long id;

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private Set<String> roles;

}
