package com.denimexpertexpo.denimexpo.BackendHttp;

/**
 * Created by ratul on 7/13/2015.
 */
public interface AsyncHttpRequestHandler {

    public void onResponseRecieved(String response);
    public void onHttpErrorOccured();
}
