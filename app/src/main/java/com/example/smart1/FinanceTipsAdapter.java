package com.example.smart1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.smart1.FinancialTip;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FinanceTipsAdapter extends RecyclerView.Adapter<FinanceTipsAdapter.TipViewHolder> {

    private List<FinancialTip> tipsList;

    public FinanceTipsAdapter(List<FinancialTip> tipsList) {
        this.tipsList = tipsList;
    }

    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_financial_tip, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
        FinancialTip tip = tipsList.get(position);
        holder.title.setText(tip.getTitle());
        holder.description.setText(tip.getDescription());
    }

    @Override
    public int getItemCount() {
        return tipsList.size();
    }

    public static class TipViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public TipViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            description = itemView.findViewById(R.id.tv_description);
        }
    }
}

