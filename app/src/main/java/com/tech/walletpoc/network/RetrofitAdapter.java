package com.tech.walletpoc.network;


import com.tech.walletpoc.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitAdapter {

    private static ApiService apiService;

    public static final String BASE_URL = "http://127.0.0.1/";

    public static ApiService getInterface() {
        return apiService == null
                ? apiService = restAdapterGen(BASE_URL)
                : apiService;
    }

    public static ApiService restAdapterGen(String baseUrl) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl url = original.url().newBuilder()
                        .addQueryParameter("version", "1.0")
                        .addQueryParameter("token", "ssjsjsjsj")
                        .addQueryParameter("app_android", "1").build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("User-Agent", "Android");
                Request request = requestBuilder.url(url).build();
                return chain.proceed(request);
            }
        }).addInterceptor(getLoggingInterceptor()).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();
        ApiService service = retrofit.create(ApiService.class);
        return service;
    }

    public static HttpLoggingInterceptor getLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(BuildConfig.isLogEnabled ?
                HttpLoggingInterceptor.Level.BASIC : HttpLoggingInterceptor.Level.NONE);
    }

    public static HttpLoggingInterceptor getLoggingInterceptor(HttpLoggingInterceptor.Level level) {
        return new HttpLoggingInterceptor().setLevel(level);
    }
}
