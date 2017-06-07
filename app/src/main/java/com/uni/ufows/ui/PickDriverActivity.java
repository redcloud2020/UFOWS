package com.uni.ufows.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.uni.ufows.R;
import com.uni.ufows.config.Parameters;
import com.uni.ufows.datalayer.models.Truck;
import com.uni.ufows.datalayer.models.User;
import com.uni.ufows.security.SecurePreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sammy on 4/20/2017.
 */

public class PickDriverActivity extends AppCompatActivity implements View.OnClickListener {

        private Spinner truckNumber;
        private Spinner driverName;

        private Button save;

         private TextView truckName;
         private TextView driverNumber;

        ArrayList<String> trucks = new ArrayList<>();
        ArrayList<String> truckTranslation = new ArrayList<>();

        private List<User> drivers = new ArrayList<>();
        private List<User> driversSorted = new ArrayList<>();


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pick_driver);


            truckNumber = (Spinner) findViewById(R.id.truck_number);
            driverName = (Spinner) findViewById(R.id.driver_name);
            truckName = (TextView) findViewById(R.id.truck_name);
            driverNumber = (TextView) findViewById(R.id.driver_number);
            save = (Button) findViewById(R.id.save);


            drivers = User.getAll();
            ArrayList<String> spinnerArray = new ArrayList<>();
            for (int i = 0; i < drivers.size(); i++) {
                if (drivers.get(i).getRoleId().equals("15")) {
                    spinnerArray.add(drivers.get(i).getFirstNameAr() + " " + drivers.get(i).getLastNameAr());
                    driversSorted.add(drivers.get(i));
                }
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            driverName.setAdapter(spinnerArrayAdapter);
            driverName.setBackground(ContextCompat.getDrawable(this, R.drawable.ufow_spinner_pressed_holo_light));

            final List<Truck> truckList = Truck.getAll();

            for (int i = 0; i < truckList.size(); i++) {
                trucks.add(truckList.get(i).getTruck_number());
                truckTranslation.add(truckList.get(i).getTruck_Id());
            }


            ArrayAdapter<String> truckArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, trucks); //selected item will look like a spinner set from XML
            truckArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            truckNumber.setAdapter(truckArray);
            truckNumber.setBackground(ContextCompat.getDrawable(this, R.drawable.ufow_spinner_pressed_holo_light));


            driverName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    driverNumber.setText(driversSorted.get(i).getUserName() + "");
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView) adapterView.getChildAt(0)).setTextSize(24);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            driverNumber.setVisibility(View.GONE);

            truckNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    truckName.setText(
                            getString(R.string.quantity) + " " +
                                    truckList.get(i).getTruck_total_capacity() + " " + getString(R.string.meter));
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView) adapterView.getChildAt(0)).setTextSize(24);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            save.setOnClickListener(this);
        }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save:
                SecurePreferences.getInstance(this).put(Parameters.DRIVER_NUMBER, driversSorted.get(driverName.getSelectedItemPosition()).getUserName());
                SecurePreferences.getInstance(this).put("driver_name", driversSorted.get(driverName.getSelectedItemPosition()).getFirstNameAr()+" "+
                        driversSorted.get(driverName.getSelectedItemPosition()).getLastNameAr());
                SecurePreferences.getInstance(this).put("d", driversSorted.get(driverName.getSelectedItemPosition()).getUserId());
                SecurePreferences.getInstance(this).put(Parameters.USER_ID_ABOUT, driversSorted.get(driverName.getSelectedItemPosition()).getUserName());
                SecurePreferences.getInstance(this).put(Parameters.DRIVER_ID, driversSorted.get(driverName.getSelectedItemPosition()).getUserId());
                SecurePreferences.getInstance(this).put(Parameters.VEHICLE_NUMBER, trucks.get(truckNumber.getSelectedItemPosition()));
                SecurePreferences.getInstance(this).put(Parameters.TRUCK_ID, truckTranslation.get(truckNumber.getSelectedItemPosition()));

                SecurePreferences.getInstance(this).put(Parameters.FIRST_TIME, Parameters.FIRST_TIME);
                Intent homeIntent = new Intent(PickDriverActivity.this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                break;
            default:
                break;
        }
    }
}
