package org.bankly.transactionservice.dtos;

import org.bankly.transactionservice.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponseDto {
    private Long id;

    private String walletId;

    private Double amount;
    @Enumerated(value = EnumType.STRING)
    private TransactionType type;

    private LocalDateTime createdAt;

}
