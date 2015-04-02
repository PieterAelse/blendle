package com.piotapps.blendle.api;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.piotapps.blendle.BuildConfig;
import com.piotapps.blendle.interfaces.BlendleApiCallback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * The basic background task for interacting with the Blendle API. Uses a {@link BlendleApiCallback}
 * to pass the results back to the caller.
 * @param <T> The class of the return type retrieved
 */
public abstract class BlendleApiTask<T> extends AsyncTask<String, Integer, T> {

    private static final int DEBUG_NETWORK_DELAY = 3000;

    protected BlendleApiCallback callback;
    protected Class<T> returnType;

    protected BlendleApiTask(@NonNull BlendleApiCallback callback, @NonNull Class<T> returnType) {
        this.callback = callback;
        this.returnType = returnType;
    }

    @Override
    protected T doInBackground(String... params) {
        final String url = params[0];

        // Use OkHTTP and GSON to retrieve and parse the JSON
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        // Introduce a small delay at debug to simulate slow connections
        if (BuildConfig.DEBUG) {
            try {
                Thread.sleep(DEBUG_NETWORK_DELAY);
            } catch (InterruptedException e) {
                // Ignore
            }
        }

        // Build the request and execute
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // Call was succesful, let GSON parse the result to the given type of class!
                return gson.fromJson(response.body().charStream(), returnType);
            } // Else null is returned
        } catch (IOException e) {
            // An error on HTTP level has occurred
            e.printStackTrace();
        } catch (JsonParseException je) {
            // An error on GSON level has occurred
            je.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        callback.started();
    }

    /**
     * Checks the result at postExecute to not be null.
     * If null {@link BlendleApiCallback#onError()} gets called.
     * @param t the result of the background task
     * @return true if the result isn't null (and thus can be used)
     */
    protected boolean checkPostExecute(T t) {
        if(t == null) {
            callback.onError();
            return false;
        }

        return true;
    }
}
