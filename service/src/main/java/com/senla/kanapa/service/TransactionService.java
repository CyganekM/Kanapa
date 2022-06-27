package com.senla.kanapa.service;

import com.senla.kanapa.service.dto.TransactionDto;
import com.senla.kanapa.service.exception.TokenCompareException;

import java.util.List;

public interface TransactionService {

    void setCustomerFlag(String tokenSeller, Long transactionId) throws TokenCompareException;

    List<TransactionDto> getTransactionsSeller(String tokenSeller, Boolean customerFlag);

    void setTransactionRating(String tokenCustomer, Long transactionId, Integer score) throws TokenCompareException;
}
