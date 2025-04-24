package com.bridgelabz.employeepayrollapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(toEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailSender.send(mailMessage);
        } catch (MailAuthenticationException e) {
            e.printStackTrace();
            throw new RuntimeException("Error sending email: " + e.getMessage());
        }
    }

    public void sendPasswordResetEmail(String toEmail, String otp) {
        String subject = "Password Reset OTP";
        String message = "Your OTP to reset the password is: " + otp;
        sendEmail(toEmail, subject, message);
    }

    public void sendLoginTokenEmail(String toEmail, String token) {
        String subject = "Login Token";
        String message = "Your login token is: " + token;
        sendEmail(toEmail, subject, message);
    }
}
