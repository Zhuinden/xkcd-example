package com.zhuinden.xkcdexample;

import android.app.Application;
import android.content.Context;

import com.zhuinden.xkcdexample.injection.DaggerApplicationComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Zhuinden on 2017.04.11..
 */

public class CustomApplication
        extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Injector.applicationComponent = DaggerApplicationComponent.create();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder() //
                .deleteRealmIfMigrationNeeded() //
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static CustomApplication get(Context context) {
        return (CustomApplication) context.getApplicationContext();
    }
}
