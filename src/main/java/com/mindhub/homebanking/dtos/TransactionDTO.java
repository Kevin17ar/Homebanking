package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class TransactionDTO {
    private long id;

    private TransactionType type;
    private double amount;
    private String description;
    private LocalDate transactionDate;
    private double balance;

    public TransactionDTO() {
    }

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.transactionDate = transaction.getTransactionDate();
        this.balance = transaction.getBalance();
    }
    /*
    public double calBalance(double amount, Account account){
        double balance = account.getBalance() + amount;
        return balance;
    }*/
}
