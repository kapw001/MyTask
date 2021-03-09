package com.mysystemtest.network;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mysystemtest.network.service.NetworkApiService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitAdapter {

    private static final String API_BASE_URL = "https://reqres.in/api/";

    private static Retrofit retrofit;

    private static OkHttpClient okHttpClient;

    public static final int UPLOAD_TIMEOUT_IN_MINUTES = 2;


    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getNormalClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static NetworkApiService getNetworkApiServiceClient() {
        Retrofit apiClient = RetrofitAdapter.getRetrofit();
        return apiClient.create(NetworkApiService.class);
    }


    private static class AuthorizationInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response mainResponse = chain.proceed(chain.request());
            Request mainRequest = chain.request();


            return mainResponse;
        }
    }


    private static OkHttpClient getNormalClient() {
        if (okHttpClient == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            Request.Builder builder = original.newBuilder()
//                                    /*.header("Content-Type", "application/json")*/
                                    .method(original.method(), original.body());

                            builder.header("Content-Type", "application/json");
//                            if (Preferences.INSTANCE.isUserLoggedIn()) {
//                                builder.header("Authorization", Preferences.INSTANCE.getApiAccessToken());
//                            }

                            Request request = builder.build();

                            return chain.proceed(request);
                        }
                    })

                    .addInterceptor(logging);
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }


    public void clearRetrofitAdapters() {
        retrofit = null;
        okHttpClient = null;
    }

}
