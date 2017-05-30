package com.uni.ufows.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//import org.mapsforge.map.layer.download.tilesource.OpenStreetMapMapnik;

/**
 * Created by sammy on 2/28/2017.
 */

public class MapFragment extends Fragment implements View.OnClickListener {

    private MapView mMapView;

    private MapController mMapController;

    private BottomSheetBehavior mBottomSheetBehavior;

    private TextView arrow;

    private boolean expanded = false;

    private RelativeLayout mapLinearLayout;

    private ImageView myLocation;

    private EditText blackMeasurementBefore;
    private EditText colorMeasurementBefore;
    private EditText blackMeasurementAfter;
    private EditText colorMeasurementAfter;

    private TextView districtNumber;
    private TextView downarrow;

    private EditText commentEt;

    private double blackBeforeValue;
    private double colorBeforeValue;
    private double blackAfterValue;
    private double colorAfterValue;
    private String commentString;

    private Button save;

    private Boolean displayed = false;

    private MyLocationNewOverlay myLocationNewOverlay;

    private RotationGestureOverlay rotationGestureOverlay;

    private ArrayList<Marker> pinList = new ArrayList<>();

    private RadiusMarkerClusterer radiusMarkerClusterer;

    private String userId;
    private String driverId;
    private String truckId;

    private Tank selectedTank;

    double latitude = 0.0;
    double longitude = 0.0;

    private MyHttpClient myHttpClient;

    private Event event;

    private List<Tank> tanks = new ArrayList<>();

    private Marker myMarker;

    private int eventPosition = -1;

    public static MapFragment newInstance(Event event) {
        MapFragment fragment = new MapFragment();
        Bundle b = new Bundle();
        b.putParcelable(Parameters.TYPE_MEASUREMENT, event);
        fragment.setArguments(b);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_map, container, false);

        if (getArguments() != null) {
            event = getArguments().getParcelable(Parameters.TYPE_MEASUREMENT);
        }

//        MapsWithMeApi.showPointOnMap(getActivity(), 32.294649, 36.327595, "Camp");

        Configuration.getInstance().load(getActivity(), PreferenceManager.getDefaultSharedPreferences(getActivity()));

        myLocation = (ImageView) layout.findViewById(R.id.my_location);

        setHasOptionsMenu(true);

        View bottomSheet = layout.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        downarrow = (TextView) layout.findViewById(R.id.arrow_down);
        arrow = (TextView) layout.findViewById(R.id.arrow);

        mapLinearLayout = (RelativeLayout) layout.findViewById(R.id.map_linear_layout);

        blackMeasurementAfter = (EditText) layout.findViewById(R.id.black_tank_number_after);
        blackMeasurementBefore = (EditText) layout.findViewById(R.id.black_tank_number_before);
        colorMeasurementAfter = (EditText) layout.findViewById(R.id.color_tank_number_after);
        colorMeasurementBefore = (EditText) layout.findViewById(R.id.color_tank_number_before);

        commentEt = (EditText) layout.findViewById(R.id.comment);

        districtNumber = (TextView) layout.findViewById(R.id.district_number);

        save = (Button) layout.findViewById(R.id.save);

        mMapView = (MapView) layout.findViewById(R.id.mapview);

        mMapView.setMapListener(new MapListener() {
            public boolean onZoom(ZoomEvent arg0) {
                if (arg0.getZoomLevel() > 17)
                    change();
                else hide();
                return false;
            }

            public boolean onScroll(ScrollEvent arg0) {
                if (arg0.getSource().getZoomLevel() > 17)
                    change();
                return false;
            }
        });


        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mMapView.setBuiltInZoomControls(false);
        mMapView.setSaveEnabled(true);
        mMapView.setMultiTouchControls(true);
        mMapView.setDrawingCacheEnabled(false);
        mMapController = (MapController) mMapView.getController();
//        mMapController.setZoom(13);

//
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getActivity()), mMapView);
        mLocationOverlay.enableMyLocation();
        this.mMapView.getOverlays().add(mLocationOverlay);

        this.myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getActivity()), mMapView);
        this.myLocationNewOverlay.enableMyLocation();
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_mylocation);
        this.myLocationNewOverlay.setPersonIcon(icon);
        mMapView.getOverlays().add(this.myLocationNewOverlay);
        mMapView.invalidate();

//        this.rotationGestureOverlay = new RotationGestureOverlay(getActivity(), mMapView);
//        mMapView.getOverlays().add(this.rotationGestureOverlay);

        mMapView.setTileSource(new XYTileSource("Mapnik", 15, 19, 516, ".png", new String[]{}));
        mMapView.setUseDataConnection(false);
        try {
            if (!TextUtils.isEmpty(SecurePreferences.getInstance(getActivity()).getString(Parameters.LATITUDE)))
            latitude = Double.parseDouble(SecurePreferences.getInstance(getActivity()).getString(Parameters.LATITUDE));
            else latitude = 0.0;
            if (!TextUtils.isEmpty(SecurePreferences.getInstance(getActivity()).getString(Parameters.LONGITUDE)))
            longitude = Double.parseDouble(SecurePreferences.getInstance(getActivity()).getString(Parameters.LONGITUDE));
            else longitude = 0.0;
        }catch (NumberFormatException e){
            latitude = 0.0;
            longitude = 0.0;
        }
//        mMapView.setMinZoomLevel(16);
//        double latitude = this.myLocationNewOverlay.getMyLocationProvider().getLastKnownLocation().getLatitude();
//        double longitude = this.myLocationNewOverlay.getMyLocationProvider().getLastKnownLocation().getLongitude();
//        mMapView.setRotation(45f)

//        mMapView.setMaxZoomLevel(19);
        GeoPoint gPt = new GeoPoint(32.294649, 36.327595);
        mMapController.setCenter(gPt);
        mMapController.setZoom(18);
        mMapView.setMinZoomLevel(15);


        if (event != null) {
            GeoPoint geo = new GeoPoint(event.getLatitude(), event.getLongitude());
            mMapController.setCenter(geo);
            mMapController.setZoom(18);


        }
//        mMapView.setMapListener(new DelayedMapListener(new MapListener() {
//            @Override
//            public boolean onScroll(ScrollEvent event) {
//                mMapView.invalidate();
//                return false;
//            }
//
//            @Override
//            public boolean onZoom(ZoomEvent event) {
//
//                mMapView.invalidate();
//                return false;
//            }
//        }));

        arrow.setOnClickListener(this);
        save.setOnClickListener(this);
        myLocation.setOnClickListener(this);
        downarrow.setOnClickListener(this);


//
//        radiusMarkerClusterer = new RadiusMarkerClusterer(getActivity());
//        Drawable clusterIconD = getResources().getDrawable(R.drawable.ic_menu_compass);
//        Bitmap clusterIcon = ((BitmapDrawable)clusterIconD).getBitmap();
//        radiusMarkerClusterer.setIcon(clusterIcon);
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
        }, 2000);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void hidePins() {
        mMapView.getOverlays().removeAll(pinList);
        mMapView.invalidate();
    }

    private void showPins() {

        mMapView.getOverlays().addAll(pinList);
        mMapView.invalidate();
    }

    private void fillPins() {
        if (getActivity() != null && mMapView != null) {
            if (!tanks.isEmpty()) {
                for (int i = 0; i < tanks.size(); i++) {
                    GeoPoint mGeoP = new GeoPoint(Double.parseDouble(tanks.get(i).getTankLatitude()),
                            Double.parseDouble(tanks.get(i).getTankLongitude()));

                    // build a new marker pin
                    final Marker mPin = new Marker(mMapView);
                    mPin.setPosition(mGeoP);
                    mPin.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    if (event != null) {
                        if (event.getLatitude() == Double.parseDouble(tanks.get(i).getTankLatitude()) &&
                                event.getLongitude() == Double.parseDouble(tanks.get(i).getTankLongitude())) {
                            eventPosition = i;

                            mPin.setIcon(getResources().getDrawable(R.drawable.red_pin));
                        } else mPin.setIcon(getResources().getDrawable(R.drawable.pin));
                    } else
                        mPin.setIcon(getResources().getDrawable(R.drawable.pin));


                    mPin.setTitle(tanks.get(i).getTankNumber());
                    final int finalI = i;

                    mPin.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker, MapView mapView) {
                            if (eventPosition != -1) {
                                pinList.get(eventPosition).setIcon(getResources().getDrawable(R.drawable.pin));
                            }
                            if (myMarker == null) {
                                myMarker = marker;
                                marker.setIcon(getResources().getDrawable(R.drawable.red_pin));
                            } else {
                                myMarker.setIcon(getResources().getDrawable(R.drawable.pin));
                                myMarker = marker;
                                marker.setIcon(getResources().getDrawable(R.drawable.red_pin));
                            }

//                                GeoPoint gPt = new GeoPoint(marker.getPosition().getLatitude(), marker.getPosition().getLongitude());
//                                mMapController.setCenter(gPt);
                            mapView.invalidate();
                            blackMeasurementBefore.setText("");
                            blackMeasurementAfter.setText("");
                            colorMeasurementBefore.setText("");
                            colorMeasurementAfter.setText("");
                            commentEt.setText("");


                            Event event = Event.selectById(tanks.get(finalI).getTankId());
                            Comment comment = null;
                            if (event != null && !TextUtils.isEmpty(event.getComment_Id())) {
                                comment = Comment.selectById(event.getComment_Id());

                            }

                            districtNumber.setText("");

                            districtNumber.setText(tanks.get(finalI).getTankNumber() + "-" + tanks.get(finalI).getDistrict()
                                    + "-" + tanks.get(finalI).getBlock());


                            if (comment != null && !TextUtils.isEmpty(comment.getContent()))
                                commentEt.setText(comment.getContent());

                            if (event != null) {
                                blackMeasurementBefore.setText((int) event.getMeasurement1_black() + "");
                                blackMeasurementAfter.setText((int) event.getMeasurement2_black() + "");
                                colorMeasurementBefore.setText((int) event.getMeasurement1_color() + "");
                                colorMeasurementAfter.setText((int) event.getMeasurement2_color() + "");

                            }

                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                            expanded = true;
                            selectedTank = tanks.get(finalI);
                            return false;
                        }
                    });
//                        radiusMarkerClusterer.add(mPin);


                    pinList.add(mPin);

                }
            }
        }

//        for (int i = 0; i < pinList.size(); i++) {
//            if (mMapView.getBoundingBox().getLatNorth() > Double.parseDouble(tanks.get(i).getTankLatitude()) &&
//                    mMapView.getBoundingBox().getLatSouth() < Double.parseDouble(tanks.get(i).getTankLatitude()) &&
//                    mMapView.getBoundingBox().getLonEast() > Double.parseDouble(tanks.get(i).getTankLongitude()) &&
//                    mMapView.getBoundingBox().getLonWest() < Double.parseDouble(tanks.get(i).getTankLongitude()))
//                mMapView.getOverlays().add(pinList.get(i));
//        }
        change();
        mMapView.invalidate();

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
                GeoPoint gep = new GeoPoint(event.getLatitude(), event.getLongitude());
                mMapController.setCenter(gep);
                Tank tank = Tank.getTankById(event.getTank_Id());
                districtNumber.setText(tank.getTankNumber() + "-" + tank.getDistrict()
                        + "-" + tank.getBlock());
            }

            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            expanded = true;
        }


    }

    private void change() {
        for (int i = 0; i < pinList.size(); i++) {
//            if (mMapView.getBoundingBox().getLatNorth() > Double.parseDouble(tanks.get(i).getTankLatitude()) &&
//                    mMapView.getBoundingBox().getLatSouth() < Double.parseDouble(tanks.get(i).getTankLatitude()) &&
//                    mMapView.getBoundingBox().getLonEast() > Double.parseDouble(tanks.get(i).getTankLongitude()) &&
//                    mMapView.getBoundingBox().getLonWest() < Double.parseDouble(tanks.get(i).getTankLongitude()))
//
            if (latitude + 0.000575 > Double.parseDouble(tanks.get(i).getTankLatitude()) &&
                    latitude - 0.0002575 < Double.parseDouble(tanks.get(i).getTankLatitude()) &&
                    longitude + 0.000365 > Double.parseDouble(tanks.get(i).getTankLongitude()) &&
                    longitude -
                            0.000365 < Double.parseDouble(tanks.get(i).getTankLongitude()))
                mMapView.getOverlays().add(pinList.get(i));
        }
        this.myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getActivity()), mMapView);
        this.myLocationNewOverlay.enableMyLocation();
        mMapView.getOverlays().add(this.myLocationNewOverlay);
        mMapView.invalidate();
    }

    private void hide() {
        mMapView.getOverlays().removeAll(pinList);
        mMapView.invalidate();
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
            case R.id.arrow:
                if (!expanded) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    expanded = true;
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    expanded = false;
                }
                break;
            case R.id.my_location:

                if (!TextUtils.isEmpty(SecurePreferences.getInstance(getActivity()).getString(Parameters.LATITUDE))) {
                    latitude = Double.parseDouble(SecurePreferences.getInstance(getActivity()).getString(Parameters.LATITUDE));
                }
                if (!TextUtils.isEmpty(SecurePreferences.getInstance(getActivity()).getString(Parameters.LONGITUDE))) {
                    longitude = Double.parseDouble(SecurePreferences.getInstance(getActivity()).getString(Parameters.LONGITUDE));
                }
                if (longitude != 0.0 && latitude != 0.0) {
                    GeoPoint gPt = new GeoPoint(latitude, longitude);
//                    GeoPoint gPt = new GeoPoint(32.294649, 36.327595);

                    mMapController.setCenter(gPt);
                    mMapController.setZoom(16);
                }
                break;
            case R.id.save:

                if (((MainActivity) getActivity()).displayGPSNotEnabledDialog()) {
                    if (Methods.isAutomaticTimeEnabled(getActivity())) {
//                        if (checkFields()) {
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
                                List<Tank> allTanks = Tank.getAll();
                                if (allTanks != null && !allTanks.isEmpty())
                                    for (int i = 0; i < allTanks.size(); i++) {
                                        if (allTanks.get(i).getTankId().equals(event.getTank_Id()))
                                            selectedTank = allTanks.get(i);

                                    }
                                commendid = event.getComment_Id();
                                eventId = event.getEvent_Id();
                            }

                            if (!TextUtils.isEmpty(SecurePreferences.getInstance(getActivity()).getString(Parameters.USER_ID_ABOUT))) {
                                driverId = SecurePreferences.getInstance(getActivity()).getString(Parameters.USER_ID_ABOUT);
                            }
                            if (!TextUtils.isEmpty(SecurePreferences.getInstance(getActivity()).getString(Parameters.USER_ID_FOR_LOG))) {
                                userId = SecurePreferences.getInstance(getActivity()).getString(Parameters.USER_ID_FOR_LOG);
                            } else {
                                User user = User.getUserByName(SecurePreferences.getInstance(getActivity()).getString(Parameters.USER_NUMBER));
                                userId = user.getUserId();
                            }
                            if (!TextUtils.isEmpty(SecurePreferences.getInstance(getActivity()).getString(Parameters.VEHICLE_NUMBER))) {
                                truckId = SecurePreferences.getInstance(getActivity()).getString(Parameters.VEHICLE_NUMBER);
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
                                    List<Comment> allComments = Comment.getAll();
                                    if (allComments != null && !allComments.isEmpty())
                                        for (int i = 0; i < allComments.size(); i++) {
                                            if (commendid != null)
                                                if (allComments.get(i).getComment_Id().equals(commendid))
                                                    Comment.delete(Comment.class, allComments.get(i).getId());

                                        }

                                    if (TextUtils.isEmpty(commendid))
                                        commendid = UUID.randomUUID().toString();
                                    String tankId = "";
                                    if (selectedTank != null && selectedTank.getTankId() != null)
                                        tankId = selectedTank.getTankId();

                                    Comment comment = new Comment(commendid, Methods.getCurrentTimeStamp(), commentString,
                                            userId,
                                            SecurePreferences.getInstance(getActivity()).getString("d"),
                                            tankId);
                                    comment.save();


                                    Event e = new Event(eventId, Methods.getCurrentTimeStamp(), userId, "", tankId,
                                            blackBeforeValue, blackAfterValue, colorBeforeValue, colorAfterValue,
                                            commendid, latitude, longitude);
                                    e.save();
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                    event = null;
//
//                                addComment(comment);
//                                addMeasurement(event);

                                    ActiveAndroid.setTransactionSuccessful();
                                } finally {
                                    ActiveAndroid.endTransaction();
                                }
                            }
//                        } else
//                            Toast.makeText(getActivity(), "الرجاء التأكد من المقاييس", Toast.LENGTH_LONG).show();
                    } else ((MainActivity) getActivity()).displayAutomaticTimeEnable();
                } else ((MainActivity) getActivity()).buildAlertMessageNoGps();


                break;
            case R.id.arrow_down:
                if (!expanded) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    colorMeasurementBefore.requestFocus();
                    expanded = true;
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    expanded = false;
                }
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
                if (Integer.parseInt(fB) < 5 || Integer.parseInt(fB) > 400)
                    return false;
        if (!TextUtils.isEmpty(sB))
            if (!sB.equals("0"))
                if (Integer.parseInt(sB) < 5 || Integer.parseInt(sB) > 400)
                    return false;
        if (!TextUtils.isEmpty(fC))
            if (!fC.equals("0"))
                if (Integer.parseInt(fC) < 5 || Integer.parseInt(fC) > 400)
                    return false;
        if (!TextUtils.isEmpty(sC))
            if (!sC.equals("0"))
                if (Integer.parseInt(sC) < 5 || Integer.parseInt(sC) > 400)
                    return false;
        return true;


    }
}
