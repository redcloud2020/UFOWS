package com.uni.ufows.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uni.ufows.R;
import com.uni.ufows.config.Parameters;
import com.uni.ufows.security.SecurePreferences;

/**
 * Created by sammy on 3/2/2017.
 */

public class ProfileFragment extends Fragment {


    private TextView username;
    private TextView truckname;
    private TextView drivername;
    private TextView name;
    private TextView driverId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_profile, container, false);

        setHasOptionsMenu(false);

        username = (TextView) layout.findViewById(R.id.username);
        truckname = (TextView) layout.findViewById(R.id.truck_name);
        drivername = (TextView) layout.findViewById(R.id.driver_name);
        name = (TextView) layout.findViewById(R.id.name);
        driverId = (TextView) layout.findViewById(R.id.driver_id);

        name.setText(SecurePreferences.getInstance(getActivity()).getString("my_name"));
        username.setText( SecurePreferences.getInstance(getActivity()).getString(Parameters.USER_NUMBER));
        truckname.setText(SecurePreferences.getInstance(getActivity()).getString(Parameters.VEHICLE_NUMBER));
        drivername.setText(SecurePreferences.getInstance(getActivity()).getString("driver_name"));
                driverId.setText(SecurePreferences.getInstance(getActivity()).getString(Parameters.DRIVER_NUMBER));

        return layout;
    }
}