package com.borneo.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @Size(min = 3, max = 40)
    private String username;

    @Email
    private String email;

    @Size(min = 8, max = 50)
    private String password;

    private String firstName;
    private String lastName;
    private String address;

}