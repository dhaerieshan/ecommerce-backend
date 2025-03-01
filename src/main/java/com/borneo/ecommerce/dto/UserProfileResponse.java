package com.borneo.ecommerce.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String rationCardNumber;
    private String address;
    private LocalDate DOB;
    private String fatherName;
}