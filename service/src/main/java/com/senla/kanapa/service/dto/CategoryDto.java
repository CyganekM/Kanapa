package com.senla.kanapa.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {

    private Long id;
    private Long parent;
    private String name;
    private String description;
}
