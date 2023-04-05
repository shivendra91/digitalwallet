package com.anymind.bitcoin.repository;

import com.anymind.bitcoin.dto.BitcoinWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BitcoinWalletRepository extends JpaRepository<BitcoinWalletEntity, Integer> {

    @Query(value = "select balance from digital_wallet where id = (select max(id) from digital_wallet)", nativeQuery = true)
    Double findLatestBalance();

    @Query(value = "select * from digital_wallet e where e.credit_date >= :start and e.credit_date <= :end", nativeQuery = true)
    List<BitcoinWalletEntity> getTransactions(LocalDateTime start, LocalDateTime end);
}
