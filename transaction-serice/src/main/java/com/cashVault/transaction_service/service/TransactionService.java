package com.cashVault.transaction_service.service;

import com.cashVault.transaction_service.dto.TransactionCreateRequest;
import com.cashVault.transaction_service.models.Transaction;
import com.cashVault.transaction_service.models.TransactionStatus;
import com.cashVault.transaction_service.repository.TransactionRepository;
import com.cashVault.transaction_service.repository.WalletInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.JSONObject;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    WalletInterface walletInterface;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;  // Use the ObjectMapper as a Spring bean

    public String transact(TransactionCreateRequest transactionCreateRequest, String senderId) throws JsonProcessingException {
        Transaction transaction = Transaction.builder()
                .senderId(senderId)
                .receiverId(transactionCreateRequest.getReceiverId())
                .amount(transactionCreateRequest.getAmount())
                .reason(transactionCreateRequest.getReason())
                .transactionStatus(TransactionStatus.PENDING)
                .externalTransactionId(UUID.randomUUID().toString())
                .build();

        // Save transaction with PENDING status
        transactionRepository.save(transaction);

        TransactionStatus transactionStatus = null;

        try {
            // Call wallet interface to update the wallet balance
            ResponseEntity<String> responseEntity = walletInterface.updateWallet(
                    transaction.getSenderId(),
                    transaction.getReceiverId(),
                    transactionCreateRequest.getAmount());

            // Check if the wallet update was successful
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                transactionStatus = TransactionStatus.SUCCESSFUL;
            } else {
                transactionStatus = TransactionStatus.FAILURE;
            }
        } catch (Exception e) {
            // Handle any exceptions (e.g., connection issues) and mark transaction as failed
            transactionStatus = TransactionStatus.FAILURE;
        }

        // Update transaction status in the database
        transactionRepository.updateTransaction(transactionStatus, transaction.getExternalTransactionId());

        // TODO - Fetch email addresses using sender and receiver ids or mobile numbers
//        String senderEmail = fetchEmailById(senderId);  // Implement this method
//        String receiverEmail = fetchEmailById(transactionCreateRequest.getReceiverId());  // Implement this method
        String senderEmail="sumitj00112233@gmail.com";
        String receiverEmail="sumitj00112233@gmail.com";

        // Create a JSON object to send via Kafka
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("transactionId", transaction.getExternalTransactionId());
        jsonObject.put("transactionStatus", transactionStatus);
        jsonObject.put("amount", transaction.getAmount());
        jsonObject.put("senderEmail", senderEmail);
        jsonObject.put("receiverEmail", receiverEmail);

        // Send a message to the Kafka topic
        kafkaTemplate.send("transaction_completed", objectMapper.writeValueAsString(jsonObject));
        return transaction.getExternalTransactionId();
    }

    // Mock method to fetch email by user ID, replace with actual implementation
//    private String fetchEmailById(String userId) {
//
//        return "sumit00112233@gamil.com";
//    }
}
