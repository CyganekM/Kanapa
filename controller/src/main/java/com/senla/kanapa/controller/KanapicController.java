package com.senla.kanapa.controller;

import com.senla.kanapa.service.KanapicService;
import com.senla.kanapa.service.dto.request.KanapicCreditDto;
import com.senla.kanapa.service.dto.request.KanapicDebitDto;
import com.senla.kanapa.service.exception.AccountBalanceException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kanapics")
@RequiredArgsConstructor
public class KanapicController {

    private final KanapicService kanapicService;

    @PostMapping("/debit")
    @Operation(summary = "Поступление КАНАПИКОВ")
    @ResponseStatus(HttpStatus.OK)
    public void setKanapicDebit(@RequestBody KanapicDebitDto kanapicDebitDto) {
        kanapicService.addKanapicDebit(kanapicDebitDto);
    }

    @PostMapping("/credit")
    @Operation(summary = "Расход Канапиков")
    @ResponseStatus(HttpStatus.OK)
    public void setKanapicCredit(@RequestBody KanapicCreditDto kanapicCreditDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws AccountBalanceException {
        kanapicService.addKanapicCredit(kanapicCreditDto, token);
    }
}
