package com.zhuinden.xkcdexample.injection;

import java.util.Random;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Owner on 2017. 10. 17..
 */

@Module
public class RandomModule {
    @Provides
    @Singleton
    Random random() {
        return new Random();
    }
}
