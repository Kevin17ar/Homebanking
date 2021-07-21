package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import lombok.Data;

import java.util.Set;

@Data
public class ClientLoanDTO {
    private Long id;
    private Long idLoan;
    private String name;
    private double amount;
    private int payments;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.idLoan = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }
}
