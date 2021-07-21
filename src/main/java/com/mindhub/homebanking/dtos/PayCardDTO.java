package com.mindhub.homebanking.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PayCardDTO {
    private String number;
    private int cvv;
    private double amount;
    private String description;

    public PayCardDTO() {
    }

    public PayCardDTO(String number, int cvv, double amount, String description) {
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }
}
