package com.borneo.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Setter;

@Data
public class UserUpdateRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String oldPassword;

    @Setter
    @NotBlank
    private String newPassword;


}