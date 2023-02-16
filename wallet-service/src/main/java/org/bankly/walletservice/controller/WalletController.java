package org.bankly.walletservice.controller;

import org.bankly.walletservice.dtos.WalletRequestDto;
import org.bankly.walletservice.dtos.WalletResponseDto;
import org.bankly.walletservice.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        WalletResponseDto walletResponseDto = walletService.findById(id);
        try{
            if(walletResponseDto != null){
                return walletResponseDto;
            }else{
                return ResponseEntity.badRequest().body("wallet does not found");
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/")
    public ResponseEntity<Object> find(){
        return ResponseEntity.ok().body(walletService.findAll());
    }

    @PostMapping
    public Object create(@RequestBody WalletRequestDto wallet) {
        try {
            WalletResponseDto walletResponseDto = walletService.create(wallet);
            return Objects.requireNonNullElseGet(walletResponseDto, () -> ResponseEntity.status(401).body("something wrong"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PutMapping("/transaction/{id}")
    public ResponseEntity<Object> updateBalance(@PathVariable("id") String id,@RequestBody Double balance) {
        try {
            WalletResponseDto walletResponseDto = walletService.updateBalance(id,balance);
            if(walletResponseDto !=null){
                return ResponseEntity.status(HttpStatus.CREATED).body(walletResponseDto);
            }else {
                return ResponseEntity.status(401).body("something wrong");
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<Object> update(@PathVariable("id") String id,@RequestBody WalletRequestDto walletDetails) {
        try {
            WalletResponseDto walletResponseDto = walletService.update(id,walletDetails);
            if(walletResponseDto !=null){
                return ResponseEntity.status(HttpStatus.CREATED).body(walletResponseDto);
            }else {
                return ResponseEntity.status(401).body("something wrong");
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
       walletService.delete(id);
       return ResponseEntity.ok().build();
    }
}
