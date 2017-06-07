package com.uni.ufows.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.activeandroid.ActiveAndroid;
import com.uni.ufows.R;
import com.uni.ufows.config.Parameters;
import com.uni.ufows.datalayer.models.Comment;
import com.uni.ufows.datalayer.models.Event;
import com.uni.ufows.datalayer.models.Tank;
import com.uni.ufows.datalayer.models.User;
import com.uni.ufows.datalayer.server.MyHttpClient;
import com.uni.ufows.security.SecurePreferences;
import com.uni.ufows.ui.MainActivity;
import com.uni.ufows.utilities.Methods;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.osmdroid.views.overlay.Marker;

/**
 * Created by sammy on 4/13/2017.
 */

public class SecondMapFragment extends Fragment implements View.OnClickListener {
    private EditText blackMeasurementBefore;
    private EditText colorMeasurementBefore;
    private EditText blackMeasurementAfter;
    private EditText colorMeasurementAfter;

    private EditText commentEt;

    private double blackBeforeValue;
    private double colorBeforeValue;
    private double blackAfterValue;
    private double colorAfterValue;
    private String commentString;

    private Button save;

    private Spinner tank, block, district;

    private String userId;
    private String driverId;
    private String truckId;

    private Tank selectedTank;

    double latitude = 0.0;
    double longitude = 0.0;

    private MyHttpClient myHttpClient;

    private Event event;

    private boolean notFirstTime=false;

    private List<Tank> tanks = new ArrayList<>();
    private List<String> districtsList = new ArrayList<>();
    private List<String> blocksList = new ArrayList<>();
    private List<String> tanksList = new ArrayList<>();

    private Marker myMarker;

    private int eventPosition = -1;

    private boolean notSelected = false;

    private String blockString;
    private String districtString;
    private String tankString;


    public static SecondMapFragment newInstance(Event event) {
        SecondMapFragment fragment = new SecondMapFragment();
        Bundle b = new Bundle();
        b.putParcelable(Parameters.TYPE_MEASUREMENT, event);
        fragment.setArguments(b);
        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_second_map, container, false);

        if (getArguments() != null) {
            event = getArguments().getParcelable(Parameters.TYPE_MEASUREMENT);
        }

        setHasOptionsMenu(true);

        block = (Spinner) layout.findViewById(R.id.block);
        district = (Spinner) layout.findViewById(R.id.district);
        tank = (Spinner) layout.findViewById(R.id.tank);




        blackMeasurementAfter = (EditText) layout.findViewById(R.id.black_tank_number_after);
        blackMeasurementBefore = (EditText) layout.findViewById(R.id.black_tank_number_before);
        colorMeasurementAfter = (EditText) layout.findViewById(R.id.color_tank_number_after);
        colorMeasurementBefore = (EditText) layout.findViewById(R.id.color_tank_number_before);

        commentEt = (EditText) layout.findViewById(R.id.comment);


        save = (Button) layout.findViewById(R.id.save);

        save.setOnClickListener(this);


        return layout;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tanks = Tank.select();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    fillPins();
                }
            }
        }, 100);
    }
    private void setupBlock(){

    }
    private void setupBlocksNew(String district){
        blocksList.clear();
        blocksList.add("");
        for (int i = 0; i < tanks.size(); i++) {
            if(!blocksList.contains(tanks.get(i).getBlock()) && tanks.get(i).getDistrict().equals(district))
                blocksList.add(tanks.get(i).getBlock());
        }

        ArrayAdapter<String> blockArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, blocksList); //selected item will look like a spinner set from XML
        blockArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        block.setAdapter(blockArrayAdapter);
        block.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ufow_spinner_pressed_holo_light));



        if(event!=null)
            for (int w = 0; w < blocksList.size(); w++) {
                if (blocksList.get(w).equals(blockString)) {
                    block.setSelection(w);
                }
            }


    }
    private void setupTanks(String districtString, String blocks){
        tanksList.clear();
        tanksList.add("");
        for (int i = 0; i < tanks.size(); i++) {
            if(!tanksList.contains(tanks.get(i).getTankNumber()) && tanks.get(i).getBlock().equals(blocks) && tanks.get(i).getDistrict().equals(districtString))
                tanksList.add(tanks.get(i).getTankNumber());
        }
        ArrayAdapter<String> tankArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, tanksList); //selected item will look like a spinner set from XML
        tankArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tank.setAdapter(tankArrayAdapter);
        tank.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ufow_spinner_pressed_holo_light));

        if(event!=null)
        for (int e = 0; e < tanksList.size(); e++) {
            if (tanksList.get(e).equals(tankString))
                tank.setSelection(e);
        }



}
    private void fillPins() {
        districtsList.clear();
        tanksList.clear();
        blocksList.clear();




        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    districtString = districtsList.get(i);
                    notFirstTime = true;
                    if(notFirstTime)
                    setupBlocksNew(districtString);

                    block.setEnabled(true);
                }else
                    block.setEnabled(false);
//                if(adapterView!=null && adapterView.getChildCount()!=0) {
//                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
//                    ((TextView) adapterView.getChildAt(0)).setTextSize(24);
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        block.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    blockString= blocksList.get(i);

                    if(notFirstTime)
                    setupTanks(districtString, blockString);
                    notFirstTime=true;
                    tank.setEnabled(true);
                }else{
                    tank.setEnabled(false);
                }
////                        if(adapterView!=null && adapterView.getChildCount()!=0) {
//                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
//                ((TextView) adapterView.getChildAt(0)).setTextSize(24);
//                        }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    tankString = tanksList.get(i);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (getActivity() != null) {
            if (!tanks.isEmpty()) {
                districtsList.add("");
                blocksList.add("");
                tanksList.add("");
                for (int i = 0; i < tanks.size(); i++) {

                    if (!districtsList.contains(tanks.get(i).getDistrict()))
                        districtsList.add(tanks.get(i).getDistrict());
                    if (!blocksList.contains(tanks.get(i).getBlock()))
                        blocksList.add(tanks.get(i).getBlock());
                    if (!tanksList.contains(tanks.get(i).getTankNumber()))
                        tanksList.add(tanks.get(i).getTankNumber());

                }




                ArrayAdapter<String> blockArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, blocksList); //selected item will look like a spinner set from XML
                blockArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                block.setAdapter(blockArrayAdapter);
                block.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ufow_spinner_pressed_holo_light));


                ArrayAdapter<String> districtArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, districtsList); //selected item will look like a spinner set from XML
                districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                district.setAdapter(districtArrayAdapter);
                district.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ufow_spinner_pressed_holo_light));


                ArrayAdapter<String> tankArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, tanksList); //selected item will look like a spinner set from XML
                tankArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tank.setAdapter(tankArrayAdapter);
                tank.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ufow_spinner_pressed_holo_light));




                for (int i = 0; i < tanks.size(); i++) {
                    if (event != null) {
//                        if (event.getLatitude() == Double.parseDouble(tanks.get(i).getTankLatitude()) &&
//                                event.getLongitude() == Double.parseDouble(tanks.get(i).getTankLongitude())) {

                        if(event.getTank_Id().equals(tanks.get(i).getTankId())){
                            eventPosition = i;


                            final int finalI = i;

                            blackMeasurementBefore.setText("");
                            blackMeasurementAfter.setText("");
                            colorMeasurementBefore.setText("");
                            colorMeasurementAfter.setText("");
                            commentEt.setText("");


                            Tank eventTank = tanks.get(finalI);


                            if(eventTank!=null) {
                                districtString = eventTank.getDistrict();
                                blockString = eventTank.getBlock();
                                tankString = eventTank.getTankNumber();

                                for (int q = 0; q < districtsList.size(); q++) {
                                    if (districtsList.get(q).equals(districtString)) {
                                        district.setSelection(q);

                                        setupBlocksNew(districtString);
                                    }
                                }


                                for (int w = 0; w < blocksList.size(); w++) {
                                    if (blocksList.get(w).equals(blockString)) {
                                        setupTanks(districtString, blockString);
                                        block.setSelection(w);
                                    }
                                }

                                for (int e = 0; e < tanksList.size(); e++) {
                                    if (tanksList.get(e).equals(tankString))
                                        tank.setSelection(e);
                                }
                            }

                            Event event = Event.selectById(tanks.get(finalI).getTankId());
                            Comment comment = null;
                            if (event != null && !TextUtils.isEmpty(event.getComment_Id())) {
                                comment = Comment.selectById(event.getComment_Id());

                            }



                            if (comment != null && !TextUtils.isEmpty(comment.getContent()))
                                commentEt.setText(comment.getContent());

                                blackMeasurementBefore.setText((int) event.getMeasurement1_black() + "");
                                blackMeasurementAfter.setText((int) event.getMeasurement2_black() + "");
                                colorMeasurementBefore.setText((int) event.getMeasurement1_color() + "");
                                colorMeasurementAfter.setText((int) event.getMeasurement2_color() + "");











                            selectedTank = tanks.get(finalI);

                        }
                    }
                }



//                if(event==null){
//                    if(notSelected){
//                        district.setEnabled(false);
//                        tank.setEnabled(false);
//                    }
//                }


                Comment comment = null;
                if (event != null) {
                    if (!TextUtils.isEmpty(event.getComment_Id())) {
                        comment = Comment.selectById(event.getComment_Id());
                    }

                    if (comment != null && !TextUtils.isEmpty(comment.getContent()))
                        commentEt.setText(comment.getContent());

                    if (event != null) {
                        blackMeasurementBefore.setText((int) event.getMeasurement1_black() + "");
                        blackMeasurementAfter.setText((int) event.getMeasurement2_black() + "");
                        colorMeasurementBefore.setText((int) event.getMeasurement1_color() + "");
                        colorMeasurementAfter.setText((int) event.getMeasurement2_color() + "");

                        Tank tank = Tank.getTankById(event.getTank_Id());

                    }

                }

                block.setFocusable(true);
                block.setFocusableInTouchMode(true);
                block.requestFocus();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.map_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.schedule:
                ((MainActivity) getActivity()).goToFragmentSchedule();
                break;

        }
        return true;

    }

    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.save:

                if (((MainActivity) getActivity()).displayGPSNotEnabledDialog()) {
                    if (Methods.isAutomaticTimeEnabled(getActivity())) {
                        if (block.getSelectedItemPosition()!=0 && district.getSelectedItemPosition()!=0 && tank.getSelectedItemPosition()!=0) {
                            String tankId = "";
                            blockString = blocksList.get(block.getSelectedItemPosition());
                            districtString = districtsList.get(district.getSelectedItemPosition());
                            tankString = tanksList.get(tank.getSelectedItemPosition());
                            Tank tank;
                            for(int i =0; i<tanks.size(); i++){
                                if(tanks.get(i).getBlock().equals(blockString) && tanks.get(i).getDistrict().equals(districtString) && tanks.get(i).getTankNumber().equals(tankString))
                                    tankId = tanks.get(i).getTankId();
                            }
                            if (!TextUtils.isEmpty(commentEt.getText().toString()))
                                commentString = commentEt.getText().toString();
                            else commentString = "";

                            if (!TextUtils.isEmpty(blackMeasurementBefore.getText().toString()))
                                blackBeforeValue = Double.parseDouble(blackMeasurementBefore.getText().toString());
                            else blackBeforeValue = 0;
                            if (!TextUtils.isEmpty(blackMeasurementAfter.getText().toString()))
                                blackAfterValue = Double.parseDouble(blackMeasurementAfter.getText().toString());
                            else blackAfterValue = 0;
                            if (!TextUtils.isEmpty(colorMeasurementAfter.getText().toString()))
                                colorAfterValue = Double.parseDouble(colorMeasurementAfter.getText().toString());
                            else colorAfterValue = 0;
                            if (!TextUtils.isEmpty(colorMeasurementBefore.getText().toString()))
                                colorBeforeValue = Double.parseDouble(colorMeasurementBefore.getText().toString());
                            else colorBeforeValue = 0;
                            String commendid = null;
                            String eventId = null;
                            if (event == null && myMarker != null) {
                                latitude = myMarker.getPosition().getLatitude();
                                longitude = myMarker.getPosition().getLongitude();
                                commendid = UUID.randomUUID().toString();
                                eventId = UUID.randomUUID().toString();
                            } else if (event != null) {
                                latitude = event.getLatitude();
                                longitude = event.getLongitude();
                                    for (int i = 0; i < tanks.size(); i++) {
                                        if (tanks.get(i).getTankId().equals(event.getTank_Id()))
                                            selectedTank = tanks.get(i);

                                    }
                                commendid = event.getComment_Id();
                                eventId = event.getEvent_Id();
                            }

                            if (!TextUtils.isEmpty(SecurePreferences.getInstance(getActivity()).getString(Parameters.DRIVER_ID))) {
                                driverId = SecurePreferences.getInstance(getActivity()).getString(Parameters.DRIVER_ID);
                            }
                            if (!TextUtils.isEmpty(SecurePreferences.getInstance(getActivity()).getString(Parameters.USER_ID_FOR_LOG))) {
                                userId = SecurePreferences.getInstance(getActivity()).getString(Parameters.USER_ID_FOR_LOG);
                            } else {
                                User user = User.getUserByName(SecurePreferences.getInstance(getActivity()).getString(Parameters.USER_NUMBER));
                                userId = user.getUserId();
                            }
                            if (!TextUtils.isEmpty(SecurePreferences.getInstance(getActivity()).getString(Parameters.TRUCK_ID))) {
                                truckId = SecurePreferences.getInstance(getActivity()).getString(Parameters.TRUCK_ID);
                            }



                            if (!TextUtils.isEmpty(commentString) || blackBeforeValue != 0 || blackAfterValue != 0
                                    || colorBeforeValue != 0 || colorAfterValue != 0) {
                                ActiveAndroid.beginTransaction();


                                try {
                                    List<Event> allEvent = Event.getAll();
                                    if (allEvent != null && !allEvent.isEmpty())
                                        for (int i = 0; i < allEvent.size(); i++) {
                                            if (selectedTank != null && selectedTank.getTankId() != null)
                                                if (allEvent.get(i).getTank_Id().equals(selectedTank.getTankId())) {
                                                    eventId = allEvent.get(i).getEvent_Id();
                                                    commendid = allEvent.get(i).getComment_Id();
                                                    Event.delete(Event.class, allEvent.get(i).getId());
                                                }

                                        }

                                    if(!TextUtils.isEmpty(commentString)) {
                                        List<Comment> allComments = Comment.getAll();
                                        if (allComments != null && !allComments.isEmpty())
                                            for (int i = 0; i < allComments.size(); i++) {
                                                if (commendid != null)
                                                    if (allComments.get(i).getComment_Id().equals(commendid))
                                                        Comment.delete(Comment.class, allComments.get(i).getId());

                                            }

                                        if (TextUtils.isEmpty(commendid))
                                            commendid = UUID.randomUUID().toString();

                                        Comment comment = new Comment(commendid, Methods.getCurrentTimeStamp(), commentString,
                                                userId,
                                                SecurePreferences.getInstance(getActivity()).getString("d"),
                                                tankId);
                                        comment.save();
                                    } else {
                                        commendid = "";
                                    }



                                    if(TextUtils.isEmpty(eventId))
                                        eventId = UUID.randomUUID().toString();
                                    Event e = new Event(eventId, Methods.getCurrentTimeStamp(), userId, "", tankId,
                                            blackBeforeValue, blackAfterValue, colorBeforeValue, colorAfterValue,
                                            commendid, latitude, longitude, truckId, driverId);
                                    e.save();
                                    event= null;


                                    ActiveAndroid.setTransactionSuccessful();
                                } finally {
                                    ActiveAndroid.endTransaction();
                                }

                                blackMeasurementAfter.setText("");
                                blackMeasurementBefore.setText("");
                                colorMeasurementAfter.setText("");
                                colorMeasurementBefore.setText("");
                                commentEt.setText("");
                                if(getActivity()!=null)
                                ((MainActivity)getActivity()).goToFragmentSchedule();

                            }
                        }
//                            Toast.makeText(getActivity(), "الرجاء التأكد من المقاييس", Toast.LENGTH_LONG).show();
                    } else ((MainActivity) getActivity()).displayAutomaticTimeEnable();
                } else ((MainActivity) getActivity()).buildAlertMessageNoGps();


                break;

        }
    }

    private boolean checkFields() {
        String fB = blackMeasurementBefore.getText().toString();
        String sB = blackMeasurementAfter.getText().toString();
        String fC = colorMeasurementBefore.getText().toString();
        String sC = colorMeasurementAfter.getText().toString();
        if (!TextUtils.isEmpty(fB))
            if (!fB.equals("0"))
                if (Integer.parseInt(fB)!=0  || Integer.parseInt(fB) < 5 || Integer.parseInt(fB) > 400)
                    return false;
        if (!TextUtils.isEmpty(sB))
            if (!sB.equals("0"))
                if (Integer.parseInt(sB)!=0  ||Integer.parseInt(sB) < 5 || Integer.parseInt(sB) > 400)
                    return false;
        if (!TextUtils.isEmpty(fC))
            if (!fC.equals("0"))
                if (Integer.parseInt(fC)!=0  ||Integer.parseInt(fC) < 5 || Integer.parseInt(fC) > 400)
                    return false;
        if (!TextUtils.isEmpty(sC))
            if (!sC.equals("0"))
                if (Integer.parseInt(sC)!=0  ||Integer.parseInt(sC) < 5 || Integer.parseInt(sC) > 400)
                    return false;
        if(block.getSelectedItemPosition()==0)
            return false;
        if(district.getSelectedItemPosition()==0)
            return false;
        if(tank.getSelectedItemPosition()==0)
            return false;
        return true;


    }
}
