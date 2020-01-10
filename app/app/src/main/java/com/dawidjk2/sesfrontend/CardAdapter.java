package com.dawidjk2.sesfrontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private ArrayList<Card> mDataset;
    private OnItemListener mOnItemListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // each data item is just a string in this case
        public TextView cardName;
        public TextView endsIn;
        public TextView previousActivity;

        public OnItemListener onItemListener;

        public CardViewHolder(View itemView, OnItemListener onItemListener) {
            super(itemView);
            cardName = itemView.findViewById(R.id.cardName);
            endsIn = itemView.findViewById(R.id.cardNumber);
            previousActivity = itemView.findViewById(R.id.previousActivity);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        public void setDetails(Card card) {
            cardName.setText(card.getCardName());
            int cardNumLength = card.getCardNumber().length();
            endsIn.setText("Ending In - " + card.getCardNumber().substring(cardNumLength - 5, cardNumLength - 1));
            previousActivity.setText("$" + card.getPreviousActivity());

        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardAdapter(ArrayList<Card> myDataset, OnItemListener onItemListener) {
        this.mDataset = myDataset;
        this.mOnItemListener = onItemListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        CardViewHolder viewHolder = new CardViewHolder(view, mOnItemListener);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Card card = mDataset.get(position);
        holder.setDetails(card);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }
}