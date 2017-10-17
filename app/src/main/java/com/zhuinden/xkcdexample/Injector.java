package com.zhuinden.xkcdexample;

import com.zhuinden.xkcdexample.injection.ApplicationComponent;

/**
 * Created by Owner on 2017. 10. 17..
 */

public class Injector {
    private Injector() {
    }

    static ApplicationComponent applicationComponent;

    public static ApplicationComponent get() {
        return applicationComponent;
    }
}
