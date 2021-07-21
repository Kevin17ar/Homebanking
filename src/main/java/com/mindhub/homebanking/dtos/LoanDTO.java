package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LoanDTO {
    private Long id;
    private String name;
    private double maxAmount;
    private double interest;
    private List<Integer> payments = new ArrayList<>();

    public LoanDTO() {
    }

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.interest = loan.getInterest();
        this.payments = loan.getPayments();
    }
}
