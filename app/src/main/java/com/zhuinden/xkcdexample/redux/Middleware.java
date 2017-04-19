package com.zhuinden.xkcdexample.redux;

import io.reactivex.Flowable;

/**
 * Created by Owner on 2017. 04. 19..
 */

public abstract class Middleware {
    public interface Interception {
        Flowable<State> intercept(ReduxStore reduxStore, Action action);
    }

    Flowable<State> executeBefore(ReduxStore reduxStore, State state, Action action) {
        doBefore().intercept(reduxStore, action);
        return Flowable.just(state);
    }

    Flowable<State> executeAfter(ReduxStore reduxStore, State state, Action action) {
        doAfter().intercept(reduxStore, action);
        return Flowable.just(state);
    }

    public abstract Interception doBefore();

    public abstract Interception doAfter();
}
