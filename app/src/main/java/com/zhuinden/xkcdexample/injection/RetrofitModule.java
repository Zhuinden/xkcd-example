package com.zhuinden.xkcdexample.injection;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.zhuinden.xkcdexample.XkcdService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Owner on 2017. 10. 17..
 */

@Module
public class RetrofitModule {
    @Provides
    @Singleton
    public Retrofit retrofit() {
        return new Retrofit.Builder().baseUrl("http://xkcd.com/").addConverterFactory(LoganSquareConverterFactory.create()).build();
    }

    @Provides
    @Singleton
    XkcdService xkcdService(Retrofit retrofit) {
        return retrofit.create(XkcdService.class);
    }
}
