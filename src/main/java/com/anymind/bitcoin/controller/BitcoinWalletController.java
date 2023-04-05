package com.anymind.bitcoin.controller;

import com.anymind.bitcoin.model.BitcoinWalletModel;
import com.anymind.bitcoin.service.BitcoinWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BitcoinWalletController {

    Logger logger = LoggerFactory.getLogger(BitcoinWalletController.class);
    @Autowired
    BitcoinWalletService service;

    @PostMapping(value = "/creditwallet",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String creditWallet(@RequestBody BitcoinWalletModel bitcoinWalletModel) {
        logger.info("Rest API to add transaction record in database");
        return service.creditWallet(bitcoinWalletModel);
    }

    @GetMapping(value = "/walletbalance", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BitcoinWalletModel> fetchWalletBalance(@RequestParam String start, @RequestParam String end) {
        logger.info("Rest API to fetch wallet balance hourly basis");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        if (startDate.isAfter(endDate)) {
            logger.info("Start date can't be more than end date");
            return new ArrayList<>();
        }
        return service.fetchWalletBalance(startDate, endDate);
    }
}
