package com.example.smart1;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;
    private Context context;
    private OnExpenseClickListener listener; // Interface for handling clicks

    public ExpenseAdapter(List<Expense> expenseList, Context context, OnExpenseClickListener listener) {
        this.expenseList = expenseList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.amountTextView.setText("Ksh " + expense.getAmount());
        holder.categoryTextView.setText(expense.getCategory());
        holder.noteTextView.setText(expense.getNote());

        // Handle delete button click
        holder.deleteButton.setOnClickListener(view -> {
            if (listener != null) {
                listener.onDeleteExpense(expense.getId());
            }
        });

        // Handle transfer to savings button click
        holder.transferButton.setOnClickListener(view -> {
            if (listener != null) {
                listener.onTransferToSavings(expense.getId(), expense.getAmount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView amountTextView, categoryTextView, noteTextView;
        Button deleteButton, transferButton;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.expense_amount);
            categoryTextView = itemView.findViewById(R.id.expense_category);
            noteTextView = itemView.findViewById(R.id.expense_note);
            deleteButton = itemView.findViewById(R.id.btn_delete);
            transferButton = itemView.findViewById(R.id.btn_transfer);
        }
    }

    // Interface for handling expense actions
    public interface OnExpenseClickListener {
        void onDeleteExpense(String expenseId);
        void onTransferToSavings(String expenseId, double amount);
    }
}
