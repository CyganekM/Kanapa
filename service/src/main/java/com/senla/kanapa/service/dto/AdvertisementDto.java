package com.senla.kanapa.service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AdvertisementDto {

    private Long advertisementId;
    private String name;
    private BigDecimal price;
    private String description;
    private LocalDateTime dateStart;
    private Long userId;
    private Long categoryId;
}
