package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.MessageResponse;
import com.borneo.ecommerce.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "02. OTP Verification", description = "OTP generation and verification APIs")
@RestController
@RequestMapping("/api/otp")
public class OtpController {

  @Autowired private EmailService emailService;

  private String createOtp() {
    return String.valueOf(new Random().nextInt(900000) + 100000);
  }

  @Operation(
      summary = "Send OTP",
      description =
          "Generates a 6-digit OTP and sends it to the provided email address for verification.",
      requestBody =
          @RequestBody(
              description = "Email address to send OTP to",
              required = true,
              content = @Content(schema = @Schema(example = "{\"email\": \"john@example.com\"}"))),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "OTP sent successfully to email",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"OTP sent successfully\"}"))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid or missing email address",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value = "{\"message\": \"Invalid or missing email address\"}"))),
        @ApiResponse(
            responseCode = "500",
            description = "Failed to send email",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"message\": \"Something went wrong. Please try again later.\"}")))
      })
  @PostMapping("/send")
  public String sendOtp(
      @org.springframework.web.bind.annotation.RequestBody Map<String, String> request) {
    String email = request.get("email");
    String otp = createOtp();
    emailService.sendOtp(email, otp);
    return "OTP sent successfully";
  }

  @Operation(
      summary = "Verify OTP",
      description = "Verifies the OTP entered by the user against the one sent to their email.",
      requestBody =
          @RequestBody(
              description = "Email and OTP for verification",
              required = true,
              content =
                  @Content(
                      schema =
                          @Schema(
                              example = "{\"email\": \"john@example.com\", \"otp\": \"123456\"}"))),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "OTP verified successfully",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(value = "{\"message\": \"OTP verified successfully!\"}"))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid or expired OTP",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Invalid OTP\"}")))
      })
  @PostMapping("/verify")
  public String verifyOtp(
      @org.springframework.web.bind.annotation.RequestBody Map<String, String> request) {
    String email = request.get("email");
    String otp = request.get("otp");
    if (emailService.verifyOtp(email, otp)) {
      return "OTP verified successfully!";
    }
    return "Invalid OTP";
  }
}
