package com.cashVault.transaction_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Data
public class TransactionCreateRequest {



    @NotBlank
    private String receiverId;

    @Min(1)
    private Long amount;

    private String reason;


}
