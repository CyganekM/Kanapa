package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Transaction;
import com.senla.kanapa.entity.User;
import com.senla.kanapa.repository.TransactionJpaRepository;
import com.senla.kanapa.repository.UserJpaRepository;
import com.senla.kanapa.service.TransactionService;
import com.senla.kanapa.service.dto.TransactionDto;
import com.senla.kanapa.service.exception.TokenCompareException;
import com.senla.kanapa.service.mapper.TransactionMapper;
import com.senla.kanapa.service.security.TokenExtractData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionJpaRepository transactionJpaRepository;

    private final UserJpaRepository userJpaRepository;

    private final TokenExtractData tokenExtractData;

    @Override
    public void setCustomerFlag(String tokenSeller, Long transactionId) throws TokenCompareException {
        Transaction transaction = transactionJpaRepository.getReferenceById(transactionId);
        if (transaction.getAdvertisement().getUser().getId().equals(tokenExtractData.extractUserIdFromToken(tokenSeller))) {
            transaction.setCustomerFlag(true);
            transactionJpaRepository.save(transaction);
        } else {
            throw new TokenCompareException("You don't have access to edit this transaction");
        }
    }

    @Override
    public List<TransactionDto> getTransactionsSeller(String tokenSeller, Boolean customerFlag) {
        Long sellerId = tokenExtractData.extractUserIdFromToken(tokenSeller);
        List<Transaction> transactionList = transactionJpaRepository.getByAdvertisement_User_IdAndCustomerFlag(sellerId, customerFlag);
        return transactionList.stream().map(TransactionMapper::convertTransactionToDto).collect(Collectors.toList());
    }

    @Override
    public void setTransactionRating(String tokenSeller, Long transactionId, Integer score) throws TokenCompareException {
        Transaction transaction = transactionJpaRepository.getReferenceById(transactionId);
        Long customerId = tokenExtractData.extractUserIdFromToken(tokenSeller);
        if (transaction.getCustomer().getId().equals(customerId) && transaction.getCustomerFlag()) {
            transaction.setRating(score);
            transactionJpaRepository.save(transaction);
            User seller = transaction.getAdvertisement().getUser();
            Double ratingUser = transactionJpaRepository.getRaringSeller(seller);
            seller.setRating(ratingUser);
            userJpaRepository.save(seller);
        } else {
            throw new TokenCompareException("You don't have access to edit this transaction");
        }
    }
}
