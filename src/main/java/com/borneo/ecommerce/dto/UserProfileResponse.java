package com.borneo.ecommerce.dto;

import lombok.Data;

@Data
public class UserProfileResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
}