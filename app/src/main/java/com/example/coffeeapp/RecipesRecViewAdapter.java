package com.example.coffeeapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Adapter for the recycler view in the RecipesList Activity
 */
public class RecipesRecViewAdapter extends RecyclerView.Adapter<RecipesRecViewAdapter.ViewHolder> {

    private ArrayList<Recipe> recipes = new ArrayList<>();
    private Context context;

    public RecipesRecViewAdapter(Context context) {
        this.context = context;
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
     * Used to modify the properties of the card in the RecView list
     * @param position position of the item in the RecView, can be used to get data from the recipes ArrayList
     */
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // set the values of the elements inside the card view
        holder.txtName.setText(recipes.get(position).getName());
        holder.txtDateAdded.setText(recipes.get(position).getDateAdded().toString());
        if(recipes.get(position).getPhoto() != null) {
            holder.recipePhoto.setImageBitmap(recipes.get(position).getPhoto());
        }
        // create an onClickListener for when the item is clicked
        holder.recipeParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, recipes.get(holder.getAdapterPosition()).getName() + " Selected", Toast.LENGTH_SHORT).show();
            }
        });
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
        private TextView txtName, txtDateAdded;
        private CardView recipeParent;
        private ImageView recipePhoto;

        // constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtDateAdded = itemView.findViewById(R.id.txtDateAdded);
            recipeParent = itemView.findViewById(R.id.recipe_parent);
            recipePhoto = itemView.findViewById(R.id.recipe_image);
        }
    }
}
