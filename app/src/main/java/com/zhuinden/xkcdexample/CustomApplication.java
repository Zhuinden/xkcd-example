package com.zhuinden.xkcdexample;

import android.app.Application;
import android.content.Context;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;

/**
 * Created by Zhuinden on 2017.04.11..
 */

public class CustomApplication
        extends Application {
    Retrofit retrofit;
    XkcdService xkcdService;
    Executor executor;
    XkcdMapper xkcdMapper;
    Random random;

    ReduxStore reduxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        random = new Random();
        xkcdMapper = new XkcdMapper();
        executor = Executors.newSingleThreadExecutor();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder() //
                .deleteRealmIfMigrationNeeded() //
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        retrofit = new Retrofit.Builder().baseUrl("http://xkcd.com/")
                .addConverterFactory(LoganSquareConverterFactory.create())
                .build();
        xkcdService = retrofit.create(XkcdService.class);
        reduxStore = new ReduxStore();
    }

    public static CustomApplication get(Context context) {
        return (CustomApplication) context.getApplicationContext();
    }

    public XkcdService xkcdService() {
        return xkcdService;
    }

    public Retrofit retrofit() {
        return retrofit;
    }

    public Executor executor() {
        return executor;
    }

    public XkcdMapper xkcdMapper() {
        return xkcdMapper;
    }

    public Random random() {
        return random;
    }

    public ReduxStore reduxStore() {
        return reduxStore;
    }
}
