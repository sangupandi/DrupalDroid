package com.voidberg.drupaldroid;

import com.loopj.android.http.HttpResponseHandler;
import org.json.JSONObject;

public class SystemServices {
    private ServicesClient client;

    public SystemServices(ServicesClient c) {
        client = c;
    }

    public void connect(HttpResponseHandler responseHandler) {
        client.post("system/connect", new JSONObject(), responseHandler);
    }
}