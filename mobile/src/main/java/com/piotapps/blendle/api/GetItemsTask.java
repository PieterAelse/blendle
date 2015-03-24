package com.piotapps.blendle.api;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.piotapps.blendle.interfaces.AsynCallback;
import com.piotapps.blendle.pojo.PopularItems;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class GetItemsTask extends AsyncTask<String, Integer, PopularItems> {

    private AsynCallback callback;

    public GetItemsTask(AsynCallback callback) {
        this.callback = callback;
    }

    @Override
    protected PopularItems doInBackground(String... params) {
        final String url = params[0];

        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

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
