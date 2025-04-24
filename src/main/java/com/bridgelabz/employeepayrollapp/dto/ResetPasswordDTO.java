package com.bridgelabz.employeepayrollapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {
    private String email;
    private String otp;
    private String newPassword;
}
