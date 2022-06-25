package com.senla.kanapa.service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KanapicDebitDto {

    private Long userId;
    private Integer debit;
}
