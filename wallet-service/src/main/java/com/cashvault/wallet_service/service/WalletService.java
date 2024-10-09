package com.cashvault.wallet_service.service;


import com.cashvault.wallet_service.repository.WalletRepository;
import com.cashvault.wallet_service.model.Wallet;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private Long initialBalance = 100L;

    @Autowired
    WalletRepository walletRepository;


    /**
     * User onboarding flow
     *
     *
     */
    @KafkaListener(topics = {"user_created"}, groupId = "jbdl123")
    public void createWallet(String msg) {
        try {
            JSONObject userJsonObject = (JSONObject) new JSONParser().parse(msg);

            String mobileNumber = (String) userJsonObject.get("phone");

            // Check if the wallet already exists
            Wallet existingWallet = walletRepository.findWalletByWalletId(mobileNumber);
            if (existingWallet == null) {
                Wallet wallet = Wallet.builder()
                        .walletId(mobileNumber)
                        .currency("INR")
                        .balance(initialBalance)
                        .build();

                walletRepository.save(wallet);
            } else {
                System.out.println("Wallet already exists for user with mobile number: " + mobileNumber);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public String updateWallet(String senderId, String receiverId, Long amount) {
        Wallet senderWallet = walletRepository.findWalletByWalletId(senderId);
        Wallet receiverWallet = walletRepository.findWalletByWalletId(receiverId);

        if (receiverWallet == null) {
            throw new RuntimeException("Receiver wallet not found for ID: " + receiverId);
        }

        if (senderWallet == null) {
            throw new RuntimeException("Sender wallet not found for ID: " + senderId);
        }

        if (senderWallet.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance in sender's wallet");
        }

        walletRepository.updateWallet(senderId, -amount);
        walletRepository.updateWallet(receiverId, amount);

        return "Wallet updated successfully";
    }





}
