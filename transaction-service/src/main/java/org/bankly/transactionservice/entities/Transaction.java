package org.bankly.transactionservice.entities;

import org.bankly.transactionservice.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String walletId;

    private Double amount;

    @Enumerated(value = EnumType.STRING)
    private TransactionType type;

    private LocalDateTime createdAt;

}
