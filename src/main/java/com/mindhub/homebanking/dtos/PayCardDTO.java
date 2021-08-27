package com.mindhub.homebanking.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PayCardDTO {
    private String cardNumber;
    private int cvv;
    private double amount;
    private String description;

    public PayCardDTO() {
    }

    public PayCardDTO(String cardNumber, int cvv, double amount, String description) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }
}
