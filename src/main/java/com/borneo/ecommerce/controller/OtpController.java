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

    private static final long OTP_EXPIRY_TIME_MS = 5 * 60 * 1000; // 5 minutes
    @Autowired
    private EmailService emailService;

    public String createOtp() {
        String otp;
        return otp = String.valueOf(new Random().nextInt(900000) + 100000);

    }

    @PostMapping("/send")
    public String sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String Otp = createOtp();
        emailService.sendOtp(email, Otp);
        return "OTP is" + Otp;
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