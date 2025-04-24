package com.bridgelabz.employeepayrollapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {
    private String oldPassword;
    private String newPassword;
}
