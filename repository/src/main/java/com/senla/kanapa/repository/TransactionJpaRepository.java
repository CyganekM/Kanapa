package com.senla.kanapa.repository;

import com.senla.kanapa.entity.Transaction;
import com.senla.kanapa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> getByAdvertisement_User_IdAndCustomerFlag(Long userId, Boolean customerFlag);

    @Query("select avg(t.rating) from Transaction t where t.customerFlag = true and t.advertisement.user = :seller")
    Double getRaringSeller(User seller);
}
