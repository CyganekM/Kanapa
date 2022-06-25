package com.senla.kanapa.service.dto.request;

import com.senla.kanapa.entity.FieldFilter;
import com.senla.kanapa.entity.QueryOperator;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Data
@Builder
public class FilterDto {

    @Enumerated(EnumType.STRING)
    private FieldFilter key;
    @Enumerated(EnumType.STRING)
    private QueryOperator operator;
    private String value;
    private List<String> values;
}
