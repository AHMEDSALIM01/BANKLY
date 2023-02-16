package org.bankly.transactionservice.dtos;

import org.bankly.transactionservice.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequestDto {
    private String walletId;
    private Double amount;
    @Enumerated(value = EnumType.STRING)
    private TransactionType type;
}
