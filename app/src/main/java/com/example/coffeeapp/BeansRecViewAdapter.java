package com.example.coffeeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BeansRecViewAdapter extends RecyclerView.Adapter<BeansRecViewAdapter.ViewHolder>{
    private ArrayList<Bean> beans = new ArrayList<>();
    private Context context;

    public BeansRecViewAdapter(Context context) {
        this.context = context;
    }

    /**
     * Creates an instance of the ViewHolder (specified below) for every item in the RecView list
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // initialise a view object
        // View Group --> parent of all layouts, used to group views inside this group
        // we want to attach this layout to its parent, which in this case is RecipesList activity
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beans_list_item, parent, false);
        // create a ViewHolder object
        BeansRecViewAdapter.ViewHolder holder = new BeansRecViewAdapter.ViewHolder(view);
        return holder;
    }

    /**
     * Used to modify the properties of the card in the RecView list
     * @param position position of the item in the RecView, can be used to get data from the recipes ArrayList
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(beans.get(position).getName());
        holder.txtRoaster.setText("Roaster: " + beans.get(position).getRoaster());
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
        private TextView txtName, txtRoaster, txtRating, txtDate;
        private ImageButton deleteBtn;
        private CardView parent;
        private ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.bl_beans_name);
            txtRoaster = itemView.findViewById(R.id.bl_roaster_name);
            txtDate = itemView.findViewById(R.id.bl_date_added);
            txtRating = itemView.findViewById(R.id.bl_rating);
            deleteBtn = itemView.findViewById(R.id.bl_delete_button);
            parent = itemView.findViewById(R.id.bean_card_parent);
            photo = itemView.findViewById(R.id.bl_beans_image);
        }
    }
}
