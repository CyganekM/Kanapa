package com.senla.kanapa.service;

import com.senla.kanapa.repository.exception.OperationException;
import com.senla.kanapa.service.dto.AdvertisementDto;
import com.senla.kanapa.service.dto.request.FilterDto;
import com.senla.kanapa.service.exception.TokenCompareException;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AdvertisementService {

    void saveAdvertisement(AdvertisementDto advertisementDto, String token);

    void editAdvertisement(Long advertisementId, AdvertisementDto advertisementDto, String token) throws TokenCompareException;

    void closeAdvertisement(Long advertisementId, String token) throws TokenCompareException;

    List<AdvertisementDto> getAdvertisementByUser(Long userId);

    List<AdvertisementDto> getAdvertisementByFilter(List<FilterDto> filterDtoList, String fieldSort, Sort.Direction sort) throws OperationException;
}
