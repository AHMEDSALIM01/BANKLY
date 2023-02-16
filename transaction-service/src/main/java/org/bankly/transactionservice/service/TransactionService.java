package org.bankly.transactionservice.service;

import org.bankly.transactionservice.dtos.TransactionRequestDto;
import org.bankly.transactionservice.dtos.TransactionResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    public TransactionResponseDto withdrawal(TransactionRequestDto transaction);
    public TransactionResponseDto deposit(TransactionRequestDto transaction);
}
