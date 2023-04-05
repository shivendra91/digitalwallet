package com.anymind.bitcoin.service;

import com.anymind.bitcoin.model.BitcoinWalletModel;

import java.time.LocalDateTime;
import java.util.List;

public interface BitcoinWalletService {

    String creditWallet(BitcoinWalletModel bitcoinWalletModel);

    List<BitcoinWalletModel> fetchWalletBalance(LocalDateTime start, LocalDateTime end);
}
