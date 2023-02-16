package org.bankly.authenticationservice.services;

import org.bankly.authenticationservice.dtos.WalletRequestDto;
import org.bankly.authenticationservice.dtos.WalletResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "wallet-service")
public interface WalletServiceClient {

    @PostMapping("/api/wallets")
    WalletResponseDto create(@RequestBody WalletRequestDto walletRequestDto);
}
