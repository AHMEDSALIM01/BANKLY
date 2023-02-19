package org.bankly.transactionservice.service;


import org.bankly.transactionservice.dtos.WalletResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "wallet-service")
public interface WalletServiceClient {
    @GetMapping("/api/wallets/{id}")
    WalletResponseDto findById(@PathVariable("id") String id);

    @PutMapping("/api/wallets/transaction/{id}")
    WalletResponseDto updateBalance(@PathVariable("id") String id,@RequestBody Double balance);
}
