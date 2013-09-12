package com.voidberg.drupaldroid;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.HttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.*;

public class ServicesClient {
    private String url;
    private String rootUrl;

    public static AsyncHttpClient client = new AsyncHttpClient();

    public ServicesClient(String server, String base) {
        this.url = server + '/' + base + '/';
        this.rootUrl = server + '/';
        client.setTimeout(60000);
    }

    public void setCookieStore(PersistentCookieStore cookieStore) {
        client.setCookieStore(cookieStore);
    }

    private String getAbsoluteUrl(String relativeUrl) {
        return this.url + relativeUrl;
    }

    private String getAbsoluteRootUrl(String relativeUrl) {
      return this.rootUrl + relativeUrl;
    }

    public void get(final String url, final RequestParams params, final HttpResponseHandler responseHandler) {
        new TokenHandler(responseHandler instanceof AsyncHttpResponseHandler){
        	@Override
        	public void onFinish(){
        		client.get(getAbsoluteUrl(url), params,  responseHandler);
        	}
        };
    }

    public void post(final String url, final RequestParams params, final HttpResponseHandler responseHandler) {
        new TokenHandler(responseHandler instanceof AsyncHttpResponseHandler){
        	@Override
        	public void onFinish(){
        		client.post(getAbsoluteUrl(url), params,  responseHandler);
        	}
        };
    }

    public void post(String url, JSONObject params, HttpResponseHandler responseHandler) {
        StringEntity se = null;
        try {
            se = new StringEntity(params.toString(), HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        final String _url = url;
        final StringEntity _se = se;
        final HttpResponseHandler _responseHandler = responseHandler;
        new TokenHandler(responseHandler instanceof AsyncHttpResponseHandler){
        	@Override
        	public void onFinish(){
        		client.post(null, getAbsoluteUrl(_url), _se, "application/json", _responseHandler);
        	}
        };
    }

    public void put(final String url, final RequestParams params, final HttpResponseHandler responseHandler) {
        new TokenHandler(responseHandler instanceof AsyncHttpResponseHandler){
        	@Override
        	public void onFinish(){
        		client.put(getAbsoluteUrl(url), params,  responseHandler);
        	}
        };
    }

    public void put(String url, JSONObject params, HttpResponseHandler responseHandler) {
        StringEntity se = null;
        try {
            se = new StringEntity(params.toString(), HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        
        final String _url = url;
        final StringEntity _se = se;
        final HttpResponseHandler _responseHandler = responseHandler;
        new TokenHandler(responseHandler instanceof AsyncHttpResponseHandler){
        	@Override
        	public void onFinish(){
        		client.put(null, getAbsoluteUrl(_url), _se, "application/json", _responseHandler);
        	}
        };
    }
    
    class TokenHandler{
    	final String TAG = this.getClass().getName();
    	public TokenHandler(boolean async){
    		HttpResponseHandler responseHandler;
    		if (async){
    			responseHandler = new AsyncHttpResponseHandler(){
    				@Override
    				public void onSuccess(String response){
    					addHeader(response);
    				}
    				
    				@Override
    				public void onFinish(){
    					TokenHandler.this.onFinish();
    				}
    			};
    		}else{
    			responseHandler = new SyncHttpResponseHandler(){
    				@Override
    				public void onSuccess(String response){
    					addHeader(response);
    				}
                    
    				@Override
    				public void onFinish(){
    					TokenHandler.this.onFinish();
    				}
    			};
    		}
    		
    		client.get(null, getAbsoluteRootUrl("/services/session/token"), responseHandler);
    	}
    	
    	public void onFinish(){
    		
    	}
    	
		private void addHeader(String response) {
			client.removeHeader("X-CSRF-Token");
			client.addHeader("X-CSRF-Token", response);
		}
    }
}
