package com.uni.ufows.datalayer.server;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by Sammy Shwairy on 2/28/2016.
 */
public class MyHttpClient extends AsyncHttpClient {
    @Override
    public boolean isUrlEncodingEnabled() {
        return false;
    }

    @Override
    public void setTimeout(int value) {
        super.setTimeout(20000);
    }
}
