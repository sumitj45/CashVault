package com.cashVault.transaction_service.repository;

import com.cashVault.transaction_service.models.Transaction;
import com.cashVault.transaction_service.models.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    @Transactional
    @Modifying
    @Query("update Transaction t set t.transactionStatus = ?1 where t.externalTransactionId = ?2")
    void updateTransaction(TransactionStatus transactionStatus, String externalTransactionId);
}
