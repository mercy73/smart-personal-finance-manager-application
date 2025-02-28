package com.example.smart1;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ExpensesForm extends AppCompatActivity {
private EditText expenseEditText,noteEditText;
private TextView newExpenseTextView;
private Spinner categorySpinner;
private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expenses_form);


            //find views by id
        expenseEditText=findViewById(R.id.amount);
        noteEditText=findViewById(R.id.note);
        newExpenseTextView=findViewById(R.id.ane);
        categorySpinner=findViewById(R.id.category);
        saveButton=findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveExpense();
            }
        });



    }

    private void saveExpense() {
        String amount = expenseEditText.getText().toString().trim();
        String note = noteEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        // Validate input
        if (TextUtils.isEmpty(amount)) {
            expenseEditText.setError("Amount is required");
            return;
        }

        double expenseValue;
        try {
            expenseValue = Double.parseDouble(amount);
            if (expenseValue <= 0) {
                expenseEditText.setError("Enter a valid amount");
                return;
            }
        } catch (NumberFormatException e) {
            expenseEditText.setError("Invalid amount format");
            return;
        }
        // In onCreate(), after findViewById:
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.expense_categories,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Here, you can store data in Firestore or SQLite
        Toast.makeText(this, "expense Saved: " + amount + " from " + category, Toast.LENGTH_SHORT).show();





    }
}