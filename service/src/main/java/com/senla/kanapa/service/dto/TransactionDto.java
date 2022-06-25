package com.senla.kanapa.service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDto {

    private Long id;
    private LocalDateTime date;
    private Long userId;
    private Long advertisementId;
}
