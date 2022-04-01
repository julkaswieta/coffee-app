package com.example.coffeeapp;

import static com.example.coffeeapp.BeansList.beansFromDB;
import static com.example.coffeeapp.BeansList.beansList;
import static com.example.coffeeapp.CoffeeLog.drinksList;
import static com.example.coffeeapp.CoffeeLog.recipeDrinksDB;
import static com.example.coffeeapp.CoffeeLog.shopDrinksDB;
import static com.example.coffeeapp.RecipesList.recipesFromDB;
import static com.example.coffeeapp.RecipesList.recipesList;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeapp.db.BeanDB;
import com.example.coffeeapp.db.CoffeeDatabase;
import com.example.coffeeapp.db.RecipeDrinkDB;
import com.example.coffeeapp.db.ShopDrinkDB;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Recycler view adapter for drink objects - used in coffeeLog
 */
public class DrinksRecViewAdapter extends RecyclerView.Adapter<DrinksRecViewAdapter.ViewHolder> {
    private ArrayList<Drink> drinks = new ArrayList<>();
    private Context context;

    /**
     * Constructor for the recyclerview adater
     * @param context context of the parent activity
     */
    public DrinksRecViewAdapter(Context context) {
        this.context = context;
    }

    /**
     * Creates an instance of the ViewHolder (specified below) for every item in the RecView list
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * Used to modify the properties of the card in the RecView list
     * @param position position of the item in the RecView, can be used to get data from the recipes ArrayList
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int type = 2; // 0 - shop, 1 - recipe
        holder.txtDrinkName.setText(drinks.get(holder.getAdapterPosition()).getDrinkName());
        DateFormat df = new DateFormat();
        holder.txtDateAdded.setText(df.format("yyyy-MM-dd HH:mm", drinks.get(holder.getAdapterPosition()).getDateAdded()));
        if(drinks.get(holder.getAdapterPosition()).getClass() == DrinkFromShop.class) {
            DrinkFromShop drink = (DrinkFromShop) drinks.get(holder.getAdapterPosition());
            holder.txtSourceName.setText(drink.getShopName());
            holder.txtType.setText("FROM SHOP");
            type = 0;
        }
        else if(drinks.get(holder.getAdapterPosition()).getClass() == DrinkFromRecipe.class) {
            DrinkFromRecipe drink = (DrinkFromRecipe) drinks.get(holder.getAdapterPosition());
            holder.txtSourceName.setText(drink.getRecipeName());
            holder.txtType.setText("FROM RECIPE");
            type = 1;
        }
        if(drinks.get(holder.getAdapterPosition()).getDrinkPhoto() != null) {
            holder.drinkPhoto.setImageBitmap(drinks.get(holder.getAdapterPosition()).getDrinkPhoto());
            holder.drinkPhoto.setBackgroundResource(R.color.white);
        }
        else {
            holder.drinkPhoto.setImageBitmap(null);
            holder.drinkPhoto.setBackgroundResource(R.mipmap.coffee_drink);
        }

        // create an onClickListener for the delete recipe button
        final int finalType = type;
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
                AlertDialog deleteDialog = dialogBuilder.create();
                dialogBuilder
                        .setTitle(R.string.dialog_delete_drink_title)
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
                                if(finalType == 0) {
                                    for(ShopDrinkDB drinkDB : shopDrinksDB) {
                                        if(drinks.get(holder.getAdapterPosition()).getId() == drinkDB.drinkId) {
                                            db.shopDrinkDao().deleteShopDrink(drinkDB);
                                            Toast.makeText(context, "Drink " + drinkDB.drinkName + " deleted.", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                    drinksList.remove(drinks.get(holder.getAdapterPosition()));
                                    shopDrinksDB.remove(shopDrinksDB.get(holder.getAdapterPosition()));
                                }
                                else if(finalType == 1) {
                                    for(RecipeDrinkDB drinkDB : recipeDrinksDB) {
                                        if(drinks.get(holder.getAdapterPosition()).getId() == drinkDB.drinkId) {
                                            db.recipeDrinkDao().deleteRecipeDrink(drinkDB);
                                            Toast.makeText(context, "Drink " + drinkDB.drinkName + " deleted.", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                    drinksList.remove(drinks.get(holder.getAdapterPosition()));
                                    recipeDrinksDB.remove(recipeDrinksDB.get(holder.getAdapterPosition()));
                                }
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
        return drinks.size();
    }

    /**
     * changes the data set for the adapter to a new set
     * @param drinks   new data set
     */
    public void setDrinks(ArrayList<Drink> drinks) {
        Collections.sort(drinks, new Comparator<Drink>() {
            @Override
            public int compare(Drink lhs, Drink rhs) {
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
        this.drinks = drinks;
        notifyDataSetChanged();
    }

    /**
     * inner class for holding a view for every item in the recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtType, txtDrinkName, txtDateAdded, txtSourceName;
        private ImageView drinkPhoto;
        private ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtType = itemView.findViewById(R.id.type_label);
            txtDrinkName = itemView.findViewById(R.id.drink_name);
            txtDateAdded = itemView.findViewById(R.id.date_added_drink);
            txtSourceName = itemView.findViewById(R.id.drink_source);
            drinkPhoto = itemView.findViewById(R.id.drink_image);
            deleteBtn = itemView.findViewById(R.id.drink_delete_button);
        }
    }
}
