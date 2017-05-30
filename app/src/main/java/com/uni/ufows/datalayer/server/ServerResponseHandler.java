package com.uni.ufows.datalayer.server;


import com.loopj.android.http.JsonHttpResponseHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Sammy Shwairy on 2/28/2016.
 */
public abstract class ServerResponseHandler<T> extends JsonHttpResponseHandler {

    private Type type;

    public ServerResponseHandler(Type type) {
        this.type = type;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        try {
            if (response.getInt("Response") == 1 ) {
               T serverResponse = JsonProvider.getObject(response.get("Data").toString(), getType());
                onServerSuccess(serverResponse);
            } else {
                String msg = response.getString("Message");
                onServerFailure(msg);
            }
        } catch( JSONException e) {
            onDataError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        if (statusCode == 412 || statusCode == 403 || statusCode == 411 || statusCode == 409
                || statusCode == 404 || statusCode == 406) {
            ErrorResponse<T> error = null;
            try {
                error = JsonProvider.getObject(errorResponse.toString(), getType());

                onServerFailure(error.getError(), error.getDebugger());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (throwable.getMessage() != null) {
            onConnectivityError("Please check your internet connection.");
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        if (statusCode == 200)
            onServerSuccess(statusCode);
        else if (responseString != null)
            onConnectivityError(responseString);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        if (statusCode == 200)
            onServerSuccess(statusCode);

        else if (statusCode == 412) {
            ErrorResponse<T> error = null;
            try {
                error = JsonProvider.getObject(errorResponse.toString(), getType());
                onServerFailure(error.getError(), error.getDebugger());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (throwable.getMessage() != null) {
            onConnectivityError(throwable.getMessage().toString());
        }
    }


    public ServerResponseHandler() {
        super();
    }

    public abstract void onConnectivityError(String message);

    public abstract void onDataError(String message);

    public abstract void onServerFailure(String message);

    public void onServerFailure(int statusCode, String message) {
    }

    public void onServerFailure(T data, String message) {
    }

    public abstract void onServerSuccess(T data);

    public void onServerSuccess(int code) {
    }

    ;

    public void onServerFailure(int responseCode, T data, String message) {
    }

    ;

    private void setType(Type type) {
        this.type = type;
    }

    private Type getType() {
        return this.type;
    }
}
