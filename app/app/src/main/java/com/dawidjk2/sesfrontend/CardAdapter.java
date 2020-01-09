package com.dawidjk2.sesfrontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class CardViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView cardName;
        public TextView endsIn;
        public TextView previousActivity;

        public CardViewHolder(View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.cardName);
            endsIn = itemView.findViewById(R.id.cardNumber);
            previousActivity = itemView.findViewById(R.id.previousActivity);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardAdapter(String[] myDataset) {
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_page, parent, false);

        CardViewHolder viewholder = new CardViewHolder(view);
        return viewholder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public setDetails(Card card) {
        cardName =
    }
}