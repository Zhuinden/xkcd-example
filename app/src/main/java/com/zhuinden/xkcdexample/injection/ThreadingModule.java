package com.zhuinden.xkcdexample.injection;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Owner on 2017. 10. 17..
 */

@Module
public class ThreadingModule {
    @Provides
    @Singleton
    Executor executor() { // single background thread
        return Executors.newSingleThreadExecutor();
    }
}
