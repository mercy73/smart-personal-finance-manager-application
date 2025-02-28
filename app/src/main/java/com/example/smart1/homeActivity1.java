package com.example.smart1;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Spinner;

import java.util.Calendar;

public class homeActivity1 extends AppCompatActivity {
    private EditText amountEditText,dateEditText;
    private Spinner sourceSpinner;
    private Button saveButton;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home1);
        //initialize find view by id
        amountEditText=findViewById(R.id.amount);
        dateEditText=findViewById(R.id.date);
        sourceSpinner=findViewById(R.id.source);
        saveButton=findViewById(R.id.save);

        //date picker
        dateEditText.setOnClickListener(view -> showDatePicker());
        //save button
        saveButton.setOnClickListener(view -> saveIncomeData());

        // Setup Spinner with two options
        String[] sourceOptions = {"Mpesa", "Bank"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sourceOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpinner.setAdapter(adapter);






    }

    private void showDatePicker() {
        Calendar Calendar = java.util.Calendar.getInstance();
        int year = Calendar.get(Calendar.YEAR);
        int month = Calendar.get(Calendar.MONTH);
        int day = Calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    dateEditText.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();

    }
    private void saveIncomeData(){
        String amountStr = amountEditText.getText().toString().trim();
        String source = sourceSpinner.getSelectedItem().toString();
        String date = dateEditText.getText().toString().trim();
        // Validation
        if (amountStr.isEmpty() || source.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }
        double amount = Double.parseDouble(amountStr);
        if (amount <= 0) {
            Toast.makeText(this, "Enter a valid amount!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Here, you can store data in Firestore or SQLite
        Toast.makeText(this, "Income Saved: " + amount + " from " + source, Toast.LENGTH_SHORT).show();

    }

}






