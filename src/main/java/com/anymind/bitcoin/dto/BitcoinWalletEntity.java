package com.anymind.bitcoin.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "digital_wallet")
public class BitcoinWalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private Double amount;

    @Column(name = "credit_date")
    private LocalDateTime creditDate;

    @Column
    private Double balance;
}
