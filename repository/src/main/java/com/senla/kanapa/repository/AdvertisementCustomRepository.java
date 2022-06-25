package com.senla.kanapa.repository;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.Filter;
import com.senla.kanapa.repository.exception.OperationException;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AdvertisementCustomRepository {
    List<Advertisement> getQueryResult(List<Filter<?>> filterDtoList, Sort sort) throws OperationException;
}
