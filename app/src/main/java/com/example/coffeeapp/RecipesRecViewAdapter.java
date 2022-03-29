package com.example.coffeeapp;

import static com.example.coffeeapp.RecipesList.recipesFromDB;
import static com.example.coffeeapp.RecipesList.recipesList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeapp.db.RecipeDB;
import com.example.coffeeapp.db.CoffeeDatabase;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Adapter for the recycler view in the RecipesList Activity
 */
public class RecipesRecViewAdapter extends RecyclerView.Adapter<RecipesRecViewAdapter.ViewHolder> {
    public static final String RECIPE_ID_KEY = "recipeId";
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private Context context;

    /**
     * Constructor for the recyclerview adater
     * @param context context of the parent activity
     */
    public RecipesRecViewAdapter(Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_list_item, parent, false);
        // create a ViewHolder object
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * Used to modify the properties of the card in the RecView list
     * @param position position of the item in the RecView, can be used to get data from the recipes ArrayList
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // set the values of the elements inside the card view
        holder.txtName.setText(recipes.get(position).getName());
        DateFormat df = new DateFormat();
        holder.txtDateAdded.setText(df.format("yyyy-MM-dd", recipes.get(position).getDateAdded()));
        holder.txtBeansUsed.setText("Beans: " + recipes.get(position).getBeansUsed().getName() + ", " + recipes.get(position).getBeansUsed().getRoaster());
        holder.txtMethod.setText("Method: " + recipes.get(position).getMethodOfBrewing());
        if(recipes.get(position).getRating() != 0) {
            holder.txtRating.setText("Rating: " + recipes.get(position).getRating());
        }
        if(recipes.get(position).getPhoto() != null) {
            holder.recipePhoto.setImageBitmap(recipes.get(position).getPhoto());
            holder.recipePhoto.setBackgroundResource(R.color.white);
        }
        else {
            holder.recipePhoto.setBackgroundResource(R.mipmap.coffee_cup);
            holder.recipePhoto.setImageBitmap(null);
        }
        // create an onClickListener for when the item is clicked
        holder.recipeParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, recipes.get(holder.getAdapterPosition()).getName() + " Selected", Toast.LENGTH_SHORT).show();
                Intent displayRecipe = new Intent(context, RecipeDetails.class);
                displayRecipe.putExtra(RECIPE_ID_KEY, recipes.get(holder.getAdapterPosition()).getId());
                context.startActivity(displayRecipe);
            }
        });
        // create an onClickListener for the delete recipe button
        holder.deleteRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
                AlertDialog deleteDialog = dialogBuilder.create();
                dialogBuilder
                        .setTitle(R.string.dialog_delete_recipe_title)
                        .setMessage(R.string.dialog_delete_recipe_mess)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteDialog.dismiss();
                            }
                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CoffeeDatabase db = CoffeeDatabase.getDatabase(context.getApplicationContext());
                                for (RecipeDB recipe : recipesFromDB) {
                                    if (recipes.get(holder.getAdapterPosition()).getId() == recipe.recipeId) {
                                        db.recipeDao().deleteRecipe(recipe);
                                        Toast.makeText(context, "Recipe " + recipe.name + " deleted.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                                recipesList.remove(recipes.get(holder.getAdapterPosition()));
                                recipesFromDB.remove(recipesFromDB.get(holder.getAdapterPosition()));
                                notifyDataSetChanged();
                            }
                        }).show();
            }
        });
    }

    /**
     * Gets the number of elements in the recycler view
     * @return  number of items
     */
    @Override
    public int getItemCount() {
        return recipes.size();
    }

    /**
     * changes the data set for the adapter to a new set
     * @param recipes   new data set
     */
    public void setRecipes(ArrayList<Recipe> recipes) {
        Collections.sort(recipes, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe lhs, Recipe rhs) {
                if(lhs.getDateAdded().before(rhs.getDateAdded())) {
                    return 1;
                }
                else if(lhs.getDateAdded().after(rhs.getDateAdded())) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });
        this.recipes = recipes;
        // check if the list of recipes has changed at all
        notifyDataSetChanged();
    }

    /**
     * inner class for holding a view for every item in the recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        // elements inside each list item
        private TextView txtName, txtDateAdded, txtBeansUsed, txtMethod, txtRating;
        private CardView recipeParent;
        private ImageView recipePhoto;
        private ImageButton deleteRecipeBtn;

        // constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtDateAdded = itemView.findViewById(R.id.txtDateAdded);
            recipeParent = itemView.findViewById(R.id.recipe_parent);
            recipePhoto = itemView.findViewById(R.id.recipe_image);
            txtBeansUsed = itemView.findViewById(R.id.txtBeansUsed);
            txtMethod  = itemView.findViewById(R.id.txtMethod);
            txtRating = itemView.findViewById(R.id.txtRating);
            deleteRecipeBtn = itemView.findViewById(R.id.deleteRecipeBtn);
        }
    }
}
