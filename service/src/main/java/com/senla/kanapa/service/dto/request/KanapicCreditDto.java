package com.senla.kanapa.service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KanapicCreditDto {

    private Long advertisementId;
    private Integer credit;
}
