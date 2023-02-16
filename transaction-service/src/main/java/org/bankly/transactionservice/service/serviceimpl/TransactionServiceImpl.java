package org.bankly.transactionservice.service.serviceimpl;

import org.bankly.transactionservice.dtos.TransactionRequestDto;
import org.bankly.transactionservice.dtos.TransactionResponseDto;
import org.bankly.transactionservice.dtos.WalletResponseDto;
import org.bankly.transactionservice.entities.Transaction;
import org.bankly.transactionservice.enums.TransactionType;
import org.bankly.transactionservice.repositories.TransactionRepository;
import org.bankly.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bankly.transactionservice.service.WalletServiceClient;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletServiceClient walletServiceClient;
    private final ModelMapper modelMapper;
    @Override
    public TransactionResponseDto withdrawal(TransactionRequestDto transaction) {
        transaction.setType(TransactionType.valueOf("WITHDRAWAL"));
        return getResponseDto(transaction);
    }

    @Override
    public TransactionResponseDto deposit(TransactionRequestDto transaction) {
        transaction.setType(TransactionType.valueOf("DEPOSIT"));
        return getResponseDto(transaction);
    }

    private TransactionResponseDto getResponseDto(TransactionRequestDto transaction) {
        if(transaction == null){
            throw new IllegalArgumentException("transaction should not be null");
        }
        if (transaction.getWalletId() == null){
            throw new IllegalArgumentException("transaction wallet should not be null");

        }
        if(transaction.getAmount() == null){
            throw new IllegalArgumentException("transaction amount should not be null");
        }
        if(transaction.getAmount()<=0){
            throw new IllegalArgumentException("transaction amount should be greater than 0");
        }
        Transaction transactionEntity = new Transaction();
        transactionEntity.setWalletId(transaction.getWalletId());
        transactionEntity.setAmount(transaction.getAmount());
        transactionEntity.setCreatedAt(LocalDateTime.now());
        transactionEntity.setType(transaction.getType());
        if(transactionEntity.getType() == TransactionType.valueOf("DEPOSIT")){
            WalletResponseDto walletResponseDto = walletServiceClient.findById(transactionEntity.getWalletId());
            if(walletResponseDto !=null){
                Double balance = walletResponseDto.getBalance();
                balance += transactionEntity.getAmount();

                walletServiceClient.updateBalance(walletResponseDto.getId(),balance);
            }else {
                throw new IllegalArgumentException("wallet does not found");
            }

        }
        if(transactionEntity.getType() == TransactionType.valueOf("WITHDRAWAL")){
            WalletResponseDto walletResponseDto = walletServiceClient.findById(transactionEntity.getWalletId());
            if(walletResponseDto !=null){
                Double balance = walletResponseDto.getBalance();
                if(balance>0 && balance>transactionEntity.getAmount()){
                    balance -= transactionEntity.getAmount();
                    walletServiceClient.updateBalance(walletResponseDto.getId(),balance);
                }else{
                    throw new IllegalStateException("balance not valid");
                }

            }else {
                throw new IllegalArgumentException("wallet does not found");
            }

        }
        return modelMapper.map(transactionRepository.save(transactionEntity), TransactionResponseDto.class);
    }
}
