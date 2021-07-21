package com.mindhub.homebanking.dtos;

import lombok.Data;

@Data
public class LoanApplicationDTO {

    private Long id;
    private double amount;
    private int payments;
    private String number;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(Long id, double amount, int payments, String number) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.number = number;
    }
}
