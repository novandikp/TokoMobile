package com.itbrain.aplikasitoko;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GroceryRecyclerViewAdapter extends RecyclerView.Adapter<GroceryRecyclerViewAdapter.GroceryViewHolder> {

    private List<Grocery> groceryList = new ArrayList<>();

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_jasa, viewGroup, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder groceryViewHolder, int i) {
        Grocery grocery = groceryList.get(i);

        groceryViewHolder.tvGroceryName.setText(grocery.getName());
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public void updateData(List<Grocery> groceryList) {
        this.groceryList = groceryList;
        notifyDataSetChanged();
    }

    static class GroceryViewHolder extends RecyclerView.ViewHolder {

        private TextView tvGroceryName;
        private TextView tvGroceryQuantity;

        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
//            tvGroceryName = itemView.findViewById(R.id.tvGroceryName);
        }
    }
}