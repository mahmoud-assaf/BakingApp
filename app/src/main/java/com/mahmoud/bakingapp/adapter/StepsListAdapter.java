package com.mahmoud.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahmoud.bakingapp.R;
import com.mahmoud.bakingapp.model.Recipe;
import com.mahmoud.bakingapp.model.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mahmoud on 10/03/2018.
 */

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.ViewHolder>  {
    private List<Step> mDataSource;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private int selectedPos = RecyclerView.NO_POSITION;

    // data is passed into the constructor
    public StepsListAdapter(Context context, List<Step> list,int slectedPos){
        this.mInflater=LayoutInflater.from(context);
        this.mDataSource=list;
        this.context=context;
        this.selectedPos=slectedPos;

    }
    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.step_row, parent, false);

        return new ViewHolder(view);
    }

    // binds the data in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Step step = mDataSource.get(position);
        holder.itemView.setSelected(selectedPos == position);
        holder.step_description.setText(step.shortDescription);

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mDataSource.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView step_description;


        ViewHolder(View itemView) {
            super(itemView);

            step_description=(TextView) itemView.findViewById(R.id.step_description_tv);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);

            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Step getItem(int id) {
        return mDataSource.get(id);
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
