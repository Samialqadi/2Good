package com.dawidjk2.sesfrontend.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dawidjk2.sesfrontend.Models.PreferenceItem;
import com.dawidjk2.sesfrontend.Models.Transaction;
import com.dawidjk2.sesfrontend.R;

import java.util.ArrayList;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.PreferencesViewHolder> {

    private ArrayList<PreferenceItem> mDataset;

    public static class PreferencesViewHolder extends RecyclerView.ViewHolder {

        public CheckBox preference;

        public PreferencesViewHolder(View itemView) {
            super(itemView);
            preference = itemView.findViewById(R.id.preference);
        }

        public void setDetails(PreferenceItem preferenceItem) {
            preference.setText(preferenceItem.getTitle());
        }
    }

    // Constructor
    public PreferencesAdapter(ArrayList<PreferenceItem> myDataset) { this.mDataset = myDataset; }

    // Create new views
    @Override
    public PreferencesAdapter.PreferencesViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.preference, parent, false);

        PreferencesAdapter.PreferencesViewHolder viewHolder = new PreferencesAdapter.PreferencesViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view
    @Override
    public void onBindViewHolder(PreferencesAdapter.PreferencesViewHolder holder, int position) {
        PreferenceItem preferenceItem = mDataset.get(position);
        holder.setDetails(preferenceItem);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
