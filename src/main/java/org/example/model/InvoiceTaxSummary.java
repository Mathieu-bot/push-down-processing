package org.example.model;

import java.math.BigDecimal;

public class InvoiceTaxSummary {

    private final int id;
    private final BigDecimal totalHt;
    private final BigDecimal totalTva;
    private final BigDecimal totalTtc;

    public InvoiceTaxSummary(int id,
                             BigDecimal totalHt,
                             BigDecimal totalTva,
                             BigDecimal totalTtc) {
        this.id = id;
        this.totalHt = totalHt;
        this.totalTva = totalTva;
        this.totalTtc = totalTtc;
    }

    @Override
    public String toString() {
        return id + " | HT " + totalHt +
                " | TVA " + totalTva +
                " | TTC " + totalTtc;
    }
}
