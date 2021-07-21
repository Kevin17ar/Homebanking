package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PayCardDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.utils.CardUtils;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Transactional
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<Object> transfer(@RequestParam double amount, @RequestParam String description, @RequestParam String numberOrigin, @RequestParam String numberDestiny, Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());

        if (description.isEmpty() || numberOrigin.isEmpty() || numberDestiny.isEmpty() || amount == 0){
            return new ResponseEntity<>("empty",HttpStatus.FORBIDDEN);
        }
        if (numberDestiny == numberOrigin){
            return new ResponseEntity<>("number is equal", HttpStatus.FORBIDDEN);
        }
        if (accountRepository.findByNumber(numberOrigin) == null || accountRepository.findByNumber(numberDestiny) == null){
            return new ResponseEntity<>("error existencia",HttpStatus.FORBIDDEN);
        }
        if ( !client.getAccounts().contains(accountRepository.findByNumber(numberOrigin))){
            return  new ResponseEntity<>("search account origin",HttpStatus.FORBIDDEN);
        }
        if (accountRepository.findByNumber(numberOrigin).getBalance() < amount ){
            return new ResponseEntity<>("balance",HttpStatus.FORBIDDEN);
        }
        else {
            double balanceD = accountRepository.findByNumber(numberOrigin).getBalance() - amount;
            double balanceC = accountRepository.findByNumber(numberDestiny).getBalance() + amount;

            accountRepository.findByNumber(numberOrigin).setBalance(balanceD);
            accountRepository.findByNumber(numberDestiny).setBalance(balanceC);

            Transaction transactionD = new Transaction(TransactionType.DEBIT, - amount, description , LocalDate.now(), balanceD,accountRepository.findByNumber(numberOrigin));
            Transaction transactionC = new Transaction(TransactionType.CREDIT, amount, description, LocalDate.now(), balanceC,accountRepository.findByNumber(numberDestiny));

            transactionRepository.save(transactionD);
            transactionRepository.save(transactionC);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
    }

    @Transactional
    @PostMapping("/clients/pay")
    public ResponseEntity<Object> payCard(@RequestBody PayCardDTO payCardDTO){
        Card card = cardRepository.findByNumber(payCardDTO.getNumber());
        Account account = card.getClient().getAccounts().stream().findFirst().get();

        if (payCardDTO.getNumber().isEmpty() || payCardDTO.getCvv() == 0 || payCardDTO.getAmount() == 0 || payCardDTO.getDescription().isEmpty()){
            return new ResponseEntity<>("empty", HttpStatus.BAD_REQUEST);
        }
        if (card == null){
            return new ResponseEntity<>("card no exist", HttpStatus.FORBIDDEN);
        }
        if (payCardDTO.getCvv() != card.getCvv()){
            return new ResponseEntity<>("cvv invalido", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() < payCardDTO.getAmount()){
            return new ResponseEntity<>("purchase limit", HttpStatus.FORBIDDEN);
        }
        else {
            double balanceD = account.getBalance() - payCardDTO.getAmount();
            account.setBalance(balanceD);
            accountRepository.save(account);

            Transaction transaction = new Transaction(TransactionType.DEBIT, payCardDTO.getAmount(), payCardDTO.getDescription(), LocalDate.now(), balanceD, account);
            transactionRepository.save(transaction);

            return new ResponseEntity<>("pay accepted",HttpStatus.ACCEPTED);
        }
    }

    @PostMapping("/clients/filter")
    public  ResponseEntity<?> getTransactionFilter(@RequestParam Long id, @RequestParam String star, @RequestParam String end){
        Account account = accountRepository.findById(id).get();

        List<Transaction> transactionList = transactionRepository.findByTransactionDateBetween(LocalDate.parse(star), LocalDate.parse(end));

        List<TransactionDTO> transactionDTOS = transactionList.stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());

        List<TransactionDTO>  transactionDTOS2 = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());

        List<TransactionDTO>  transactionDTOS1 = transactionDTOS.stream().filter(tr -> transactionDTOS2.contains(tr)).collect(Collectors.toList());

        return new ResponseEntity<>(transactionDTOS1, HttpStatus.OK);
    }
}