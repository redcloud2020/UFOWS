package com.uni.ufows.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.uni.ufows.R;
import com.uni.ufows.config.Parameters;
import com.uni.ufows.datalayer.models.Comment;
import com.uni.ufows.datalayer.models.Event;
import com.uni.ufows.datalayer.models.GpsLog;
import com.uni.ufows.datalayer.models.Tank;
import com.uni.ufows.datalayer.models.TankWrapperModel;
import com.uni.ufows.datalayer.models.Truck;
import com.uni.ufows.datalayer.models.TruckWrapperModel;
import com.uni.ufows.datalayer.models.User;
import com.uni.ufows.datalayer.models.UserWrapperModel;
import com.uni.ufows.datalayer.server.GetRequestModel;
import com.uni.ufows.datalayer.server.MyHttpClient;
import com.uni.ufows.datalayer.server.RequestDataProvider;
import com.uni.ufows.datalayer.server.RequestModel;
import com.uni.ufows.datalayer.server.ServerResponseHandler;
import com.uni.ufows.security.SecurePreferences;
import com.uni.ufows.utilities.Methods;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sammy on 2/28/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText userNumberEt;
    private EditText passwordEt;

    private Button login;

    private String vehicleNumber;
    private String driverNumber;
    private String userNumber;
    private String password;

    private MyHttpClient myHttpClient;

    private ProgressBar progressBar;

    private ImageView sync;
    private List<Event> measureLog;
    private List<Comment> commentLog;
    private List<GpsLog> gpsLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNumberEt = (EditText) findViewById(R.id.user_number_edit_text);
        passwordEt = (EditText) findViewById(R.id.password_edit_text);
        sync = (ImageView) findViewById(R.id.sync);
        sync.bringToFront();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(this);
        sync.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:

                userNumber = userNumberEt.getText().toString();
                password = passwordEt.getText().toString();
                SecurePreferences.getInstance(LoginActivity.this).put(Parameters.USER, userNumber);
                if (!TextUtils.isEmpty(password))
                    if (!TextUtils.isEmpty(userNumber)) {
                                if (displayGPSNotEnabledDialog()) {
                                    if (Methods.isAutomaticTimeEnabled(LoginActivity.this)) {
                                        try {
                                            if (TextUtils.isEmpty(SecurePreferences.getInstance(this).getString(Parameters.FIRST_TIME)))
                                                getUsers(userNumber, Methods.md5(password), Parameters.DUMMY_TIMESTAMP);
                                            else {
                                                User lastUser = User.getOldDate();
                                                if(lastUser!=null && lastUser.getUpdatedAt()!=null)
                                                    getUsers(userNumber, Methods.md5(password), lastUser.getUpdatedAt());
                                                else
                                                    getUsers(userNumber, Methods.md5(password),Parameters.DUMMY_TIMESTAMP);
                                                uploadComments();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    } else displayAutomaticTimeEnable();
                                } else buildAlertMessageNoGps();
                    } else displayUserNumberMissingDialog();
                else displayPasswordMissingDialog();


                break;
            case R.id.sync:
                    uploadAndLogout();
                break;

        }
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

    private void proceedToLogin() {
        progressBar.setVisibility(View.GONE);
        User myUser = User.login(userNumber, Methods.md5(password));
        if (myUser != null) {
            SecurePreferences.getInstance(LoginActivity.this).put(Parameters.USER_NUMBER, userNumber);
            SecurePreferences.getInstance(LoginActivity.this).put("my_name", myUser.getFirstNameAr()+" "+myUser.getLastNameAr());
            SecurePreferences.getInstance(LoginActivity.this).put(Parameters.USER_ID_FOR_LOG, myUser.getUserId());
            SecurePreferences.getInstance(LoginActivity.this).put(Parameters.USER_ID_ABOUT, myUser.getEmployerId());

            SecurePreferences.getInstance(LoginActivity.this).put(Parameters.PASSWORD, Methods.md5(password));
            Intent homeIntent = new Intent(LoginActivity.this, PickDriverActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
        } else{
            displayWrongCredentials();
        progressBar.setVisibility(View.GONE);
        }

    }

    private void displayPasswordMissingDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setMessage(getString(R.string.please_enter_password))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void displayDriverMissingDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setMessage(getString(R.string.please_enter_driver_number))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(Dialog.BUTTON_NEGATIVE).setTextSize(30);

    }

    private void displayVehicleNumberDialog() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setMessage(getString(R.string.input_vehicle_number))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(Dialog.BUTTON_NEGATIVE).setTextSize(30);

    }

    private void displayUserNumberMissingDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setMessage(getString(R.string.please_input_user_number))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(Dialog.BUTTON_NEGATIVE).setTextSize(30);

    }

    private boolean displayGPSNotEnabledDialog() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private void buildAlertMessageNoGps() {
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
        alert.getButton(Dialog.BUTTON_POSITIVE).setTextSize(30);
        alert.getButton(Dialog.BUTTON_NEGATIVE).setTextSize(30);

    }

    private void displayAutomaticTimeEnable() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setMessage(getString(R.string.automatic_time_inactive))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.enable), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                    }
                });
//                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//                        dialog.cancel();
//                    }
//                }

        final AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(Dialog.BUTTON_POSITIVE).setTextSize(30);

    }

    private void displayWrongCredentials() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setMessage(getString(R.string.wrong_information))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(Dialog.BUTTON_POSITIVE).setTextSize(30);
    }


    private void getUsers(final String username, final String password, final String timestamp) throws JSONException, UnsupportedEncodingException {
        progressBar.setVisibility(View.VISIBLE);
        myHttpClient = new MyHttpClient();
        RequestDataProvider requestDataProvider = new RequestDataProvider(this);
        GetRequestModel requestModel = requestDataProvider.getUsers(username, password, timestamp);
        Type type = new TypeToken<UserWrapperModel>() {
        }.getType();

        myHttpClient.get(this, requestModel.getUrl(), requestModel.getParams(), new ServerResponseHandler<UserWrapperModel>(type) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onConnectivityError(String message) {
                proceedToLogin();
                setError(message);
            }

            @Override
            public void onDataError(String message) {
                setError(message);
            }

            @Override
            public void onServerFailure(String message) {
                setError(message);
            }


            @Override
            public void onServerSuccess(UserWrapperModel data) {
                if (data != null) {
                    insertUsers(data, username, password, timestamp);
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void setError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void insertUsers(UserWrapperModel data, String userNumber, String password, String timestamp) {

        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < data.getNewUpdatedData().size(); i++) {
                User user = new User(data.getNewUpdatedData().get(i).getUserId(), data.getNewUpdatedData().get(i).getFirstNameAr()
                        , data.getNewUpdatedData().get(i).getLastNameAr(), data.getNewUpdatedData().get(i).getFirstNameEn(),
                        data.getNewUpdatedData().get(i).getLastNameEn(),
                        data.getNewUpdatedData().get(i).getPhoneNumber(), data.getNewUpdatedData().get(i).getUserName()
                        , data.getNewUpdatedData().get(i).getPassword(), data.getNewUpdatedData().get(i).getEmployerId(),
                        data.getNewUpdatedData().get(i).getRating(), data.getNewUpdatedData().get(i).getCreatedAt(),
                        data.getNewUpdatedData().get(i).getUpdatedAt(), data.getNewUpdatedData().get(i).getIsDeleted(),
                        data.getNewUpdatedData().get(i).getRoleId(), data.getNewUpdatedData().get(i).getUfow_app());
                user.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        if (data.getDeletedData() != null && !data.getDeletedData().isEmpty() && !TextUtils.isEmpty(SecurePreferences.getInstance(this).getString(Parameters.FIRST_TIME))) {

            ActiveAndroid.beginTransaction();
            try {
                for (int i = 0; i < data.getDeletedData().size(); i++) {
                    User.delete(User.class, data.getDeletedData().get(i).getId());
                }

                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }
        try {
            if (TextUtils.isEmpty(SecurePreferences.getInstance(this).getString(Parameters.FIRST_TIME)))
                getTanks(userNumber, password, Parameters.DUMMY_TIMESTAMP);
            else {
                Tank lastTank = Tank.getOldDate();
                if(lastTank!=null && lastTank.getTimestamp()!=null)
                getTanks(userNumber, password, lastTank.getTimestamp());
                else
                getTanks(userNumber, password,Parameters.DUMMY_TIMESTAMP);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getTanks(final String username, final String password, final String timestamp) throws JSONException, UnsupportedEncodingException {

        myHttpClient = new MyHttpClient();
        RequestDataProvider requestDataProvider = new RequestDataProvider(this);
        GetRequestModel requestModel = requestDataProvider.getTanks(username, password, timestamp);
        Type type = new TypeToken<TankWrapperModel>() {
        }.getType();

        myHttpClient.get(this, requestModel.getUrl(), requestModel.getParams(), new ServerResponseHandler<TankWrapperModel>(type) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onConnectivityError(String message) {
                proceedToLogin();
                setError(message);
            }

            @Override
            public void onDataError(String message) {
                setError(message);
            }

            @Override
            public void onServerFailure(String message) {
                setError(message);
            }


            @Override
            public void onServerSuccess(TankWrapperModel data) {
                if (data != null) {
                    insertTanks(data, username, password, timestamp);
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void insertTanks(TankWrapperModel data, String userNumber, String password, String timestamp) {

        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < data.getNewUpdatedData().size(); i++) {
                Tank tank = new Tank(data.getNewUpdatedData().get(i).getTankId(), data.getNewUpdatedData().get(i).getTankLatitude()
                        , data.getNewUpdatedData().get(i).getTankLongitude(), data.getNewUpdatedData().get(i).getDistrict(),
                        data.getNewUpdatedData().get(i).getBlock(),
                        data.getNewUpdatedData().get(i).getTankNumber(), data.getNewUpdatedData().get(i).getTankType()
                        , data.getNewUpdatedData().get(i).getCreatedAt(), data.getNewUpdatedData().get(i).getTimestamp(),
                        data.getNewUpdatedData().get(i).getCommentId());
                tank.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
        if (data.getDeletedData() != null && !data.getDeletedData().isEmpty()  && !TextUtils.isEmpty(SecurePreferences.getInstance(this).getString(Parameters.FIRST_TIME))) {

            ActiveAndroid.beginTransaction();
            try {
                for (int i = 0; i < data.getDeletedData().size(); i++) {
                    Tank.delete(Tank.class, data.getDeletedData().get(i).getId());
                }

                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }

        try {
            if (TextUtils.isEmpty(SecurePreferences.getInstance(this).getString(Parameters.FIRST_TIME)))
                getTrucks(userNumber, password, Parameters.DUMMY_TIMESTAMP);
            else {
                Truck lastTruck = Truck.getOldDate();
                if(lastTruck!=null && lastTruck.getUpdated_at()!=null)
                getTrucks(userNumber, password, lastTruck.getUpdated_at());
                else
                getTrucks(userNumber, password, Parameters.TIMESTAMP);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private void getTrucks(final String username, final String password, final String timestamp) throws JSONException, UnsupportedEncodingException {

        myHttpClient = new MyHttpClient();
        RequestDataProvider requestDataProvider = new RequestDataProvider(this);
        GetRequestModel requestModel = requestDataProvider.getTrucks(username, password, timestamp);
        Type type = new TypeToken<TruckWrapperModel>() {
        }.getType();

        myHttpClient.get(this, requestModel.getUrl(), requestModel.getParams(), new ServerResponseHandler<TruckWrapperModel>(type) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onConnectivityError(String message) {
                proceedToLogin();
            }

            @Override
            public void onDataError(String message) {
                setError(message);
            }

            @Override
            public void onServerFailure(String message) {
                setError(message);
            }


            @Override
            public void onServerSuccess(TruckWrapperModel data) {
                if (data != null) {
                    insertTrucks(data, username, password, timestamp);
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
    private void insertTrucks(TruckWrapperModel data, String userNumber, String password, String timestamp) {

        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < data.getNewUpdatedData().size(); i++) {
                Truck truck = new Truck(data.getNewUpdatedData().get(i).getTruck_Id(), data.getNewUpdatedData().get(i).getTruck_number()
                        , data.getNewUpdatedData().get(i).getEmployer_Id(), data.getNewUpdatedData().get(i).getGreen_plate_number(),
                        data.getNewUpdatedData().get(i).getYellow_plate_number(),
                        data.getNewUpdatedData().get(i).getYear_of_make(), data.getNewUpdatedData().get(i).getTruck_total_capacity()
                        , data.getNewUpdatedData().get(i).getHose_length(), data.getNewUpdatedData().get(i).getIsDeleted(),
                        data.getNewUpdatedData().get(i).getCreated_at(), data.getNewUpdatedData().get(i).getUpdated_at());
                truck.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
        if (data.getDeletedData() != null && !data.getDeletedData().isEmpty()) {

            ActiveAndroid.beginTransaction();
            try {
                for (int i = 0; i < data.getDeletedData().size(); i++) {
                    Truck.delete(Truck.class, data.getDeletedData().get(i).getId());
                }

                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }

        proceedToLogin();
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
        if(jsonArray!=null && jsonArray.length()!=0)
        try {
            uploadMeasurementList(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        else {
            if(progressBar!=null)
                progressBar.setVisibility(View.GONE);
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
        if(jsonArray!=null && jsonArray.length()!=0)
        try {
            uploadComment(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }else uploadMeasurements();


    }

    private void uploadComment(final JSONArray log) throws JSONException, UnsupportedEncodingException {

        myHttpClient = new MyHttpClient();
        RequestDataProvider requestDataProvider = new RequestDataProvider(LoginActivity.this);
        RequestModel requestModel = requestDataProvider.addComment(
                SecurePreferences.getInstance(LoginActivity.this).getString(Parameters.USER),
                SecurePreferences.getInstance(LoginActivity.this).getString(Parameters.PASSWORD),
                log
        );
        Type type = new TypeToken<UserWrapperModel>() {
        }.getType();

        myHttpClient.post(LoginActivity.this, requestModel.getUrl(), requestModel.getParams(), new ServerResponseHandler<UserWrapperModel>(type) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onConnectivityError(String message) {
                setError(message);
                uploadMeasurements();
            }

            @Override
            public void onDataError(String message) {
                setError(message);
                uploadMeasurements();
            }

            @Override
            public void onServerFailure(String message) {
                setError(message);
                uploadMeasurements();
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
        RequestDataProvider requestDataProvider = new RequestDataProvider(LoginActivity.this);
        RequestModel requestModel = requestDataProvider.sendMeasurements(
                SecurePreferences.getInstance(LoginActivity.this).getString(Parameters.USER),
                SecurePreferences.getInstance(LoginActivity.this).getString(Parameters.PASSWORD),
                log
        );
        Type type = new TypeToken<UserWrapperModel>() {
        }.getType();

        myHttpClient.post(LoginActivity.this, requestModel.getUrl(), requestModel.getParams(), new ServerResponseHandler<UserWrapperModel>(type) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onConnectivityError(String message) {
                setError(message);
            }

            @Override
            public void onDataError(String message) {
                setError(message);
            }

            @Override
            public void onServerFailure(String message) {
                setError(message);
            }


            @Override
            public void onServerSuccess(UserWrapperModel data) {
                for (int i = 0; i < measureLog.size(); i++) {
                    Event.delete(Event.class, measureLog.get(i).getId());
                }

            }

            @Override
            public void onFinish() {
                if(progressBar!=null)
                progressBar.setVisibility(View.GONE);
                super.onFinish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHttpClient != null)
            myHttpClient.cancelAllRequests(true);
    }
}
