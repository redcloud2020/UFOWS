package com.uni.ufows.datalayer.server;

import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.uni.ufows.config.Config;
import com.uni.ufows.config.Parameters;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**
 * Created by Sammy Shwairy on 2/28/2016.
 */
public class RequestDataProvider {

    protected Context mContext;

    public RequestDataProvider(Context mContext) {
        this.mContext = mContext;
    }
    public GetRequestModel getTrucks(String username, String password,  String timestamp) throws JSONException, UnsupportedEncodingException {
        RequestParams params = new RequestParams();
        params.put(Parameters.USERNAME, username);
        params.put(Parameters.PASSWORD, password);
        params.put(Parameters.TYPE, Parameters.TYPE_TRUCKS);
        params.put(Parameters.TIMESTAMP, timestamp);
        String url = Config.URL;
        return new GetRequestModel(url, params);
    }
    public RequestModel sendMeasurements(String username, String password, JSONArray jsonObject) throws JSONException, UnsupportedEncodingException {
        RequestParams params = new RequestParams();
        params.put(Parameters.USERNAME, username);
        params.put(Parameters.PASSWORD, password);
        params.put(Parameters.JSON_OBJECT, jsonObject);
        params.put(Parameters.TYPE, Parameters.TYPE_MEASUREMENT);
        String url = Config.URL;
        return new RequestModel(url, params);
    }
    public RequestModel addMeasurementsEmptying(String username, String password, JSONArray jsonObject) throws JSONException, UnsupportedEncodingException {
        RequestParams params = new RequestParams();
        params.put(Parameters.USERNAME, username);
        params.put(Parameters.PASSWORD, password);
        params.put(Parameters.JSON_OBJECT, jsonObject);
        params.put(Parameters.TYPE, Parameters.TYPE_MEASUREMENT_EMPTYING);
        String url = Config.URL;
        return new RequestModel(url, params);
    }
    public RequestModel addLocation(String username, String password, JSONArray jsonObject) throws JSONException, UnsupportedEncodingException {
        RequestParams params = new RequestParams();
        params.put(Parameters.USERNAME, username);
        params.put(Parameters.PASSWORD, password);
        params.put(Parameters.JSON_OBJECT, jsonObject);
        params.put(Parameters.TYPE, Parameters.TYPE_LOCATION);
        String url = Config.URL;
        return new RequestModel(url, params);
    }
    public RequestModel addIsFull(String username, String password, JSONArray jsonObject) throws JSONException, UnsupportedEncodingException {
        RequestParams params = new RequestParams();
        params.put(Parameters.USERNAME, username);
        params.put(Parameters.PASSWORD, password);
        params.put(Parameters.JSON_OBJECT, jsonObject);
        params.put(Parameters.TYPE, Parameters.TYPE_IS_FULL_ADD);
        String url = Config.URL;
        return new RequestModel(url, params);
    }
    public GetRequestModel getUsers(String username, String password,  String timestamp) throws JSONException, UnsupportedEncodingException {
        RequestParams params = new RequestParams();
        params.put(Parameters.USERNAME, username);
        params.put(Parameters.PASSWORD, password);
        params.put(Parameters.TYPE, Parameters.TYPE_USERS);
        params.put(Parameters.TIMESTAMP, timestamp);
        String url = Config.URL;
        return new GetRequestModel(url, params);
    }
    public GetRequestModel getTanks(String username, String password,  String timestamp) throws JSONException, UnsupportedEncodingException {
        RequestParams params = new RequestParams();
        params.put(Parameters.USERNAME, username);
        params.put(Parameters.PASSWORD, password);
        params.put(Parameters.TYPE, Parameters.TYPE_TANKS);
        params.put(Parameters.TIMESTAMP, timestamp);
        String url = Config.URL;
        return new GetRequestModel(url, params);
    }
    public RequestModel addComment(String username, String password, JSONArray jsonObject) throws JSONException, UnsupportedEncodingException {
        RequestParams params = new RequestParams();
        params.put(Parameters.USERNAME, username);
        params.put(Parameters.PASSWORD, password);
        params.put(Parameters.TYPE, Parameters.TYPE_COMMENT);
        params.put(Parameters.JSON_OBJECT, jsonObject);
        String url = Config.URL;
        return new RequestModel(url, params);
    }
}
