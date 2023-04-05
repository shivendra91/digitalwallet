package com.anymind.bitcoin.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BitcoinWalletModel {

    private Double amount;
    private LocalDateTime dateTime;

}
