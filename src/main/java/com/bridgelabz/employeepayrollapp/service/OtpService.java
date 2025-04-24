package com.bridgelabz.employeepayrollapp.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();

    public void storeOtp(String email, String otp) {
        otpStorage.put(email, otp);
    }

    public String getOtp(String email) {
        return otpStorage.get(email);
    }

    public void clearOtp(String email) {
        otpStorage.remove(email);
    }
}
