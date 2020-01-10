package com.dawidjk2.sesfrontend.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dawidjk2.sesfrontend.R;
import com.dawidjk2.sesfrontend.Models.Transaction;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private ArrayList<Transaction> mDataset;

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        public TextView vendor;
        public TextView amount;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            vendor = itemView.findViewById(R.id.transactionVendor);
            amount = itemView.findViewById(R.id.transactionAmount);
        }

        public void setDetails(Transaction transaction) {
            vendor.setText(transaction.getVendor());
            amount.setText("$" + transaction.getAmount());
        }
    }

    // Constructor
    public TransactionAdapter(ArrayList<Transaction> myDataset) {
        this.mDataset = myDataset;
    }

    // Create new views
    @Override
    public TransactionAdapter.TransactionViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction, parent, false);

        TransactionViewHolder viewHolder = new TransactionViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view
    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        Transaction transaction = mDataset.get(position);
        holder.setDetails(transaction);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
