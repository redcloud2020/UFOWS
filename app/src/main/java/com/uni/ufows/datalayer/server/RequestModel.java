package com.uni.ufows.datalayer.server;

import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Sammy Shwairy on 2/28/2016.
 */
public class RequestModel {

    private String url;
    private StringEntity entity;
    private RequestParams params;
    public RequestModel() {
    }

    public RequestModel(String url, RequestParams params) {
        this.url = url;
        this.params = params;
    }

    public RequestParams getParams() {
        return params;
    }

    public void setParams(RequestParams params) {
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public StringEntity getEntity() {
        return entity;
    }

    public void setEntity(StringEntity entity) {
        this.entity = entity;
    }
}
