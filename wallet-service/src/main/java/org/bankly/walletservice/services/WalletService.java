package org.bankly.walletservice.services;

import org.bankly.walletservice.dtos.WalletRequestDto;
import org.bankly.walletservice.dtos.WalletResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface WalletService {
    public WalletResponseDto create(WalletRequestDto wallet);
    public WalletResponseDto update(String id, WalletRequestDto walletDetails);
    public void delete(String id);
    public List<WalletResponseDto> findAll();
    public WalletResponseDto findById(String id);
    public WalletResponseDto updateBalance(String id,Double balance);
}
