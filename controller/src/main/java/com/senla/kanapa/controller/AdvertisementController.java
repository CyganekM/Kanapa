package com.senla.kanapa.controller;

import com.senla.kanapa.repository.exception.OperationException;
import com.senla.kanapa.service.AdvertisementService;
import com.senla.kanapa.service.dto.AdvertisementDto;
import com.senla.kanapa.service.dto.request.FilterDto;
import com.senla.kanapa.service.exception.TokenCompareException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/advertisements")
@RequiredArgsConstructor
public class AdvertisementController {

    private static final String FIELD_SORT = "dateBonus,user_rating";
    private static final String SORT_ORDER = "DESC";
    private final AdvertisementService advertisementService;

    @PostMapping
    @Operation(summary = "Добавить объявление")
    @ResponseStatus(HttpStatus.OK)
    public void addAdvertisement(@RequestBody AdvertisementDto advertisementDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        advertisementService.saveAdvertisement(advertisementDto, token);
    }

    @PutMapping("/{advertisementId}")
    @Operation(summary = "Редактировать объявление")
    @ResponseStatus(HttpStatus.OK)
    public void editAdvertisement(@RequestBody AdvertisementDto advertisementDto, @PathVariable Long advertisementId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws TokenCompareException {
        advertisementService.editAdvertisement(advertisementId, advertisementDto, token);
    }

    @DeleteMapping("/{advertisementId}")
    @Operation(summary = "Закрыть объявление")
    @ResponseStatus(HttpStatus.OK)
    public void closeAdvertisement(@PathVariable Long advertisementId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws TokenCompareException {
        advertisementService.closeAdvertisement(advertisementId, token);
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Показать текущие(открытые) объявления пользоватея")
    @ResponseStatus(HttpStatus.OK)
    public List<AdvertisementDto> getOpenAdvertisementByUser(@PathVariable Long userId) {
        return advertisementService.getAdvertisementByUser(userId);
    }

    @GetMapping
    @Operation(summary = "Фильтры и сортировка объявлений")
    @ResponseStatus(HttpStatus.OK)
    public List<AdvertisementDto> getCurrentOrder(@RequestBody(required = false) List<FilterDto> filterDto,
                                                  @RequestParam(defaultValue = FIELD_SORT) String fieldSort,
                                                  @RequestParam(defaultValue = SORT_ORDER) Direction sort) throws OperationException {
        return advertisementService.getAdvertisementByFilter(filterDto, fieldSort, sort);
    }
}
