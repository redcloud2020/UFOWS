package com.uni.ufows.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uni.ufows.R;
import com.uni.ufows.datalayer.models.Event;

import java.util.List;

/**
 * Created by sammy on 3/2/2017.
 */

public class SecondRecyclerAdapter extends RecyclerView.Adapter<SecondRecyclerAdapter.ViewHolder> {

    private List<Event> myChildren;
    private Context context;
    private String catID;
    private int parentPosition;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case



        public TextView blackOne;
        public TextView blacktwo;
        public TextView colorOne;
        public TextView colorTwo;

        public ViewHolder(View v) {
            super(v);

            blackOne = (TextView) v.findViewById(R.id.black_one);
            blacktwo = (TextView) v.findViewById(R.id.black_one_value);
            colorOne = (TextView) v.findViewById(R.id.color_one);
            colorTwo = (TextView) v.findViewById(R.id.color_one_value);


        }

    }

    public SecondRecyclerAdapter(List<Event> myDataset, Context context, int parentPosition) {
        this.myChildren = myDataset;
        this.context = context;
        this.parentPosition = parentPosition;
    }

    @Override
    public SecondRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.card_view_expenses, parent, false);
                .inflate(R.layout.row_second_recycler, parent, false);


        return new SecondRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SecondRecyclerAdapter.ViewHolder holder, final int position) {
            holder.blackOne.setText(" "+context.getString(R.string.black_one)+": "+myChildren.get(parentPosition).getMeasurement1_black() );
            holder.blacktwo.setText(context.getString(R.string.color_one)+": "+myChildren.get(parentPosition).getMeasurement1_color());
            holder.colorOne.setText(context.getString(R.string.black_two)+": "+myChildren.get(parentPosition).getMeasurement2_black() );
            holder.colorTwo.setText(context.getString(R.string.color_two)+": "+myChildren.get(parentPosition).getMeasurement2_color() + "");




    }

    @Override
    public int getItemCount() {
        return 1;
    }

}

