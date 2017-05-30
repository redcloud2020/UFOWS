package com.uni.ufows.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uni.ufows.R;
import com.uni.ufows.datalayer.models.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sammy on 3/2/2017.
 */

public class FirstRecyclerAdapter extends RecyclerView.Adapter<FirstRecyclerAdapter.ViewHolder> {

    private List<Event> myChildren;
    private Context context;
    private String catID;
    private SecondRecyclerAdapter secondRecyclerAdapter;
    private OnClick onClick;
    ArrayList<String> blocks;
    ArrayList<String> districts;
    ArrayList<String> tanks;
    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case



        public TextView district;
        public TextView block;
        public TextView tank;
        public TextView time;
        public RecyclerView secondRecyclerView;

        public ViewHolder(View v) {
            super(v);

            district = (TextView) v.findViewById(R.id.district);
            block = (TextView) v.findViewById(R.id.block);
            tank = (TextView) v.findViewById(R.id.tank);
            time = (TextView) v.findViewById(R.id.time);
            secondRecyclerView = (RecyclerView) v.findViewById(R.id.second_recycler);


        }

    }

    public FirstRecyclerAdapter(List<Event> myDataset,ArrayList<String> blocks,ArrayList<String> districts,ArrayList<String> tanks, Context context) {
        this.myChildren = myDataset;
        this.context = context;
        this.blocks = blocks;
        this.districts = districts;
        this.tanks = tanks;
    }

    @Override
    public FirstRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.card_view_expenses, parent, false);
                .inflate(R.layout.row_first_recycler, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(position==0){
            holder.time .setText(context.getString(R.string.time));
            holder.district .setText(context.getString(R.string.district));
            holder.tank .setText(context.getString(R.string.tank));
            holder.block.setText(context.getString(R.string.block));
        }else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClick!=null)
                        onClick.onClick(myChildren.get(position-1));
                }
            });
//            holder.tank.setText(myChildren.get(position).getTank_Id());
            String str = myChildren.get(position-1).getTimestamp();
            String[] splitStr = str.split("\\s+");
            holder.time.setText(splitStr[1]);
            holder.district.setText(districts.get(position-1));
            holder.tank.setText(tanks.get(position-1));
            holder.block.setText(blocks.get(position-1));

            LinearLayoutManager secondLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.secondRecyclerView.setLayoutManager(secondLayoutManager);
            secondRecyclerAdapter = new SecondRecyclerAdapter(myChildren, context, position-1);
            holder.secondRecyclerView.setAdapter(secondRecyclerAdapter);
        }
    }

    public interface OnClick{
        void  onClick(Event event);
    }

    @Override
    public int getItemCount() {
        return myChildren.size()+1;
    }

}

