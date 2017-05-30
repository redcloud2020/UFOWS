package com.uni.ufows.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.uni.ufows.R;
import com.uni.ufows.adapter.FirstRecyclerAdapter;
import com.uni.ufows.datalayer.models.Event;
import com.uni.ufows.datalayer.models.Tank;
import com.uni.ufows.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sammy on 3/2/2017.
 */

public class ScheduleFragment extends Fragment {


    private RecyclerView firstRecyclerView;
    private RecyclerView secondRecyclerView;

    private FirstRecyclerAdapter firstRecyclerAdapter;

    private List<Event> measurement;

    private ArrayList<String> tanks = new ArrayList<>();
    private ArrayList<String> block = new ArrayList<>();
    private ArrayList<String> district = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_schedule, container, false);

        firstRecyclerView = (RecyclerView) layout.findViewById(R.id.first_recycler);

        LinearLayoutManager firstLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        firstRecyclerView.setLayoutManager(firstLayoutManager);


        measurement = Event.getAll();



        if(measurement!=null && !measurement.isEmpty())
        {
            List<Tank> allTanks = Tank.getAll();
            Tank tank = null;
            for(int i =0; i<measurement.size(); i++){
                if(allTanks!=null && !allTanks.isEmpty())
                    for(int j =0;j<allTanks.size();j++){
                        if(allTanks.get(j).getTankId().equals(measurement.get(i).getTank_Id()))
                            tank = allTanks.get(j);
;                    }
                if(tank!=null){
                    block.add(tank.getBlock());
                    district.add(tank.getDistrict());
                    tanks.add(tank.getTankNumber());
                }
            }


            firstRecyclerAdapter = new FirstRecyclerAdapter(measurement,block, district, tanks, getActivity());
            firstRecyclerView.setAdapter(firstRecyclerAdapter);
            firstRecyclerAdapter.setOnClick(new FirstRecyclerAdapter.OnClick() {
                @Override
                public void onClick(Event event) {
                    ((MainActivity) getActivity()).goToFragmentMap(event);
                }
            });

        }

        setHasOptionsMenu(true);

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_schedule, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map:
                ((MainActivity) getActivity()).goToFragmentMap();
                break;

        }
        return true;

    }
}
