package com.bridgelabz.employeepayrollapp.controller;

import com.bridgelabz.employeepayrollapp.dto.ForgotPasswordDTO;
import com.bridgelabz.employeepayrollapp.dto.ResetPasswordDTO;
import com.bridgelabz.employeepayrollapp.dto.UpdatePasswordDTO;
import com.bridgelabz.employeepayrollapp.dto.UserDTO;
import com.bridgelabz.employeepayrollapp.model.User;
import com.bridgelabz.employeepayrollapp.repository.UserRepository;
import com.bridgelabz.employeepayrollapp.service.EmailService;
import com.bridgelabz.employeepayrollapp.service.OtpService;
import com.bridgelabz.employeepayrollapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("User Registered Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String token = jwtUtil.generateToken(userDTO.getEmail());

                emailService.sendLoginTokenEmail(userDTO.getEmail(), token);

                return ResponseEntity.ok("Login successful. Token: " + token);
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO request) {
        String email = request.getEmail().trim();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String otp = generateOtp();
        otpService.storeOtp(email, otp);
        emailService.sendPasswordResetEmail(email, otp);

        return ResponseEntity.ok("OTP sent to your email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO dto) {
        Optional<User> userOptional = userRepository.findByEmail(dto.getEmail().trim());

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String storedOtp = otpService.getOtp(dto.getEmail().trim());

        if (storedOtp == null || !storedOtp.equals(dto.getOtp().trim())) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        otpService.clearOtp(dto.getEmail().trim());

        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdatePasswordDTO updatePasswordDTO) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String actualToken = token.substring(7);

        if (!jwtUtil.validateToken(actualToken)) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        String email = jwtUtil.extractEmail(actualToken);
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(updatePasswordDTO.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }

    private String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }
}
