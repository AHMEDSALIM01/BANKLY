package org.bankly.walletservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document(collection = "wallet")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Wallet implements Serializable {
    @MongoId
    private String id;
    private Double balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
}
