package com.example.kzhan.zhang_kevin_shopify_winternship_2018;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Singleton that implements the OkHttp client class to create connections
 * Created by kzhan on 2017-09-06.
 */

public class ConnectionClient {
    private static ConnectionClient mInstance;
    private OkHttpClient mClient;
    private Call mCall;

    public static final String DATA_ENDPOINT =
            "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";

    public static ConnectionClient getInstance() {
        if (mInstance == null ){
            synchronized (ConnectionClient.class) {
                if (mInstance == null) {
                    mInstance = new ConnectionClient();
                }
            }
        }
        return mInstance;
    }

    private ConnectionClient() {
        mClient = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .build();
    }

    public void getData(Callback callback) {
        Request request = new Request.Builder()
                .url(DATA_ENDPOINT)
                .get()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build();

        mCall = mClient.newCall(request);

//        if (callback == null) {
//            mCall.enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                }
//            });
//        } else {
            mCall.enqueue(callback);
//        }
    }

    public void cancel() {
        if (mCall != null) {
            mCall.cancel();
        }
    }
}
