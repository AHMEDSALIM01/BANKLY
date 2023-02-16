package org.bankly.walletservice.services.serviceimpl;

import org.bankly.walletservice.entities.Wallet;
import org.bankly.walletservice.repositories.WalletRepository;
import org.bankly.walletservice.dtos.WalletRequestDto;
import org.bankly.walletservice.dtos.WalletResponseDto;
import org.bankly.walletservice.services.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.el.PropertyNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private static final String WALLET_NOT_FOUND = "wallet does not found";
    @Override
    public WalletResponseDto create(WalletRequestDto wallet) {

        return getResponseDto(wallet);
    }

    @Override
    public WalletResponseDto update(String id, WalletRequestDto walletDetails) {
        if(!walletRepository.existsById(id)){
            throw new IllegalArgumentException(WALLET_NOT_FOUND);
        }
        return getResponseDto(walletDetails);
    }

    private WalletResponseDto getResponseDto(WalletRequestDto walletDetails) {
        if(walletDetails==null){
            throw new IllegalArgumentException("wallet should not be null");
        }
        if(walletDetails.getBalance() == null){
            throw new IllegalArgumentException("wallet balance should not be null");
        }
        if(walletDetails.getBalance()<0){
            throw new IllegalArgumentException("wallet balance should not be less then 0");
        }
        if(walletDetails.getUserId() == null){
            throw new IllegalArgumentException("user Id is required");
        }
        Wallet walletEntity = modelMapper.map(walletDetails,Wallet.class);
        walletEntity.setId(null);
        walletEntity.setCreatedAt(LocalDateTime.now());
        walletEntity.setUpdatedAt(LocalDateTime.now());
        Wallet walletSaved = walletRepository.save(walletEntity);
        log.info("wallet {} saved",walletSaved.getId());
        return modelMapper.map(walletSaved, WalletResponseDto.class);
    }

    @Override
    public void delete(String id) {
        if(!walletRepository.existsById(id)){
            throw new PropertyNotFoundException(WALLET_NOT_FOUND);
        }
        walletRepository.deleteById(id);
    }

    @Override
    public List<WalletResponseDto> findAll() {
        List<Wallet> wallets = walletRepository.findAll();
        return wallets.stream().map(wallet -> modelMapper.map(wallet, WalletResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public WalletResponseDto findById(String id) {
       Optional<Wallet> wallet = walletRepository.findById(id);
       if(wallet.isPresent()){
           return modelMapper.map(wallet.get(),WalletResponseDto.class);
       }
       throw new IllegalArgumentException(WALLET_NOT_FOUND);
    }

    @Override
    public WalletResponseDto updateBalance(String id, Double balance) {
        if(!walletRepository.existsById(id)){
            throw new IllegalArgumentException(WALLET_NOT_FOUND);
        }
        if(balance == null){
            throw new IllegalArgumentException("wallet balance should not be null");
        }
        if(balance<0){
            throw new IllegalArgumentException("wallet balance should not be less then 0");
        }
        Optional<Wallet> walletOptional = walletRepository.findById(id);
        if(walletOptional.isPresent()){
            Wallet wallet = walletOptional.get();
            wallet.setBalance(balance);
            wallet.setUpdatedAt(LocalDateTime.now());
            walletRepository.save(wallet);
            return modelMapper.map(wallet,WalletResponseDto.class);
        }
        throw new IllegalArgumentException(WALLET_NOT_FOUND);
    }
}
