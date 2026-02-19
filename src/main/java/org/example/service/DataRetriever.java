package org.example.service;

import org.example.model.*;
import org.example.database.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    private final DBConnection dbConnection;

    public DataRetriever(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // Q1
    public List<InvoiceTotal> findInvoiceTotals() {
        String sql = """
            SELECT i.id, i.customer_name, i.status,
                   SUM(il.quantity * il.unit_price) AS total
            FROM invoice i
            JOIN invoice_line il ON il.invoice_id = i.id
            GROUP BY i.id, i.customer_name, i.status
            ORDER BY i.id
        """;

        List<InvoiceTotal> results = new ArrayList<>();
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new InvoiceTotal(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        InvoiceStatus.valueOf(rs.getString("status")),
                        rs.getBigDecimal("total")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching invoice totals", e);
        }

        return results;
    }

    // Q2
    public List<InvoiceTotal> findConfirmedAndPaidInvoiceTotals() {
        String sql = """
            SELECT i.id, i.customer_name, i.status,
                   SUM(il.quantity * il.unit_price) AS total
            FROM invoice i
            JOIN invoice_line il ON il.invoice_id = i.id
            WHERE i.status IN ('CONFIRMED', 'PAID')
            GROUP BY i.id, i.customer_name, i.status
            ORDER BY i.id
        """;

        List<InvoiceTotal> results = new ArrayList<>();
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new InvoiceTotal(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        InvoiceStatus.valueOf(rs.getString("status")),
                        rs.getBigDecimal("total")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching confirmed/paid invoice totals", e);
        }

        return results;
    }

    // Q3
    public InvoiceStatusTotals computeStatusTotals() {
        String sql = """
            SELECT
                SUM(CASE WHEN i.status = 'PAID' THEN il.quantity * il.unit_price ELSE 0 END) AS total_paid,
                SUM(CASE WHEN i.status = 'CONFIRMED' THEN il.quantity * il.unit_price ELSE 0 END) AS total_confirmed,
                SUM(CASE WHEN i.status = 'DRAFT' THEN il.quantity * il.unit_price ELSE 0 END) AS total_draft
            FROM invoice i
            JOIN invoice_line il ON il.invoice_id = i.id
        """;

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new InvoiceStatusTotals(
                        rs.getBigDecimal("total_paid"),
                        rs.getBigDecimal("total_confirmed"),
                        rs.getBigDecimal("total_draft")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error computing status totals", e);
        }

        return null;
    }

    // Q4
    public Double computeWeightedTurnover() {
        String sql = """
            SELECT SUM(
                (il.quantity * il.unit_price) *
                CASE
                    WHEN i.status = 'PAID' THEN 1
                    WHEN i.status = 'CONFIRMED' THEN 0.5
                    ELSE 
                        CASE 
                            WHEN i.status = 'DRAFT' 
                            THEN il.quantity * il.unit_price * 0 
                            ELSE 0 
                        END
                END
            ) AS weighted_turnover
            FROM invoice i
            JOIN invoice_line il ON il.invoice_id = i.id
        """;

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("weighted_turnover");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error computing weighted turnover", e);
        }

        return 0.0;
    }

    // Q5-A
    public List<InvoiceTaxSummary> findInvoiceTaxSummaries() {
        String sql = """
            SELECT\s
                i.id,
                SUM(il.quantity * il.unit_price) AS total_ht,
                SUM(il.quantity * il.unit_price) * (t.rate / 100) AS total_tva,
                SUM(il.quantity * il.unit_price) * (1 + t.rate / 100) AS total_ttc
            FROM invoice i
            JOIN invoice_line il ON il.invoice_id = i.id
            CROSS JOIN tax_config t
            GROUP BY i.id, t.rate
            ORDER BY i.id
       \s""";

        List<InvoiceTaxSummary> results = new ArrayList<>();
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new InvoiceTaxSummary(
                        rs.getInt("id"),
                        rs.getBigDecimal("total_ht"),
                        rs.getBigDecimal("total_tva"),
                        rs.getBigDecimal("total_ttc")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching invoice tax summaries", e);
        }

        return results;
    }

    // Q5-B
    public BigDecimal computeWeightedTurnoverTtc() {
        String sql = """
            SELECT SUM(
                (il.quantity * il.unit_price) * (1 + t.rate / 100) *
                CASE
                    WHEN i.status = 'PAID' THEN 1
                    WHEN i.status = 'CONFIRMED' THEN 0.5
                    ELSE 0
                END
            ) AS weighted_turnover_ttc
            FROM invoice i
            JOIN invoice_line il ON il.invoice_id = i.id
            CROSS JOIN tax_config t
        """;

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal("weighted_turnover_ttc");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error computing weighted TTC turnover", e);
        }

        return BigDecimal.ZERO;
    }
}
