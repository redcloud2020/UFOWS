package com.uni.ufows.datalayer.server;

/**
 * Created by Sammy Shwairy on 2/28/2016.
 */

public class ErrorResponse<T> {

    private int code;
    private String debugger;
    private T error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDebugger() {
        return debugger;
    }

    public void setDebugger(String debugger) {
        this.debugger = debugger;
    }

    public T getError() {
        return error;
    }

    public void setError(T error) {
        this.error = error;
    }
}
