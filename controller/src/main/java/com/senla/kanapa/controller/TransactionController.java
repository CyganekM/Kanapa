package com.senla.kanapa.controller;

import com.senla.kanapa.service.TransactionService;
import com.senla.kanapa.service.dto.TransactionDto;
import com.senla.kanapa.service.exception.TokenCompareException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@Validated
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping("/{transactionId}")
    @Operation(summary = "Set the transaction sign",
            description = "If the buyer has requested the seller's data, the seller can put a sign of the transaction")
    @ResponseStatus(HttpStatus.OK)
    public void setCustomerFlag(@PathVariable Long transactionId, @RequestHeader(HttpHeaders.AUTHORIZATION) String tokenSeller) throws TokenCompareException {
        transactionService.setCustomerFlag(tokenSeller, transactionId);
    }

    @GetMapping
    @Operation(summary = "Show transactions (for the seller)")
    public ResponseEntity<List<TransactionDto>> getTransactionsSeller(@RequestHeader(HttpHeaders.AUTHORIZATION) String tokenSeller, @RequestParam Boolean customerFlag) {
        return ResponseEntity.ok().body(transactionService.getTransactionsSeller(tokenSeller, customerFlag));
    }

    @PutMapping("/{transactionId}/rating")
    @Operation(summary = "Set a rating for the seller",
            description = "Can only be done by the user who made the transaction (confirmed by the seller)")
    @ResponseStatus(HttpStatus.OK)
    public void setSellerRating(@RequestHeader(HttpHeaders.AUTHORIZATION) String tokenCustomer,
                                @PathVariable Long transactionId, @RequestParam @Min(1) @Max(10) Integer score) throws TokenCompareException {
        transactionService.setTransactionRating(tokenCustomer, transactionId, score);
    }
}
