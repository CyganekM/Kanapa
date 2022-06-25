package com.senla.kanapa.service.dto.request;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class UserPasswordDto {

    private Long userId;
    private String passwordOld;
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,20}$",
            message = "The length of the login must be from 4 to 20 characters," +
                    " must contain at least one number," +
                    " at least one character in uppercase and lowercase")
    private String passwordNew;
}
