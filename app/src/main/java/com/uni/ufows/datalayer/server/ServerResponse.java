package com.uni.ufows.datalayer.server;

/**
 * Created by Sammy Shwairy on 2/28/2016.
 */
public class ServerResponse<T> {


    private int Response;
    private String Message;
    private T Data;


    public int getResponse() {
        return Response;
    }

    public void setResponse(int response) {
        this.Response = response;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        this.Data = data;
    }
}
