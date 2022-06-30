package com.senla.kanapa.service;

import com.senla.kanapa.service.dto.request.KanapicCreditDto;
import com.senla.kanapa.service.dto.request.KanapicDebitDto;
import com.senla.kanapa.service.exception.AccountBalanceException;

public interface KanapicService {

    void creditKanapic(KanapicDebitDto kanapicDebitDto);

    void debitKanapic(KanapicCreditDto kanapicCreditDto, String token) throws AccountBalanceException;
}
