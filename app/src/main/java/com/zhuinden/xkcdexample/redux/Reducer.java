package com.zhuinden.xkcdexample.redux;

import io.reactivex.Flowable;

/**
 * Created by Owner on 2017. 04. 13..
 */

public interface Reducer {
    Flowable<State> reduce(final State state, final Action action);
}
