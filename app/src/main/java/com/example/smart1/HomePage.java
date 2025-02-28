package com.example.smart1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomePage extends AppCompatActivity {
    private Button incomeButton, expensesButton, savingsButton, reportButton;
    private TextView hpageTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);


            //find views by id
            incomeButton=findViewById(R.id.income);
            expensesButton=findViewById(R.id.expenses);
            savingsButton=findViewById(R.id.savings);
            reportButton=findViewById(R.id.report);
            hpageTextview=findViewById(R.id.hpage);
            //set onclick listener
            incomeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomePage.this,homeActivity1.class));
                }
            });
        expensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,ExpensesForm.class));
            }
        });
        savingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,SavingsForm.class));
            }

        });
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,ReportsActivity.class));
            }
        });


    }
}