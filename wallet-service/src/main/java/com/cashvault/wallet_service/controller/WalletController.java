package com.cashvault.wallet_service.controller;

import com.cashvault.wallet_service.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    Environment environment;

    @Autowired
    WalletService walletService;

    @PostMapping("/update")
    public ResponseEntity<String> updateWallet(@RequestParam String senderId,
                                               @RequestParam String receiverId,
                                               @RequestParam Long amount) {
        // Validate input parameters
        if (senderId == null || senderId.isEmpty() || receiverId == null || receiverId.isEmpty()) {
            return new ResponseEntity<>("Sender and receiver IDs must not be null or empty", HttpStatus.BAD_REQUEST);
        }

        if (amount == null || amount <= 0) {
            return new ResponseEntity<>("Amount must be greater than zero", HttpStatus.BAD_REQUEST);
        }

        String msg;
        try {
            // Display server port in logs for debugging (if needed)
            System.out.println(environment.getProperty("local.server.port"));

            // Call the wallet update service
            msg = walletService.updateWallet(senderId, receiverId, amount);
        } catch (RuntimeException e) {
            // Return a response in case of a RuntimeException
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Return success response
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
