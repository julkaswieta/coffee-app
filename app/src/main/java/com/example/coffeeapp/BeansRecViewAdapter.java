package com.example.coffeeapp;

import static com.example.coffeeapp.BeansList.beansFromDB;
import static com.example.coffeeapp.BeansList.beansList;
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

import com.example.coffeeapp.db.BeanDB;
import com.example.coffeeapp.db.CoffeeDatabase;
import com.example.coffeeapp.db.RecipeDB;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A recycler view adapter for Beans objects - used in BeansList
 */
public class BeansRecViewAdapter extends RecyclerView.Adapter<BeansRecViewAdapter.ViewHolder>{
    private ArrayList<Bean> beans = new ArrayList<>();
    private Context context;
    public static final String BEANS_ID_KEY = "beansId";

    /**
     * Constructor for the recyclerview adater
     * @param context context of the parent activity
     */
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
        DateFormat df = new DateFormat();
        holder.txtDate.setText(df.format("yyyy-MM-dd", beans.get(position).getDateAdded()));
        if(beans.get(position).getRating() != 0) {
            holder.txtRating.setText("Rating: " + beans.get(position).getRating());
        }
        if(beans.get(position).getPhoto() != null) {
            holder.photo.setImageBitmap(beans.get(position).getPhoto());
            holder.photo.setBackgroundResource(R.color.white);
        }
        else {
            holder.photo.setBackgroundResource(R.mipmap.beans);
            holder.photo.setImageBitmap(null);
        }

        // create an onClickListener for when the item is clicked
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, beans.get(holder.getAdapterPosition()).getName() + " Selected", Toast.LENGTH_SHORT).show();
                Intent displayBeans = new Intent(context, BeanDetails.class);
                displayBeans.putExtra(BEANS_ID_KEY, beans.get(holder.getAdapterPosition()).getId());
                context.startActivity(displayBeans);
            }
        });

        // create an onClickListener for the delete recipe button
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
                AlertDialog deleteDialog = dialogBuilder.create();
                dialogBuilder
                        .setTitle(R.string.dialog_delete_beans_title)
                        .setMessage(R.string.dialog_delete_beans_mess)
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
                                // remove all recipes using these beans
                                for(int x = recipesList.size() - 1; x >= 0; --x) {
                                    if(recipesList.get(x).getBeansUsed().getId() == beans.get(holder.getAdapterPosition()).getId()) {
                                        recipesList.remove(x);
                                    }
                                }
                                for(int x = recipesFromDB.size() - 1; x >= 0; --x) {
                                    if(recipesFromDB.get(x).beansUsedId == beans.get(holder.getAdapterPosition()).getId()) {
                                        db.recipeDao().deleteRecipe(recipesFromDB.get(x));
                                        recipesFromDB.remove(x);
                                    }
                                }
                                // delete the beans from the db and local copy of the bean list
                                for (BeanDB bean : beansFromDB) {
                                    if (beans.get(holder.getAdapterPosition()).getId() == bean.beansId) {
                                        db.beanDao().deleteBean(bean);
                                        Toast.makeText(context, "Beans " + bean.name + " deleted.", Toast.LENGTH_SHORT).show();
                                        break;

                                    }
                                }
                                beansList.remove(beans.get(holder.getAdapterPosition()));
                                beansFromDB.remove(beansFromDB.get(holder.getAdapterPosition()));
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
        return beans.size();
    }

    /**
     * Add new beans to the recycler view and refresh the view
     * @param beans arrayList with Bean objects
     */
    public void setBeans(ArrayList<Bean> beans) {
        Collections.sort(beans, new Comparator<Bean>() {
            @Override
            public int compare(Bean lhs, Bean rhs) {
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
        this.beans = beans;
        notifyDataSetChanged();
    }

    /**
     * inner class for holding a view for every item in the recycler view
     */
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
