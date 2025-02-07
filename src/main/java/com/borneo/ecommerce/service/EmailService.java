package com.borneo.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private static final long OTP_EXPIRY_TIME_MS = 5 * 60 * 1000; // 5 minutes
    private final Map<String, OtpData> otpStorage = new HashMap<>();
    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String email, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + ". It will expire in 5 minutes.");

        mailSender.send(message);
    }

    public boolean verifyOtp(String email, String otp) {
        OtpData otpData = otpStorage.get(email);

        if (otpData == null) {
            return false; // ❌ OTP not found
        }

        // Check if OTP has expired
        if (System.currentTimeMillis() - otpData.timestamp > OTP_EXPIRY_TIME_MS) {
            otpStorage.remove(email); // ✅ Delete expired OTP
            return false; // ❌ OTP expired
        }

        // Check if OTP matches
        if (otpData.otp.equals(otp)) {
            otpStorage.remove(email); // ✅ Delete OTP after successful verification
            return true;
        }

        return false; // ❌ Invalid OTP
    }

    private static class OtpData {
        String otp;
        long timestamp;

        OtpData(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }
    }
}