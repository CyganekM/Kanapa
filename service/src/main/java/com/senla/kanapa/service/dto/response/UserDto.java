package com.senla.kanapa.service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserDto {

    private String login;
    private String name;
    private String surname;
    private LocalDate birthdate;
    private String email;
    private String phone;
    private Integer bonus;
}
