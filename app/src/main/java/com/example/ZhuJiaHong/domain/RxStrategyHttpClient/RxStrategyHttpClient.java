package com.example.ZhuJiaHong.domain.RxStrategyHttpClient;

import com.mdbs.starwave_meta.network.rxhttp.RxOwlHttpClient;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RxStrategyHttpClient {
    private static RxStrategyHttpClient instance;
    private final Retrofit retrofit;

    private RxStrategyHttpClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://tomcat85-5.mdevelop.com/tradingDisciplinesApi/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static RxStrategyHttpClient getInstance() {
        if (instance == null) {
            instance = new RxStrategyHttpClient();
        }
        return instance;
    }

    public MyApi getStrategyApi() {
        return retrofit.create(MyApi.class);
    }
}
