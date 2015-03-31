package com.piotapps.blendle.api;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.piotapps.blendle.BuildConfig;
import com.piotapps.blendle.pojo.PopularItems;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class GetInitDataTask extends AsyncTask<String, Integer, PopularItems> {

    public interface AsynCallback {
        void started();
        void progress();
        void ended(PopularItems pi);
    }

    private AsynCallback callback;

    public GetInitDataTask(@NonNull AsynCallback callback) {
        this.callback = callback;
    }

    @Override
    protected PopularItems doInBackground(String... params) {
        final String url = params[0];

        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        // Introduce a small delay at debug to simulate slow connections
        if (BuildConfig.DEBUG) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return gson.fromJson(response.body().charStream(), PopularItems.class);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        callback.started();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        callback.progress();
    }

    @Override
    protected void onPostExecute(PopularItems pi) {
        super.onPostExecute(pi);

        callback.ended(pi);
    }
}
