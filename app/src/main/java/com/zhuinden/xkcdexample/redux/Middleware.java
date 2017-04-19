package com.zhuinden.xkcdexample.redux;

import org.javatuples.Pair;

import io.reactivex.Single;

/**
 * Created by Owner on 2017. 04. 19..
 */

public abstract class Middleware {
    public interface Interception {
        Single<Action> intercept(ReduxStore reduxStore, Action action);
    }

    Single<Pair<State, Action>> executeBefore(ReduxStore reduxStore, State state, Action action) {
        Interception interception = doBefore();
        if(interception == null) {
            return Single.just(Pair.with(state, action));
        } else {
            return interception.intercept(reduxStore, action)
                    .flatMap(newAction -> Single.just(Pair.with(state, newAction)));
        }
    }

    Single<Pair<State, Action>> executeAfter(ReduxStore reduxStore, State state, Action action) {
        Interception interception = doAfter();
        if(interception == null) {
            return Single.just(Pair.with(state, action));
        } else {
            return interception.intercept(reduxStore, action)
                    .flatMap(newAction -> Single.just(Pair.with(state, newAction)));
        }
    }

    public abstract Interception doBefore();

    public abstract Interception doAfter();
}
