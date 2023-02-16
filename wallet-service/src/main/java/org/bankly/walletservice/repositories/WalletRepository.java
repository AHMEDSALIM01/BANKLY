package org.bankly.walletservice.repositories;

import org.bankly.walletservice.entities.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends MongoRepository<Wallet, String> {
}
