package org.example.model;

import java.math.BigDecimal;

public class InvoiceTotal {

    private final int id;
    private final String customerName;
    private final InvoiceStatus status;
    private final BigDecimal total;

    public InvoiceTotal(int id, String customerName, InvoiceStatus status, BigDecimal total) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.total = total;
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public InvoiceStatus getStatus() { return status; }
    public BigDecimal getTotal() { return total; }

    @Override
    public String toString() {
        return id + " | " + customerName + " | " + status + " | " + total;
    }
}
