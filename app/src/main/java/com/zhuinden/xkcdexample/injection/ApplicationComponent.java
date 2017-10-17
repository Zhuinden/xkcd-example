package com.zhuinden.xkcdexample.injection;

import com.zhuinden.xkcdexample.RealmManager;
import com.zhuinden.xkcdexample.XkcdPresenter;
import com.zhuinden.xkcdexample.XkcdService;

import java.util.Random;
import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by Owner on 2017. 10. 17..
 */

@Component(modules = {RandomModule.class, RetrofitModule.class, ThreadingModule.class})
@Singleton
public interface ApplicationComponent {
    Executor executor();

    Random random();

    RealmManager realmManager();

    XkcdPresenter xkcdPresenter();

    XkcdService xkcdService();

    Retrofit retrofit();
}
