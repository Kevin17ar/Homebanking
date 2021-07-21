package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    LocalDate todayCard= LocalDate.now();

    DateTimeFormatter todayFormatCard = DateTimeFormatter.ofPattern("MM-yy");
    String formattedDateCard = todayCard.format(todayFormatCard);
    String dueDate = todayCard.plusYears(5).format(todayFormatCard);


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType type, @RequestParam CardColor color, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getCards().stream().filter(card -> card.getType() == type).toArray().length >= 3){
            return new ResponseEntity<>("Max cards", HttpStatus.FORBIDDEN);
        }
        else {
            String number = CardUtils.getNumber();

            Card card = new Card(type, color, number, CardUtils.getRandomNumber(100,999), CardUtils.getDateFormatCard(), dueDate, client);
            cardRepository.save(card);
            return new ResponseEntity<>("Add Card", HttpStatus.CREATED);
        }

    }

    @PostMapping("/clients/current/card")
    public ResponseEntity<Object> deleteCard(@RequestParam Long id,Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());

        if (id == 0){
            return new ResponseEntity<>("Empty", HttpStatus.FORBIDDEN);
        }
        if (!client.getCards().contains(cardRepository.findById(id))){
            return new ResponseEntity<>("card not client", HttpStatus.FORBIDDEN);
        } else {
            cardRepository.deleteById(id);
            return new ResponseEntity<>("card delete", HttpStatus.ACCEPTED);
        }
    }



}
