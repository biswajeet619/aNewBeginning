package org.hitam.epics.biswajeet.anewbeginning.support;

import java.util.Map;

public class RazorOrderRequest {
    private Double amount;
    private String currency="INR";
    private int payment_capture=1;
    private Map<String, String> notes;

    public RazorOrderRequest(Double amount) {
        this.amount = amount;
    }

    public RazorOrderRequest(Double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public RazorOrderRequest(Double amount, String currency, Map<String, String> notes) {
        this.amount = amount;
        this.currency = currency;
        this.notes = notes;
    }

    public RazorOrderRequest(Double amount, String currency, int payment_capture) {
        this.amount = amount;
        this.currency = currency;
        this.payment_capture = payment_capture;
    }

    public RazorOrderRequest(Double amount, String currency, int payment_capture, Map<String, String> notes) {
        this.amount = amount;
        this.currency = currency;
        this.payment_capture = payment_capture;
        this.notes = notes;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getPayment_capture() {
        return payment_capture;
    }

    public void setPayment_capture(int payment_capture) {
        this.payment_capture = payment_capture;
    }

    public Map<String, String> getNotes() {
        return notes;
    }

    public void setNotes(Map<String, String> notes) {
        this.notes = notes;
    }
}
