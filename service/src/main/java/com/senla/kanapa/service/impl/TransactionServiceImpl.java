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
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransactionServiceImpl implements TransactionService {

    private final TransactionJpaRepository transactionJpaRepository;

    private final UserJpaRepository userJpaRepository;

    private final TokenExtractData tokenExtractData;

    @Override
    @Transactional
    public void setCustomerFlag(String tokenSeller, Long transactionId) throws TokenCompareException {
        Long sellerId = tokenExtractData.extractUserIdFromToken(tokenSeller);
        log.info("The seller sets a mark on the successful operation");
        Transaction transaction = transactionJpaRepository.getReferenceById(transactionId);
        if (transaction.getAdvertisement().getUser().getId().equals(sellerId)) {
            transaction.setCustomerFlag(true);
            transactionJpaRepository.save(transaction);
        } else {
            log.error("The user Id ={} cannot edit the data", sellerId);
            throw new TokenCompareException("You don't have access to edit this transaction");
        }
    }

    @Override
    @Transactional
    public List<TransactionDto> getTransactionsSeller(String tokenSeller, Boolean customerFlag) {
        Long sellerId = tokenExtractData.extractUserIdFromToken(tokenSeller);
        log.info("The seller id = {} requested operations", sellerId);
        List<Transaction> transactionList = transactionJpaRepository.getByAdvertisement_User_IdAndCustomerFlag(sellerId, customerFlag);
        return transactionList.stream().map(TransactionMapper::toTransactionDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void setTransactionRating(String tokenCustomer, Long transactionId, Integer score) throws TokenCompareException {
        Long customerId = tokenExtractData.extractUserIdFromToken(tokenCustomer);
        log.info("The consumer Id = {} sets the rating to the seller", customerId);
        Transaction transaction = transactionJpaRepository.getReferenceById(transactionId);
        if (transaction.getCustomer().getId().equals(customerId) && transaction.getCustomerFlag()) {
            transaction.setRating(score);
            transactionJpaRepository.save(transaction);
            User seller = transaction.getAdvertisement().getUser();
            Double ratingUser = transactionJpaRepository.getRaringSeller(seller);
            seller.setRating(ratingUser);
            userJpaRepository.save(seller);
            log.info("The rating has been set");
        } else {
            log.error("The user Id ={} cannot edit the data", customerId);
            throw new TokenCompareException("You don't have access to edit this transaction");
        }
    }
}
