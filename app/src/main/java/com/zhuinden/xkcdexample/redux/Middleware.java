package com.zhuinden.xkcdexample.redux;

import io.reactivex.Flowable;

/**
 * Created by Owner on 2017. 04. 19..
 */

public interface Middleware {
    interface Interception {
        Flowable<State> intercept(ReduxStore reduxStore, State state, Action action);
    }

    Interception doBefore();

    Interception doAfter();
}
