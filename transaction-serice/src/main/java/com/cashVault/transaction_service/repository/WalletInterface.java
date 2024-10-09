package com.cashVault.transaction_service.repository;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("WALLET-SERVICE")
public interface WalletInterface {

    @PostMapping("/wallet/update")
    public ResponseEntity<String> updateWallet(@RequestParam String senderId,
                                               @RequestParam String receiverId,
                                               @RequestParam Long amount);
}
