package com.zhuinden.xkcdexample.redux;

import io.reactivex.Observable;

/**
 * Created by Owner on 2017. 04. 13..
 */

public interface Reducer {
    Observable<State> reduce(final State state, final Action action);
}
