package com.senla.kanapa.service.mapper;

import com.senla.kanapa.entity.Advertisement_;
import com.senla.kanapa.entity.Category;
import com.senla.kanapa.entity.Filter;
import com.senla.kanapa.entity.QueryOperator;
import com.senla.kanapa.repository.CategoryJpaRepository;
import com.senla.kanapa.service.dto.request.FilterDto;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@UtilityClass
public class FilterMapper {

    public Filter<?> toFilter(FilterDto filterDto, CategoryJpaRepository categoryJpaRepository) {

        switch (filterDto.getKey()) {
            case PRICE:
                return Filter.<BigDecimal>builder()
                        .key(Advertisement_.PRICE)
                        .queryOperator(filterDto.getOperator())
                        .value(new BigDecimal(filterDto.getValue()))
                        .build();
            case DATE_REGISTRY:
                return Filter.<LocalDateTime>builder()
                        .key(Advertisement_.DATE_START)
                        .queryOperator(filterDto.getOperator())
                        .value(LocalDateTime.parse(filterDto.getValue()))
                        .build();
            case NAME:
                return Filter.<String>builder()
                        .key(Advertisement_.NAME)
                        .queryOperator(filterDto.getOperator())
                        .value(filterDto.getValue())
                        .build();
            case DESCRIPTION:
                return Filter.<String>builder()
                        .key(Advertisement_.DESCRIPTION)
                        .queryOperator(filterDto.getOperator())
                        .value(filterDto.getValue())
                        .build();
            case CATEGORY:
                return Filter.<Category>builder()
                        .key(Advertisement_.CATEGORY)
                        .queryOperator(QueryOperator.IN)
                        .values(categoryJpaRepository.getByIdOrParentCategory(Long.parseLong(filterDto.getValue())))
                        .build();
            default:
                return null;
        }
    }
}
