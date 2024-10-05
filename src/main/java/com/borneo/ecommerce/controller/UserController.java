package com.borneo.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/User")
public class UserController {

    @GetMapping("/dashboard")
    public String adminDashboard(){
        return "Welcome to the User Dashboard!";
    }

}
