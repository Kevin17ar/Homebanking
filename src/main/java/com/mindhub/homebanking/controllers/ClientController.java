package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return this.clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
        //return this.clientRepository.findById(id).stream().map(client -> new ClientDTO(client).collect(Collectors.toList));
    }

    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        Client clientCurrent = clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(clientCurrent);
    }



    public int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max-min)) + min);
    }


    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password){

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() ) {
            return new ResponseEntity<>("Complete registration",HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) != null){
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        else{
            Client clientNew= clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
            Account accountNew= accountRepository.save(new Account("Caja de Ahorro","VN"+ getRandomNumber(10000000, 99999999), LocalDateTime.now(), 0 ,true, clientNew));
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

}
