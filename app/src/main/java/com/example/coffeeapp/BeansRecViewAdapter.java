package com.example.coffeeapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BeansRecViewAdapter extends RecyclerView.Adapter<BeansRecViewAdapter.ViewHolder>{
    private ArrayList<Bean> beans = new ArrayList<>();
    private Context context;

    public BeansRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    /**
     * Add new beans to the recycler view and refresh the view
     * @param beans arrayList with Bean objects
     */
    public void setBeans(ArrayList<Bean> beans) {
        this.beans = beans;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
