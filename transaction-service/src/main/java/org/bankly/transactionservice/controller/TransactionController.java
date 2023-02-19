package org.bankly.transactionservice.controller;


import org.bankly.transactionservice.dtos.TransactionRequestDto;
import org.bankly.transactionservice.dtos.TransactionResponseDto;
import org.bankly.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping("/withdrawal")
    public ResponseEntity<Object> withdrawal(@RequestBody TransactionRequestDto transaction){
        try{
            TransactionResponseDto transactionResponseDto = transactionService.withdrawal(transaction);
            if(transactionResponseDto == null){
                return ResponseEntity.badRequest().body("something wrong transaction not executed");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDto);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/deposit")
    public ResponseEntity<Object> deposit(@RequestBody TransactionRequestDto transaction){
        try{
            TransactionResponseDto transactionResponseDto = transactionService.deposit(transaction);
            if(transactionResponseDto == null){
                return ResponseEntity.badRequest().body("something wrong transaction not executed");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDto);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
