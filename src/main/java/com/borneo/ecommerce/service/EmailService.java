package com.borneo.ecommerce.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private static final long OTP_EXPIRY_TIME_MS = 5 * 60 * 1000; // 5 minutes
  private final Map<String, OtpData> otpStorage = new HashMap<>();

  @Autowired private JavaMailSender mailSender;

  public void sendOtp(String email, String otp) {
    // Store OTP before sending
    otpStorage.put(email, new OtpData(otp, System.currentTimeMillis()));

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Your OTP Code");
    message.setText("Your OTP is: " + otp + ". It will expire in 5 minutes.");
    mailSender.send(message);
  }

  public boolean verifyOtp(String email, String otp) {
    OtpData otpData = otpStorage.get(email);

    if (otpData == null) {
      return false;
    }

    if (System.currentTimeMillis() - otpData.timestamp > OTP_EXPIRY_TIME_MS) {
      otpStorage.remove(email);
      return false;
    }

    if (otpData.otp.equals(otp)) {
      otpStorage.remove(email);
      return true;
    }

    return false;
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
