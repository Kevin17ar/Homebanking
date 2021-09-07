package com.mindhub.homebanking.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayCardDTO {
    private String cardHolder;
    private String cardNumber;
    private int cvv;
    private double amount;
    private String description;

    public PayCardDTO(String cardHolder , String cardNumber, int cvv, double amount, String description) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }
}
