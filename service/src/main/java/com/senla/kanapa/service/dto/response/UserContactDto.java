package com.senla.kanapa.service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserContactDto {

    private String name;
    private String surname;
    private String email;
    private String phone;
}
