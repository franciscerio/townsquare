package com.townsquare;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static App INSTANCE;
    private static Retrofit RETROFIT;
    private static Gson GSON;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static App getInstance() {
        return INSTANCE;
    }

    public Retrofit getRetrofit() {
        if (RETROFIT == null) {
            new Retrofit.Builder()
                    .baseUrl("https://api.github.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return RETROFIT;
    }

    public Gson getGson() {
        if (GSON == null) {
            GSON = new GsonBuilder().create();
        }
        return GSON;
    }

}
