package com.uni.ufows.datalayer.server;

import com.loopj.android.http.RequestParams;

/**
 * Created by AppsSammy on 12/8/2016.
 */
public class GetRequestModel {

    private String url;
    private RequestParams params;

    public GetRequestModel( String url,RequestParams params) {
        this.params = params;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RequestParams getParams() {
        return params;
    }

    public void setParams(RequestParams params) {
        this.params = params;
    }
}
