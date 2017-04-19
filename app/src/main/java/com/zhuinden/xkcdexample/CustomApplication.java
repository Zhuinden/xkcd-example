package com.zhuinden.xkcdexample;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.zhuinden.xkcdexample.redux.Middleware;
import com.zhuinden.xkcdexample.redux.ReduxStore;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Flowable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        xkcdService = retrofit.create(XkcdService.class);
        XkcdReducer xkcdReducer = new XkcdReducer(xkcdService, xkcdMapper, random);
        reduxStore = ReduxStore.builder().addReducer(xkcdReducer).addMiddleware(new Middleware() {
            @Override
            public Interception doBefore() {
                return (store, action) -> {
                    Log.i("MIDDLEWARE", "BEFORE: [" + store.getState() + "]");
                    return Flowable.just(store.getState());
                };
            }

            @Override
            public Interception doAfter() {
                return (store, action) -> {
                    Log.i("MIDDLEWARE", "AFTER: [" + store.getState() + "]");
                    return Flowable.just(store.getState());
                };
            }
        }).build();
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
