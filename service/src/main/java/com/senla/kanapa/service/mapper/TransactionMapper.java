package com.senla.kanapa.service.mapper;

import com.senla.kanapa.entity.Transaction;
import com.senla.kanapa.service.dto.TransactionDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionMapper {

    public TransactionDto convertTransactionToDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .date(transaction.getDate())
                .advertisementId(transaction.getAdvertisement().getId())
                .userId(transaction.getCustomer().getId())
                .build();
    }
}
