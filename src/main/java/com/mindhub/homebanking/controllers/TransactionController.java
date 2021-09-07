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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
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

    @PostMapping("/clients/current/transactions")
    public ResponseEntity<?> transfer(@RequestParam double amount, @RequestParam String description, @RequestParam String numberOrigin, @RequestParam String numberDestiny, Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());

        if (description.isEmpty() || numberOrigin.isEmpty() || numberDestiny.isEmpty() || amount < 0){
            return new ResponseEntity<>("empty",HttpStatus.FORBIDDEN);
        }
        if (numberDestiny.equals(numberOrigin)){
            return new ResponseEntity<>("number is equal", HttpStatus.FORBIDDEN);
        }
        if (accountRepository.findByNumber(numberOrigin) == null || accountRepository.findByNumber(numberDestiny) == null){
            return new ResponseEntity<>("error existencia",HttpStatus.FORBIDDEN);
        }
        if ( !client.getAccounts().contains(accountRepository.findByNumber(numberOrigin))){
            return  new ResponseEntity<>("search account origin",HttpStatus.FORBIDDEN);
        }
        if (accountRepository.findByNumber(numberOrigin).getBalance() < amount ){
            return new ResponseEntity<>("amount not allowed",HttpStatus.FORBIDDEN);
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

    @PostMapping("/clients/pay")
    public ResponseEntity<?> payCard(@RequestBody PayCardDTO payCardDTO){
        if (payCardDTO.getCardHolder().isEmpty() || payCardDTO.getCardNumber().isEmpty() || payCardDTO.getCvv() == 0 || payCardDTO.getAmount() == 0 || payCardDTO.getDescription().isEmpty()){
            return new ResponseEntity<>("empty", HttpStatus.BAD_REQUEST);
        }

        Card card = cardRepository.findByNumber(payCardDTO.getCardNumber());

        if (card == null){
            return new ResponseEntity<>("card no exist", HttpStatus.FORBIDDEN);
        }
        Account account = card.getClient().getAccounts().stream().findFirst().orElse(null);
        if (account == null){
            return new ResponseEntity<>("account no exist", HttpStatus.FORBIDDEN);
        }
;       if(!account.getAccountType().equals("Caja de Ahorro")){
            return new ResponseEntity<>("is no account type ahorro", HttpStatus.FORBIDDEN);
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
        Account account = accountRepository.findById(id).orElse(null);

        List<Transaction> transactionList = transactionRepository.findByTransactionDateBetween(LocalDate.parse(star), LocalDate.parse(end));

        List<TransactionDTO> transactionDTOS = transactionList.stream().map(TransactionDTO::new).collect(Collectors.toList());

        List<TransactionDTO>  transactionDTOS2 = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());

        List<TransactionDTO>  transactionDTOS1 = transactionDTOS.stream().filter(transactionDTOS2::contains).collect(Collectors.toList());

        return new ResponseEntity<>(transactionDTOS1, HttpStatus.OK);
    }

    @PostMapping("/clients/filter/description")
    public ResponseEntity<?> getTransactionByString(@RequestParam Long id ,@RequestParam String word){
        Account account = accountRepository.findById(id).orElse(null);

        if (account != null){
            List<TransactionDTO> transactions = transactionRepository.findByDescriptionContaining(word).stream().map(TransactionDTO::new).collect(Collectors.toList());

            List<TransactionDTO> transactionDTOList = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());

            List<TransactionDTO> transactionDTOList1 = transactionDTOList.stream().filter(transactions::contains).collect(Collectors.toList());

            return new ResponseEntity<>(transactionDTOList1, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }
}
