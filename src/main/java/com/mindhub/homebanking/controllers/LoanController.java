package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LoanController {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    LocalDateTime today= LocalDateTime.now();

    DateTimeFormatter todayFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy / HH:mm");
    String formattedDate = today.format(todayFormat);

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    private Map<String, Object> makeMap(Object value){
        Map<String, Object> map = new HashMap<>();
        map.put("error", value);
        return  map;
    }

    @Transactional
    @PostMapping("/clients/current/loans")
    public ResponseEntity<?> requestLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findById(loanApplicationDTO.getId()).get();

        if (loanApplicationDTO.getAmount() == 0 || loanApplicationDTO.getPayments() == 0){
            return new ResponseEntity<>("empty", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(accountRepository.findByNumber(loanApplicationDTO.getNumber()))){
            return new ResponseEntity<>("exists account destiny", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getId() == 0){
            return new ResponseEntity<>("no exists", HttpStatus.FORBIDDEN);
        }
        if ( loan.getMaxAmount() < loanApplicationDTO.getAmount()){
            return new ResponseEntity<>(makeMap("maximo"), HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("payment not available", HttpStatus.FORBIDDEN);
        }
        else{
            double interest = (loanRepository.findById(loanApplicationDTO.getId()).get().getInterest() / 100 ) +1;

            double balance = accountRepository.findByNumber(loanApplicationDTO.getNumber()).getBalance() + loanApplicationDTO.getAmount() * interest;
            accountRepository.findByNumber(loanApplicationDTO.getNumber()).setBalance(balance);

            ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * interest, loanApplicationDTO.getPayments(), client, loan);
            Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount() * interest, loan.getName() + "-loan approved", LocalDate.now(), balance,accountRepository.findByNumber(loanApplicationDTO.getNumber()));

            transactionRepository.save(transaction);
            clientLoanRepository.save(clientLoan);

            return new ResponseEntity<>("loan exists", HttpStatus.CREATED);
        }
    }

    @PostMapping("/clients/loans")
    public ResponseEntity<Object> createLoan(@RequestParam String name, @RequestParam double maxAmount, @RequestParam double interest, @RequestParam String payments){
        if (name.isEmpty() || maxAmount == 0 || interest == 0 || payments.isEmpty() ){
            return new ResponseEntity<>("empty", HttpStatus.FORBIDDEN);
        }
        else {
            String str[] = payments.split("-");
            List<Integer> paymentsList = Arrays.stream(str).map(s -> Integer.parseInt(s)).collect(Collectors.toList());

            Loan loan = new Loan(name, maxAmount, interest, paymentsList);
            loanRepository.save(loan);
            return new ResponseEntity<>("create", HttpStatus.CREATED);
        }
    }
}
