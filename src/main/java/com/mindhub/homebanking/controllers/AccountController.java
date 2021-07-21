package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AccountController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return this.accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccountById(@PathVariable Long id){
        return this.accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    public int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max-min)) + min);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam String accountType ,Authentication authentication){
        Client clientAuth= clientRepository.findByEmail(authentication.getName());

        if (clientAuth.getAccounts().stream().filter(account -> account.isActive()).toArray().length >= 3){
            return new ResponseEntity<>("Max accounts",HttpStatus.FORBIDDEN);
        }
        else {
            Account account= new Account(accountType,"VIN"+ getRandomNumber(100000000, 90000000), LocalDateTime.now(), 0.0, true, clientAuth);
            accountRepository.save(account);
            return new ResponseEntity<>("Add Account",HttpStatus.CREATED);
        }
    }

    @PostMapping("/clients/current/account/delete")
    public ResponseEntity<Object> deleteAccount(@RequestParam Long id, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        if (id == 0){
            return new ResponseEntity<>("empty", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(accountRepository.getById(id))){
            return new ResponseEntity<>("card not client", HttpStatus.FORBIDDEN);
        }
        else {
            Account account = accountRepository.getById(id);
            account.setActive(false);
            accountRepository.save(account);
            /* para eliminar cuentas y transactiones, se tiene que elimicar primero las relaciones o usar cascade
            Set<Transaction> transactions = accountRepository.getById(id).getTransactions();
            for (Transaction tr: transactions) {
                transactionRepository.delete(tr);
            }
            accountRepository.delete(account);
            */
            return new ResponseEntity<>("delete", HttpStatus.OK);
        }
    }
}
