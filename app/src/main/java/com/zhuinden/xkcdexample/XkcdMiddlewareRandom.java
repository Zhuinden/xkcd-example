package com.zhuinden.xkcdexample;

import com.zhuinden.xkcdexample.redux.Middleware;

import java.util.Random;

import io.reactivex.Single;

/**
 * Created by Zhuinden on 2017.04.19..
 */

public class XkcdMiddlewareRandom
        extends Middleware {
    private Random random;

    public XkcdMiddlewareRandom(Random random) {
        this.random = random;
    }

    @Override
    public Interception doBefore() {
        return (reduxStore, action) -> Single.just(action);
    }

    @Override
    public Interception doAfter() {
        return (reduxStore, action) -> Single.just(action);
    }
}
