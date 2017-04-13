package com.zhuinden.xkcdexample.redux;

import io.reactivex.Single;

/**
 * Created by Owner on 2017. 04. 13..
 */

public interface Reducer {
    Single<State> reduce(final State state, final Action action);
}
