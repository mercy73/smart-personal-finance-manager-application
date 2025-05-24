package com.example.smart1;

public class FinancialTip {
    private String title;
    private String description;

    public FinancialTip(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
