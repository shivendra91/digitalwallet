package com.anymind.bitcoin.service;

import com.anymind.bitcoin.dto.BitcoinWalletEntity;
import com.anymind.bitcoin.model.BitcoinWalletModel;
import com.anymind.bitcoin.repository.BitcoinWalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BitcoinWalletServiceImpl implements BitcoinWalletService {

    Logger logger = LoggerFactory.getLogger(BitcoinWalletServiceImpl.class);
    @Autowired
    private BitcoinWalletRepository walletRepository;

    @Override
    public String creditWallet(BitcoinWalletModel bitcoinWalletModel) {
        logger.info("Fetching latest balance and saving the current transaction with updated balance");
        Double currBalance = walletRepository.findLatestBalance();
        BitcoinWalletEntity walletEntity = convertModelToEntity(bitcoinWalletModel, currBalance);
        BitcoinWalletEntity savedRecord = walletRepository.save(walletEntity);
        return "Wallet credited successfully. Current balance:" + savedRecord.getBalance();
    }

    @Override
    public List<BitcoinWalletModel> fetchWalletBalance(LocalDateTime start, LocalDateTime end) {
        logger.info("Fetch all transactions happened in between start datetime and end datetime");
        List<BitcoinWalletEntity> transactions = walletRepository.getTransactions(start, end);
        logger.info("Group transactions based on dates and hours");
        Map<String, List<BitcoinWalletEntity>> transactionMap = groupTransactionsOnHours(start, end, transactions);
        logger.info("Generate hourly transaction details with latest balance");
        List<BitcoinWalletModel> hourlyWalletBalanceList = hourlyBalanceGeneration(transactionMap);
        return hourlyWalletBalanceList;
    }

    private BitcoinWalletEntity convertModelToEntity(BitcoinWalletModel model, Double currBalance) {
        BitcoinWalletEntity entity = new BitcoinWalletEntity();
        entity.setAmount(model.getAmount());
        entity.setCreditDate(model.getDateTime());
        entity.setBalance(currBalance + model.getAmount());
        return entity;
    }

    private Map<String, List<BitcoinWalletEntity>> groupTransactionsOnHours(LocalDateTime start, LocalDateTime end, List<BitcoinWalletEntity> entities) {
        long hours = timeDiff(start, end);
        Map<String, List<BitcoinWalletEntity>> transactionMap = new LinkedHashMap<>();
        Map<Integer, List<BitcoinWalletEntity>> dateGrouping = entities.stream().collect(Collectors.groupingBy(entity -> entity.getCreditDate().getDayOfMonth()));
        long num = start.getHour();
        for (List<BitcoinWalletEntity> filteredEntities : dateGrouping.values()) {
            String trDate = filteredEntities.get(0).getCreditDate().toString();
            for (int i = 1; i <= hours; i++) {
                long hour = num;
                List<BitcoinWalletEntity> filteredList = filteredEntities.stream().filter(entity -> entity.getCreditDate().getHour() == hour).collect(Collectors.toList());
                num++;
                if (num == 24) {
                    num = 0;
                    trDate = filteredEntities.get(0).getCreditDate().plusDays(1).toString();
                    transactionMap.put(trDate + "<>" + num, filteredList);
                    hours = hours - i;
                    break;
                }
                transactionMap.put(trDate + "<>" + num, filteredList);
            }
        }
        return transactionMap;
    }

    private List<BitcoinWalletModel> hourlyBalanceGeneration(Map<String, List<BitcoinWalletEntity>> groupTransactionsOnHours) {
        List<BitcoinWalletModel> hourlyWalletBalanceList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        Double lastBalance = 0.0;
        for (Map.Entry<String, List<BitcoinWalletEntity>> entry : groupTransactionsOnHours.entrySet()) {
            BitcoinWalletModel model = new BitcoinWalletModel();
            String[] key = entry.getKey().split("<>");
            String date = key[0].split("T")[0];
            key[1] = key[1].length() == 1 ? "0" + key[1] : key[1];
            LocalDateTime hourlyTime = LocalDateTime.parse(date + "T" + key[1] + ":00:00", formatter);
            Double walletBalance = entry.getValue().size() != 0 ?
                    entry.getValue().stream().mapToDouble(BitcoinWalletEntity::getBalance).max().getAsDouble() : lastBalance;
            model.setAmount(walletBalance);
            model.setDateTime(hourlyTime);
            hourlyWalletBalanceList.add(model);
            lastBalance = walletBalance;
        }
        return hourlyWalletBalanceList;
    }

    private long timeDiff(LocalDateTime d1, LocalDateTime d2) {
        return Duration.between(d1, d2).toHours();
    }
}
