package com.example.smart1;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BudgetForm extends AppCompatActivity {
    private TextView currentBudget, spendingText;
    private EditText budgetInput;
    private Button saveBudgetButton;
    private ProgressBar budgetProgressBar;
    private RadioGroup budgetModeGroup;
    private RadioButton manualBudget, aiBudget;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;
    private String selectedMonthYear;
    private double budgetAmount = 0.0;
    private double totalExpenses = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_form);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI elements
        currentBudget = findViewById(R.id.currentBudget);
        spendingText = findViewById(R.id.spendingText);
        budgetInput = findViewById(R.id.budgetInput);
        saveBudgetButton = findViewById(R.id.saveBudgetButton);
        budgetProgressBar = findViewById(R.id.budgetProgressBar);
        budgetModeGroup = findViewById(R.id.budgetModeGroup);
        manualBudget = findViewById(R.id.manualBudget);
        aiBudget = findViewById(R.id.aiBudget);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        selectedMonthYear = dateFormat.format(calendar.getTime());

        // Load current budget and expenses for the month
        loadBudget();
        loadExpenses();

        // Set a listener for AI toggle. When AI mode is selected, automatically populate the suggestion.
        aiBudget.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                suggestBudget();
            } else {
                budgetInput.setText(""); // Clear input when switching to manual
            }
        });


        saveBudgetButton.setOnClickListener(v -> saveBudget());
    }

    // Load the budget saved in Firestore for the selected month
    private void loadBudget() {
        DocumentReference budgetRef = db.collection("users")
                .document(userId)
                .collection("budget")
                .document(selectedMonthYear);

        budgetRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("amount")) {
                budgetAmount = documentSnapshot.getDouble("amount");
                currentBudget.setText("Current Budget: KES " + String.format("%.2f", budgetAmount));
            } else {
                currentBudget.setText("Current Budget: KES 0.00");
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load budget", Toast.LENGTH_SHORT).show());
    }

    // Load expenses for the selected month to update progress
    private void loadExpenses() {
        Query expenseQuery = db.collection("users")
                .document(userId)
                .collection("expenses")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        expenseQuery.get().addOnSuccessListener(queryDocumentSnapshots -> {
            totalExpenses = 0.0;

            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                if (document.contains("amount") && document.contains("timestamp")) {
                    Timestamp timestamp = document.getTimestamp("timestamp");
                    if (timestamp != null) {
                        Calendar expenseDate = Calendar.getInstance();
                        expenseDate.setTime(timestamp.toDate());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
                        String expenseMonthYear = dateFormat.format(expenseDate.getTime());

                        if (expenseMonthYear.equals(selectedMonthYear)) {
                            totalExpenses += document.getDouble("amount");
                        }
                    }
                }
            }
            updateProgress();
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load expenses", Toast.LENGTH_SHORT).show());
    }

    // Suggest budget based on past expenses (using data from the last 6 months)
    private void suggestBudget() {
        Query expenseQuery = db.collection("users")
                .document(userId)
                .collection("expenses")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(100); // Fetch last 6 months of expenses

        expenseQuery.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Map<String, Double> monthlyTotals = new HashMap<>();
            SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                if (document.contains("amount") && document.contains("timestamp")) {
                    double amount = document.getDouble("amount");
                    Timestamp timestamp = document.getTimestamp("timestamp");
                    if (timestamp != null) {
                        Calendar expenseDate = Calendar.getInstance();
                        expenseDate.setTime(timestamp.toDate());
                        String monthYear = monthYearFormat.format(expenseDate.getTime());
                        monthlyTotals.put(monthYear, monthlyTotals.getOrDefault(monthYear, 0.0) + amount);
                    }
                }
            }

            if (!monthlyTotals.isEmpty()) {
                double total = 0.0;
                double highestMonth = 0.0;
                int months = 0;

                for (Double monthTotal : monthlyTotals.values()) {
                    total += monthTotal;
                    if (monthTotal > highestMonth) {
                        highestMonth = monthTotal;
                    }
                    months++;
                }

                double avgSpending = total / months;
                double aiSuggestedBudget = Math.max(avgSpending, highestMonth * 1.1);
                double adjustedBudget = aiSuggestedBudget * 1.2; // Add 20% to AI budget

                // Update UI
                budgetInput.setText(String.format("%.2f", adjustedBudget));
                Toast.makeText(this, "AI-Suggested Budget : KES " + String.format("%.2f", adjustedBudget), Toast.LENGTH_LONG).show();

                // Save the adjusted budget
                saveBudgetToFirestore(adjustedBudget);

            } else {
                Toast.makeText(this, "Not enough data for AI suggestion", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load past expenses for AI suggestion", Toast.LENGTH_SHORT).show());
    }

    // Save budget depending on selected mode (manual vs. AI)
    private void saveBudget() {
        // If AI mode is selected, suggestion is already applied.
        if (aiBudget.isChecked()) {
            // Suggestion method already saves the AI budget.
            return;
        }

        String budgetStr = budgetInput.getText().toString().trim();
        if (budgetStr.isEmpty()) {
            Toast.makeText(this, "Enter a budget amount", Toast.LENGTH_SHORT).show();
            return;
        }
        double newBudget = Double.parseDouble(budgetStr);
        saveBudgetToFirestore(newBudget);
    }

    // Save the budget amount to Firestore
    private void saveBudgetToFirestore(double budget) {
        if (userId == null) {
            Log.e("Firestore", "User ID is null. Cannot save budget.");
            return;
        }

        Log.d("Firestore", "Saving budget: " + budget + " for userId: " + userId);

        DocumentReference budgetRef = db.collection("users")
                .document(userId)
                .collection("budget")
                .document(selectedMonthYear);

        Map<String, Object> budgetData = new HashMap<>();
        budgetData.put("amount", budget);
        budgetData.put("timestamp", Timestamp.now());

        budgetRef.set(budgetData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Budget successfully saved!");
                    Toast.makeText(this, "Budget saved successfully", Toast.LENGTH_SHORT).show();

                    // ✅ Update displayed budget
                    budgetAmount = budget;
                    currentBudget.setText("Current Budget: KES " + String.format("%.2f", budgetAmount));
                    updateProgress();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error saving budget", e);
                    Toast.makeText(this, "Failed to save budget: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }


    // Update the spending progress bar and show alerts (including early AI prediction)
    private void updateProgress() {
        if (budgetAmount > 0) {
            int progress = (int) ((totalExpenses / budgetAmount) * 100);
            progress = Math.min(progress, 100);
            budgetProgressBar.setProgress(progress);
            spendingText.setText("Spending: KES " + String.format("%.2f", totalExpenses)
                    + " / " + String.format("%.2f", budgetAmount));

            // Calculate a simple prediction: extrapolate spending to the end of the month
            Calendar today = Calendar.getInstance();
            int currentDay = today.get(Calendar.DAY_OF_MONTH);
            int totalDays = today.getActualMaximum(Calendar.DAY_OF_MONTH);
            double expectedEndMonthSpending = (totalExpenses / currentDay) * totalDays;

            if (expectedEndMonthSpending > budgetAmount) {
                showSpendingWarning("AI Prediction: You might exceed your budget at this spending rate!");
            } else if (progress >= 80 && progress < 100) {
                showSpendingWarning("Warning: You have used 80% of your budget!");
            } else if (progress >= 100) {
                showSpendingWarning("ALERT: You have exceeded your budget!");
            }
        } else {
            budgetProgressBar.setProgress(0);
            spendingText.setText("No budget set.");
        }
    }

    // Display a warning alert dialog
    private void showSpendingWarning(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Budget Warning")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    // Optionally store AI predictions for tracking
    private void storeAIPrediction(double predictedBudget) {
        DocumentReference predictionRef = db.collection("users")
                .document(userId)
                .collection("budget_predictions")
                .document(selectedMonthYear);

        predictionRef.set(new PredictionModel(predictedBudget))
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "AI Prediction stored successfully"))
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to store prediction", e));
    }

    // Budget model for Firestore
    public static class BudgetModel {
        private double amount;

        public BudgetModel() {}

        public BudgetModel(double amount) {
            this.amount = amount;
        }

        public double getAmount() {
            return amount;
        }
    }

    // Prediction model for storing AI predictions
    public static class PredictionModel {
        private double predictedSpending;

        public PredictionModel() {}

        public PredictionModel(double predictedSpending) {
            this.predictedSpending = predictedSpending;
        }

        public double getPredictedSpending() {
            return predictedSpending;
        }
    }
}
