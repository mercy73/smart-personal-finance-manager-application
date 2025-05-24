package com.example.smart1;

import java.util.HashMap;
import java.util.Map;

public class Expense {
    private String id;
    private double amount;
    private String category;
    private String note;

    // Empty constructor required for Firestore
    public Expense() {
    }

    public Expense(String id, double amount, String category, String note) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getNote() {
        return note;
    }

    // Convert Expense to a Map for Firestore
    public Map<String, Object> toMap() {
        Map<String, Object> expenseMap = new HashMap<>();
        expenseMap.put("amount", amount);
        expenseMap.put("category", category);
        expenseMap.put("note", note);
        return expenseMap;
    }
}
