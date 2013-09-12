package com.voidberg.drupaldroid;

import com.loopj.android.http.HttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class UserServices {
    private ServicesClient client;

    public UserServices(ServicesClient c) {
        client = c;
    }

    public void login(String username, String password, HttpResponseHandler responseHandler) {
        JSONObject params = new JSONObject();
        try {
            params.put("username", username);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.post("user/login", params, responseHandler);
    }

    public void logout(HttpResponseHandler responseHandler) {
        client.post("user/logout", new JSONObject(), responseHandler);
    }
}
