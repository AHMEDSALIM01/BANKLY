package org.bankly.transactionservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WalletResponseDto {
    private String id;
    private Double balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
}
