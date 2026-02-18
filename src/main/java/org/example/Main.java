package org.example;

import org.example.service.DataRetriever;
import org.example.database.DBConnection;

public class Main {

    public static void main(String[] args) {

        DBConnection dbConnection = new DBConnection();
        DataRetriever service = new DataRetriever(dbConnection);

        System.out.println("=== Q1 - Total par facture ===");
        service.findInvoiceTotals().forEach(System.out::println);

        System.out.println("\n=== Q2 - Factures CONFIRMED et PAID ===");
        service.findConfirmedAndPaidInvoiceTotals().forEach(System.out::println);

        System.out.println("\n=== Q3 - Totaux cumulés par statut ===");
        System.out.println(service.computeStatusTotals());

        System.out.println("\n=== Q4 - Chiffre d’affaires pondéré HT ===");
        System.out.println(service.computeWeightedTurnover());

        System.out.println("\n=== Q5-A - Totaux HT, TVA et TTC ===");
        service.findInvoiceTaxSummaries().forEach(System.out::println);

        System.out.println("\n=== Q5-B - Chiffre d’affaires TTC pondéré ===");
        System.out.println(service.computeWeightedTurnoverTtc());
    }
}
