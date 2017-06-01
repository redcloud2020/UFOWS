package com.uni.ufows.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.uni.ufows.R;
import com.uni.ufows.config.Parameters;
import com.uni.ufows.datalayer.models.Comment;
import com.uni.ufows.datalayer.models.Event;
import com.uni.ufows.datalayer.models.GpsLog;
import com.uni.ufows.datalayer.models.UserWrapperModel;
import com.uni.ufows.datalayer.server.MyHttpClient;
import com.uni.ufows.datalayer.server.RequestDataProvider;
import com.uni.ufows.datalayer.server.RequestModel;
import com.uni.ufows.datalayer.server.ServerResponseHandler;
import com.uni.ufows.fragment.ProfileFragment;
import com.uni.ufows.fragment.ScheduleFragment;
import com.uni.ufows.fragment.SecondMapFragment;
import com.uni.ufows.security.SecurePreferences;
import com.uni.ufows.service.LocationService;
import com.uni.ufows.utilities.Methods;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String[] drawerItemsArray;

    private int position = 0;

    private Fragment fragment;

    private MyHttpClient myHttpClient;

    private Intent intent;

    private List<GpsLog> gpsLog;
    private List<Event> measureLog;
    private List<Comment> commentLog;

    private ProgressBar progressBar;

    private boolean isMap = true;


    protected ServiceConnection mServerConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(LOG_TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(LOG_TAG, "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        intent = new Intent(this, LocationService.class);
        bindService(intent, mServerConn, Context.BIND_AUTO_CREATE);
        startService(intent);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        drawerItemsArray = getResources().getStringArray(R.array.drawer_items_array);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getSupportActionBar().setTitle(getString(R.string.app_name));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new SecondMapFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.content_layout, fragment, "")
                .commit();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle args = new Bundle();
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (id) {

            case R.id.nav_home:
                position = 0;
                if (displayGPSNotEnabledDialog()) {
                    if (Methods.isAutomaticTimeEnabled(this)) {
                        fragment = new SecondMapFragment();
                    } else displayAutomaticTimeEnable();
                } else buildAlertMessageNoGps();

                break;
            case R.id.nav_profile:
                position = 1;
                if (displayGPSNotEnabledDialog()) {
                    if (Methods.isAutomaticTimeEnabled(this)) {
                        fragment = new ProfileFragment();
                    } else displayAutomaticTimeEnable();
                } else buildAlertMessageNoGps();

                break;
            case R.id.nav_logout:
                position = 2;
                showLogoutDialog();
                break;

            default:
                fragment = new SecondMapFragment();
                break;
        }

        if (position != 2) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_layout, fragment, "")
                    .commit();
            getSupportActionBar().setTitle(drawerItemsArray[position]);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(this, R.style.AlertDialogCustom)));
        alertDialogBuilder.setTitle(getResources().getString(R.string.are_you_logout));
        alertDialogBuilder
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        uploadAndLogout();

                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void uploadGpsLog(final JSONArray log) throws JSONException, UnsupportedEncodingException {

        myHttpClient = new MyHttpClient();
        RequestDataProvider requestDataProvider = new RequestDataProvider(this);
        RequestModel requestModel = requestDataProvider.addLocation(
                SecurePreferences.getInstance(this).getString(Parameters.USER_NUMBER),
                SecurePreferences.getInstance(this).getString(Parameters.PASSWORD),
                log
        );
        Type type = new TypeToken<UserWrapperModel>() {
        }.getType();

        myHttpClient.post(this, requestModel.getUrl(), requestModel.getParams(), new ServerResponseHandler<UserWrapperModel>(type) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onConnectivityError(String message) {
                uploadComments();
                setError(message);

            }

            @Override
            public void onDataError(String message) {
                setError(message);
                uploadComments();
            }

            @Override
            public void onServerFailure(String message) {
                setError(message);
                uploadComments();
            }


            @Override
            public void onServerSuccess(UserWrapperModel data) {
                for (int i = 0; i < gpsLog.size(); i++) {
                    GpsLog.delete(GpsLog.class, gpsLog.get(i).getId());
                }
                uploadComments();


            }

            @Override
            public void onFinish() {
                super.onFinish();
                for (int i = 0; i < gpsLog.size(); i++) {
                    GpsLog.delete(GpsLog.class, gpsLog.get(i).getId());
                }


            }
        });
    }

    private void setError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void uploadAndLogout() {
        progressBar.setVisibility(View.VISIBLE);
        gpsLog = GpsLog.selectAll();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String listString = gson.toJson(
                gpsLog,
                new TypeToken<ArrayList<GpsLog>>() {
                }.getType());

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(listString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray != null && jsonArray.length() == 0)
            try {
                uploadGpsLog(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        else uploadComments();

    }

    private void uploadMeasurements() {

        measureLog = Event.getAll();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String listString = gson.toJson(
                measureLog,
                new TypeToken<ArrayList<Event>>() {
                }.getType());

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(listString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            uploadMeasurementList(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void uploadComments() {

        commentLog = Comment.selectAll();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String listString = gson.toJson(
                commentLog,
                new TypeToken<ArrayList<Comment>>() {
                }.getType());

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(listString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            uploadComment(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void uploadComment(final JSONArray log) throws JSONException, UnsupportedEncodingException {

        myHttpClient = new MyHttpClient();
        RequestDataProvider requestDataProvider = new RequestDataProvider(MainActivity.this);
        RequestModel requestModel = requestDataProvider.addComment(
                SecurePreferences.getInstance(MainActivity.this).getString(Parameters.USER_NUMBER),
                SecurePreferences.getInstance(MainActivity.this).getString(Parameters.PASSWORD),
                log
        );
        Type type = new TypeToken<UserWrapperModel>() {
        }.getType();

        myHttpClient.post(MainActivity.this, requestModel.getUrl(), requestModel.getParams(), new ServerResponseHandler<UserWrapperModel>(type) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onConnectivityError(String message) {
                logout();
                setError(message);
            }

            @Override
            public void onDataError(String message) {
                setError(message);
                logout();
            }

            @Override
            public void onServerFailure(String message) {
                setError(message);
                logout();
            }


            @Override
            public void onServerSuccess(UserWrapperModel data) {
                for (int i = 0; i < commentLog.size(); i++) {
                    Comment.delete(Comment.class, commentLog.get(i).getId());
                }
                uploadMeasurements();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void uploadMeasurementList(final JSONArray log) throws JSONException, UnsupportedEncodingException {

        myHttpClient = new MyHttpClient();
        RequestDataProvider requestDataProvider = new RequestDataProvider(MainActivity.this);
        RequestModel requestModel = requestDataProvider.sendMeasurements(
                SecurePreferences.getInstance(MainActivity.this).getString(Parameters.USER_NUMBER),
                SecurePreferences.getInstance(MainActivity.this).getString(Parameters.PASSWORD),
                log
        );
        Type type = new TypeToken<UserWrapperModel>() {
        }.getType();

        myHttpClient.post(MainActivity.this, requestModel.getUrl(), requestModel.getParams(), new ServerResponseHandler<UserWrapperModel>(type) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onConnectivityError(String message) {
                logout();
//                setError(message);
            }

            @Override
            public void onDataError(String message) {
//                setError(message);
                logout();
            }

            @Override
            public void onServerFailure(String message) {
//                setError(message);
                logout();
            }


            @Override
            public void onServerSuccess(UserWrapperModel data) {
                for (int i = 0; i < measureLog.size(); i++) {
                    Event.delete(Event.class, measureLog.get(i).getId());
                }
                logout();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                logout();
            }
        });
    }

    public boolean displayGPSNotEnabledDialog() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private void logout() {
        progressBar.setVisibility(View.GONE);
        SecurePreferences.getInstance(MainActivity.this).put(Parameters.USER_NUMBER, "");
        SecurePreferences.getInstance(MainActivity.this).put(Parameters.DRIVER_NUMBER, "");
        SecurePreferences.getInstance(MainActivity.this).put(Parameters.VEHICLE_NUMBER, "");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setMessage(getString(R.string.gps_inactive))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.enable), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void displayAutomaticTimeEnable() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setMessage(getString(R.string.automatic_time_inactive))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.enable), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void goToFragmentSchedule() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_layout, new ScheduleFragment()).addToBackStack(ScheduleFragment.class.getName()).commit();
    }

    public void goToFragmentMap() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_layout, new SecondMapFragment()).addToBackStack(SecondMapFragment.class.getName()).commit();
    }

    public void goToFragmentMap(Event event) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_layout, SecondMapFragment.newInstance(event)).addToBackStack(SecondMapFragment.class.getName()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.schedule:
                goToFragmentSchedule();

                break;
            case R.id.map:
                goToFragmentMap();
                break;

        }
        return true;

    }

    @Override
    protected void onDestroy() {

        stopService(intent);
        unbindService(mServerConn);
        if (myHttpClient != null)
            myHttpClient.cancelAllRequests(true);
        super.onDestroy();
    }
}
