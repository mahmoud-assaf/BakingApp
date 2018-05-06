package com.mahmoud.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahmoud.bakingapp.R;
import com.mahmoud.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mahmoud on 08/03/2018.
 */

public class RecipesGridAdapter extends RecyclerView.Adapter<RecipesGridAdapter.ViewHolder>  {
    private List<Recipe> RecipeList = new ArrayList<Recipe>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private Random r;
    //sorry for colors :D .. just placeholders
    private int[] recipes_backgrounds={R.drawable.bg1,
            R.drawable.bg3,

            R.drawable.bg6,
            R.drawable.bg7,};

    // data is passed into the constructor
    public RecipesGridAdapter(Context context, List<Recipe> list){
        this.mInflater=LayoutInflater.from(context);
        this.RecipeList=list;
        this.context=context;
        r = new Random();

        }
    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = RecipeList.get(position);
        if (recipe.image.equals("")) {
            holder.recipe_image.setBackgroundResource(recipes_backgrounds[r.nextInt(4)]);
        } else {
            Picasso.with(context)

                    .load(recipe.image)
                    .placeholder(R.drawable.ic_loading)

                    .into(holder.recipe_image);

        }
        holder.recipe_name.setText(recipe.name);
        holder.recipe_serving.setText("Serving : "+String.valueOf(recipe.servings)+" persons");

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return RecipeList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView recipe_image;
        TextView recipe_name;
        TextView recipe_serving;



        ViewHolder(View itemView) {
            super(itemView);
            recipe_image = (ImageView) itemView.findViewById(R.id.recipe_image);
            recipe_name=(TextView) itemView.findViewById(R.id.recipe_name_tv);
            recipe_serving=(TextView) itemView.findViewById(R.id.serving_tv);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Recipe getItem(int id) {
        return RecipeList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
