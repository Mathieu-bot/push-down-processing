package org.example.model;

import java.math.BigDecimal;

public class InvoiceStatusTotals {

    private final BigDecimal totalPaid;
    private final BigDecimal totalConfirmed;
    private final BigDecimal totalDraft;

    public InvoiceStatusTotals(BigDecimal totalPaid,
                               BigDecimal totalConfirmed,
                               BigDecimal totalDraft) {
        this.totalPaid = totalPaid;
        this.totalConfirmed = totalConfirmed;
        this.totalDraft = totalDraft;
    }

    @Override
    public String toString() {
        return "total_paid = " + totalPaid +
                "\ntotal_confirmed = " + totalConfirmed +
                "\ntotal_draft = " + totalDraft;
    }
}
