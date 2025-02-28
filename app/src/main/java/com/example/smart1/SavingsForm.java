package com.example.smart1;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SavingsForm extends AppCompatActivity {
    private EditText savings,goalEditText,noteEditText;
    private TextView save;
    private Button saveSavings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_savings_form);
        //find view by id
        savings=findViewById(R.id.savings);
        goalEditText=findViewById(R.id.goal);
        noteEditText=findViewById(R.id.note);
        save=findViewById(R.id.save);
        saveSavings=findViewById(R.id.saveSavings);

        saveSavings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savesavings();
            }
        });
    }
    private void savesavings(){
        String amountStr = savings.getText().toString().trim();
        String goal = goalEditText.getText().toString().trim();
        String note = noteEditText.getText().toString().trim();

        // Validate Input
        if (TextUtils.isEmpty(amountStr)) {
            savings.setError("Savings amount is required");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                savings.setError("Enter a valid amount");
                return;
            }
        } catch (NumberFormatException e) {
            savings.setError("Invalid amount format");
            return;
        }
    }
}