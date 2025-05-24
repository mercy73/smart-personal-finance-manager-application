package com.example.smart1;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class dashboard extends AppCompatActivity {

    private TextView totalIncomeTextView, totalExpensesTextView, totalSavingsTextView, balanceTextView;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        // Enable back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Find views by ID
        totalIncomeTextView = findViewById(R.id.ttlIncome);
        totalExpensesTextView = findViewById(R.id.ttlExpenses);
        totalSavingsTextView = findViewById(R.id.ttlSaving);
        balanceTextView = findViewById(R.id.balance);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Load data from Firestore
        loadDashboardData();
    }

    // Handle back navigation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDashboardData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();

        // Fetch data from Firestore
        fetchCollectionTotal("income", userId, totalIncomeTextView, "Total Income: Ksh ");
        fetchCollectionTotal("expenses", userId, totalExpensesTextView, "Total Expenses: Ksh ");
        fetchCollectionTotal("savings", userId, totalSavingsTextView, "Total Savings: Ksh ");
    }

    private void fetchCollectionTotal(String collectionName, String userId, TextView textView, String label) {
        CollectionReference collectionRef = db.collection("users").document(userId).collection(collectionName);

        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                double total = 0.0;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.contains("amount")) {
                        try {
                            total += document.getDouble("amount");
                        } catch (Exception e) {
                            Toast.makeText(this, "Error reading amount", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                textView.setText(label + String.format("%.2f", total));

                // Update balance after all collections are fetched
                updateBalance();

            } else {
                Toast.makeText(this, "Error loading " + collectionName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBalance() {
        try {
            double income = parseAmount(totalIncomeTextView.getText().toString());
            double expenses = parseAmount(totalExpensesTextView.getText().toString());
            double savings = parseAmount(totalSavingsTextView.getText().toString());

            double balance = income - expenses - savings;
            balanceTextView.setText("Balance: KES " + String.format("%.2f", balance));

        } catch (NumberFormatException e) {
            balanceTextView.setText("Balance: Error");
            Toast.makeText(this, "Error calculating balance", Toast.LENGTH_SHORT).show();
        }
    }

    private double parseAmount(String text) {
        try {
            return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
