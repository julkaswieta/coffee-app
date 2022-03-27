package com.example.coffeeapp;

import static com.example.coffeeapp.BeansList.beansFromDB;
import static com.example.coffeeapp.BeansList.beansList;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeapp.db.BeanDB;
import com.example.coffeeapp.db.CoffeeDatabase;
import com.example.coffeeapp.db.RecipeDB;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
        DateFormat df = new DateFormat();
        holder.txtDate.setText(df.format("yyyy-MM-dd", beans.get(position).getDateAdded()));
        if(beans.get(position).getRating() != 0) {
            holder.txtRating.setText("Rating: " + beans.get(position).getRating());
        }
        if(beans.get(position).getPhoto() != null) {
            holder.photo.setImageBitmap(beans.get(position).getPhoto());
        }
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
                                for(Recipe recipe : recipesList) {
                                    if(recipe.getBeansUsed().getId() == beans.get(holder.getAdapterPosition()).getId()) {
                                        recipesList.remove(recipe);
                                        Toast.makeText(context, "Recipe " + recipe.getName() + " deleted.", Toast.LENGTH_SHORT).show();                                    }
                                }
                                for(RecipeDB recDB : recipesFromDB) {
                                    if(recDB.beansUsedId == beans.get(holder.getAdapterPosition()).getId()) {
                                        recipesFromDB.remove(recDB);
                                        db.recipeDao().deleteRecipe(recDB);
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
