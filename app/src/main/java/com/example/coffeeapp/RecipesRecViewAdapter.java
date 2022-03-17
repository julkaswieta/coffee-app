package com.example.coffeeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adapter for the recycler view in the RecipesList Activity
 */
public class RecipesRecViewAdapter extends RecyclerView.Adapter<RecipesRecViewAdapter.ViewHolder> {

    private ArrayList<Recipe> recipes = new ArrayList<>();

    public RecipesRecViewAdapter() {
    }

    @NonNull
    @Override
    /**
     * Creates an instance of the ViewHolder (specified below) for every item in the RecView list
     */
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // initialise a view object
        // View Group --> parent of all layouts, used to group views inside this group
        // we want to attach this layout to its parent, which in this case is RecipesList activity
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_list_item, parent, false);
        // create a ViewHolder object
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    /**
     * Can be used to modify the properties of the item
     * @param position position of the item in the RecView, can be used to get data from the recipes ArrayList
     */
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // changes the text in the textview to the name of the recipe at the right position
        holder.txtName.setText(recipes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        // check if the list of recipes has changed at all
        notifyDataSetChanged();
    }

    // inner class for holding a view for every item in the recycler view
    public class ViewHolder extends RecyclerView.ViewHolder {
        // elements inside each list item
        private TextView txtName;
        private RelativeLayout recipeParent;

        // constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            recipeParent = itemView.findViewById(R.id.recipe_parent);
        }
    }
}
