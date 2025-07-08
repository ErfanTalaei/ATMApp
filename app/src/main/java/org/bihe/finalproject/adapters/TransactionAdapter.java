package org.bihe.finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.bihe.finalproject.databinding.ItemTransactionBinding;
import org.bihe.finalproject.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final Context context;
    private List<Transaction> transactions;
    private final LayoutInflater layoutInflater;
    private TransactionAdapterCallback callback;

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public TransactionAdapter(Context context, List<Transaction> transactions, TransactionAdapterCallback callback) {
        this(context, transactions);
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position);
    }


    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void insert(Transaction transaction) {
        transactions.add(transaction);
        notifyItemInserted(transactions.size());
    }

    public void itemInsertedOnTop() {
        notifyItemInserted(0);
        notifyItemRangeChanged(0, transactions.size() - 1);
    }

    public void updateAll(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    public void update() {
        notifyDataSetChanged();
    }

    public List<Transaction> getTransactionsBasedOnUserName(String userName) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getUserName().equals(userName)) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    public void remove(Transaction transaction) {
        int index = transactions.indexOf(transaction);
        if (index != -1) {
            transactions.remove(index);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, transactions.size());
        }
    }

//    public long getId(int position) {
//        return transactions.get(position).getId();
//    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemTransactionBinding binding;

        public ViewHolder(@NonNull ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(int position) {
            Transaction transaction = transactions.get(position);

            binding.userNameTv.setText(transaction.getUserName());
            binding.transactionAmountTv.setText(String.valueOf(transaction.getAmount()));
            binding.transactionDateTv.setText(transaction.getFormattedModifiedDate());
            binding.typeTv.setText(transaction.getType());
            binding.typeTv.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, transaction.getTypeColor())
            );
            binding.mainLay.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onItemClicked(position);
                }
            });
            binding.mainLay.setOnLongClickListener(v -> {
                if (callback != null) {
                    callback.onItemLongClicked(transaction);
                }
                return true;
            });
        }
    }

    public interface TransactionAdapterCallback {
        void onItemClicked(int position);

        void onItemLongClicked(Transaction transaction);
    }
}
