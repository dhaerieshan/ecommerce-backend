package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private EmailService emailService;

    private String createOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    @PostMapping("/send")
    public String sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = createOtp();
        emailService.sendOtp(email, otp);
        return "OTP sent successfully";  // Never expose OTP in response
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (emailService.verifyOtp(email, otp)) {
            return "OTP verified successfully!";
        }
        return "Invalid OTP";
    }
}
