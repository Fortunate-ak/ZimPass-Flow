package com.zimpassflow.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.zimpassflow.databinding.ItemTransactionBinding;
import com.zimpassflow.models.Transaction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<Transaction> transactions = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy • HH:mm", Locale.getDefault());

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(
                ItemTransactionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.bind(transactions.get(position));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        private ItemTransactionBinding binding;

        public TransactionViewHolder(ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Transaction transaction) {
            binding.tvTollgate.setText(transaction.getTollgateName());
            binding.tvVehicle.setText(transaction.getVehiclePlate());
            binding.tvAmount.setText(String.format("-$%.2f", transaction.getAmount()));
            
            if (transaction.getTimestamp() != null) {
                binding.tvDateTime.setText(dateFormat.format(transaction.getTimestamp()));
            }
        }
    }
}