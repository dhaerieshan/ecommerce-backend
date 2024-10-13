package com.borneo.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    @GetMapping("/dashboard")
    public String adminDashboard(){
        return "Welcome to the Vendor Dashboard!";
    }


}
