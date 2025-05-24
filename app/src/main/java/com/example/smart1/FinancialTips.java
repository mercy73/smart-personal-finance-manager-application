package com.example.smart1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FinancialTips extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FinanceTipsAdapter adapter;
    private List<FinancialTip> tipsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_tips);

        recyclerView = findViewById(R.id.recyclerViewTips);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tipsList = new ArrayList<>();
        tipsList.add(new FinancialTip("Diversify Your Income", "Relying on one income source is risky. Consider side gigs, or small investments."));
        tipsList.add(new FinancialTip("Track Your Spending", "Start by categorizing your spending to see where your money goes. Awareness is the first step to control."));
        tipsList.add(new FinancialTip("Follow the 50/30/20 Rule", "Spend 50% on needs, 30% on wants, and save 20% of your income."));
        tipsList.add(new FinancialTip("Build an Emergency Fund", "Set aside at least 3-6 months’ worth of expenses in case of job loss or emergencies."));
        tipsList.add(new FinancialTip("Set SMART Financial Goals", "Goals should be Specific, Measurable, Achievable, Relevant, and Time-bound."));
        tipsList.add(new FinancialTip("Understand Your Credit Score", "A good credit score helps you get lower interest rates. Pay bills on time and avoid unnecessary debt."));
        tipsList.add(new FinancialTip("Pay Yourself First", "Before spending, put money into your savings. Automate this if possible."));
        tipsList.add(new FinancialTip("Avoid Lifestyle Inflation", "Just because you earn more doesn’t mean you should spend more. Keep expenses steady and save the difference."));
        tipsList.add(new FinancialTip("Invest Early and Consistently", "Even small amounts invested regularly can grow significantly over time due to compound interest."));
        tipsList.add(new FinancialTip("Review Subscriptions Monthly", "Cancel any subscriptions or memberships you no longer use or need."));
        tipsList.add(new FinancialTip("Learn About Interest Rates", "Understand how interest works on savings, loans, and credit cards to make better financial decisions."));
        tipsList.add(new FinancialTip("Practice Delayed Gratification", "If you want to buy something non-essential, wait a few days. If you still want it, buy it. If not, you saved money."));



        adapter = new FinanceTipsAdapter(tipsList);
        recyclerView.setAdapter(adapter);
    }
}
