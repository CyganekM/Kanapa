package com.senla.kanapa.service.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class UserEditDto {

    @NotBlank(message = "Login may not be null")
    @Size(min = 4, max = 20, message = "The length of the login must be from 4 to 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "The login must contain only Latin letters or numbers")
    private String login;
    @NotBlank(message = "Name may not be null")
    private String name;
    @NotBlank(message = "Surname may not be null")
    private String surname;
    //    @NotBlank(message = "Birthdate may not be null")
    private LocalDate birthdate;
    @Email
    @NotBlank(message = "Email may not be null")
    private String email;
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{2}\\))|\\d{3})[-]?\\d{3}[-]?\\d{4}$",
            message = "Enter the phone number according to the template. Example +375(29)725-4529")
    @NotBlank(message = "Phone may not be null")
    private String phone;
}
