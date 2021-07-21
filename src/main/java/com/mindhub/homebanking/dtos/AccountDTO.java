package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class AccountDTO {

    private long id;
    private String accountType;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private boolean active;
    private Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.accountType = account.getAccountType();
        this.active = account.isActive();
        this.transactions = account.getTransactions().stream().map(tr-> new TransactionDTO(tr)).collect(Collectors.toSet());
    }

    //public double balanceAct(Set<Transaction> transactions, double balance){

    //   for (Transaction transaction: transactions) {
    //balance += transaction.getAmount();
    //    }
    //    return balance;
    //}
}
